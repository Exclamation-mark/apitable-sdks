import env from 'dotenv';
import fs from 'fs';
import { Buffer } from 'buffer';
import path from 'path';
import { APITable } from '../lib';
import { IDatasheetCreateRo, INodeItem, IRecord } from '../lib/interface';
import { IDatasheetFieldCreateRo } from '../lib/interface/datasheet.field.create.ro';
import { PermissionType, EmbedLinkTheme  } from '../lib/interface/embed.link';
import { IAddOpenSingleTextFieldProperty } from '../lib/interface/field.create.property';

env.config();

jest.setTimeout(30000);

describe('full pipeline', () => {
  const host = 'https://integration.vika.ltd/fusion/v1';
  const token = process.env.TOKEN as string || 'uskM9c6MzfkHMeCJVipM1zv';
  const datasheetId = process.env.DATASHEET_ID as string || 'dstcxPgGaxbx6vW4fZ';
  const folderId = process.env.FOLDER_ID as string || 'fodg5Cqug2i1D';
  const spaceId = process.env.SPACE_ID as string || 'spcNmQArL7SDk';
  const viewId = process.env.VIEW_ID as string || 'viwEt4bW0cBRE';


  // const host = 'https://vika.cn/fusion/v1';
  // const token = process.env.TOKEN as string || 'uskM9c6MzfkHMeCJVipM1zv';
  // const datasheetId = process.env.DATASHEET_ID as string || 'dstDDvlojfsVhWSfse';
  // const folderId = process.env.FOLDER_ID as string || 'fodWGD6LirjMM';
  // const spaceId = process.env.SPACE_ID as string || 'spcBcZN1UZZJz';
  // const viewId = process.env.VIEW_ID as string || 'viwZgTwp3cgAN';

  console.log('token', token);
  console.log('datasheetId', datasheetId);
  const apitable = new APITable({
    token,
    host,
  });

  const datasheet = apitable.datasheet(datasheetId);

  let records: IRecord[];
  let spaceIds: string[];
  let nodes: INodeItem[];
  let createdDatasheetId: string;
  let createdFieldId: string;

  it('fieldKey', async () => {
    const apitable = new APITable({
      token,
      host,
      fieldKey: 'id',
    });
    const datasheet = apitable.datasheet(datasheetId);
    console.time('list records');
    const result = await datasheet.records.query();
    console.timeEnd('list records');
    if (!result.success) {
      console.error(result);
    }
    expect(result.success).toBeTruthy();
    records = result.data!.records;
    expect(Object.keys(records[0].fields).every(key => key.startsWith('fld'))).toBeTruthy();
  });
  // Read the initial.
  it('list records', async () => {
    console.time('list records');
    const result = await datasheet.records.query({ sort: [{ field: 'Title', order: 'desc' }] });
    console.timeEnd('list records');
    if (!result.success) {
      console.error(result);
    }
    expect(result.success).toBeTruthy();
    records = result.data!.records;
  });

  it('delete records', async () => {
    console.time('delete records');
    const result = await datasheet.records.delete(records.slice(0, 10).map(record => record.recordId));
    console.timeEnd('delete records');

    expect(result.success).toBeTruthy();
  });

  it('add records', async () => {
    const recordsToAdd = [{
      fields: {
        'Title': 'One new row of records' + (new Date).toString(),
      }
    }];

    console.time('add records');
    const result = await datasheet.records.create(recordsToAdd);
    console.timeEnd('add records');
    if (!result.success) {
      console.error(result);
    }
    expect(result.success).toBeTruthy();
    records = result.data!.records;
    expect(result.data!.records.length).toEqual(recordsToAdd.length);
  });

  it('update records', async () => {
    const recordsToUpdate: IRecord[] = [{
      recordId: records[0].recordId,
      fields: {
        'Title': 'A row of modified records' + (new Date).toString(),
      }
    }];

    console.time('update records');
    const result = await datasheet.records.update(recordsToUpdate);
    console.timeEnd('update records');

    expect(result.success).toBeTruthy();
    expect(result.data!.records.length).toEqual(recordsToUpdate.length);
    const all = await datasheet.records.query();
    // The length should be the same as the original datasheet.
    expect(all.data!.records.length).toEqual(records.length);
  });

  it('upload buffer attachment', async () => {
    const buf = Buffer.from('hello world', 'utf8');
    console.time('upload attachment');
    const result = await datasheet.upload(buf, { filename: 'text.txt' });
    if (!result.success) {
      console.error(result);
    }
    console.timeEnd('upload attachment');
    expect(result.success).toBeTruthy();
  });

  it('upload attachment', async () => {
    const file = fs.createReadStream(path.join(__dirname, '../tsconfig.json'));
    console.time('upload attachment');
    const result = await datasheet.upload(file);
    if (!result.success) {
      console.error(result);
    }
    console.timeEnd('upload attachment');
    expect(result.success).toBeTruthy();
  });
  // spaces list
  it('get space list', async () => {
    const result = await apitable.spaces.list();
    expect(result.success).toBeTruthy();
    spaceIds = result.data!.spaces.map(item => item.id);
  });
  // nodes list 
  it('get node list', async () => {
    const result = await apitable.nodes.list({ spaceId: spaceIds[0] });
    expect(result.success).toBeTruthy();
    nodes = result.data!.nodes;
  });
  // node detail
  it('get node detail', async () => {
    const firstNode = nodes[0];
    const result = await apitable.nodes.get({ spaceId: spaceIds[0], nodeId: firstNode.id });
    expect(result.success).toBeTruthy();
    expect(result.data?.id).toEqual(firstNode.id);
  });
  // nodes search 
  it('search node', async () => {
    const firstNode = nodes[0];
    const result = await apitable.nodes.search({ spaceId: spaceIds[0], type: firstNode.type, query: firstNode.name });
    expect(result.success).toBeTruthy();
  });

  it('create datasheet', async () => {
    const ro: IDatasheetCreateRo = {
      name: 'New single test form.',
      folderId
    };
    const res = await apitable.space(spaceId).datasheets.create(ro);
    expect({ success: res.success, message: res.message }).toBeTruthy();
    createdDatasheetId = res.data?.id||'';
  });

  it('create field', async () => {
    const property: IAddOpenSingleTextFieldProperty = {
      defaultValue: 'I am the default value.'
    };
    const ro: IDatasheetFieldCreateRo = {
      name: 'New text field.',
      type: 'SingleText',
      property
    };
    const res = await apitable.space(spaceId).datasheet(createdDatasheetId).fields.create(ro);
    expect(res.success).toBeTruthy();
    expect(res.data?.id).toBeDefined();
    createdFieldId = res.data?.id||'';
  });

  it('delete field', async () => {
    const res = await apitable.space(spaceId).datasheet(createdDatasheetId).fields.delete(createdFieldId);
    expect(res.success).toBeTruthy();
  });

  let embedId: string;

  it('create embed link', async() => {
    const embedLinkCreateRo = {
      "paylod": {
        "primarySideBar": { "collapsed": false },
        "viewControl": {
          "viewId": viewId,
          "tabBar": false,
          "toolBar": {
            "basicTools": false,
            "shareBtn": false,
            "widgetBtn": false,
            "apiBtn": false,
            "formBtn": false,
            "historyBtn": false,
            "robotBtn": false,
          },
          "collapsed": false,
        },
        "bannerLogo": true,
        "permissionType": PermissionType.READONLY,
      },
      "theme": EmbedLinkTheme.Light,
    };
    const res = await apitable.space(spaceId).datasheet(datasheetId).createEmbedLink(embedLinkCreateRo);
    console.log('response', res);
    expect(res.success).toBeTruthy();
    embedId = res.data?.linkId || '';
  })

  it('get embed links', async() => {
    const res = await apitable.space(spaceId).datasheet(datasheetId).getEmbedLinks();
    const embedsLinks = res.data || []
    expect(embedsLinks.some(v => v.linkId === embedId)).toBeTruthy();
  })

  it('delete embed link', async() => {
    jest.advanceTimersByTime(100);
    const res = await apitable.space(spaceId).datasheet(datasheetId).deleteEmbedLink(embedId);
    expect(res.success).toBeTruthy();
  })

});
