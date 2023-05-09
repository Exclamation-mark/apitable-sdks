package com.apitable.client.api;

import java.util.ArrayList;
import java.util.List;

import com.apitable.client.api.http.ApiCredential;
import com.apitable.client.api.model.CreateFieldRequest;
import com.apitable.client.api.model.CreateFieldResponse;
import com.apitable.client.api.model.builder.CreateFieldRequestBuilder;
import com.apitable.client.api.model.field.FieldTypeEnum;
import com.apitable.client.api.model.field.property.CheckboxFieldProperty;
import com.apitable.client.api.model.field.property.CreatedTimeFieldProperty;
import com.apitable.client.api.model.field.property.CurrencyFieldProperty;
import com.apitable.client.api.model.field.property.DateTimeFieldProperty;
import com.apitable.client.api.model.field.property.EmptyProperty;
import com.apitable.client.api.model.field.property.FormulaFieldProperty;
import com.apitable.client.api.model.field.property.LastModifiedByFieldProperty;
import com.apitable.client.api.model.field.property.LastModifiedTimeFieldProperty;
import com.apitable.client.api.model.field.property.MagicLinkFieldProperty;
import com.apitable.client.api.model.field.property.MagicLookupFieldProperty;
import com.apitable.client.api.model.field.property.MemberFieldProperty;
import com.apitable.client.api.model.field.property.NumberFieldProperty;
import com.apitable.client.api.model.field.property.PercentFieldProperty;
import com.apitable.client.api.model.field.property.RatingFieldProperty;
import com.apitable.client.api.model.field.property.SingleSelectFieldProperty;
import com.apitable.client.api.model.field.property.SingleTextFieldProperty;
import com.apitable.client.api.model.field.property.TextFieldProperty;
import com.apitable.client.api.model.field.property.option.CollectTypeEnum;
import com.apitable.client.api.model.field.property.option.DateFormatEnum;
import com.apitable.client.api.model.field.property.option.Format;
import com.apitable.client.api.model.field.property.option.FormatTypeEnum;
import com.apitable.client.api.model.field.property.option.NumberFormat;
import com.apitable.client.api.model.field.property.option.PrecisionEnum;
import com.apitable.client.api.model.field.property.option.RollUpFunctionEnum;
import com.apitable.client.api.model.field.property.option.SelectOption;
import com.apitable.client.api.model.field.property.option.SymbolAlignEnum;
import com.apitable.client.api.model.field.property.option.TimeFormatEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.apitable.client.api.ConstantKey.TEST_API_KEY;
import static com.apitable.client.api.ConstantKey.TEST_DATASHEET_ID;
import static com.apitable.client.api.ConstantKey.TEST_HOST_URL;
import static com.apitable.client.api.ConstantKey.TEST_SPACE_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class FieldAddOperationTest {

    private final String SPACE_ID = System.getenv("SPACE_ID");

    private final String DATASHEET_ID = System.getenv("DATASHEET_ID");

    private final String LINKED_DATASHEET_ID = System.getenv("LINKED_DATASHEET_ID");

    private final String LINKED_VIEW_ID = System.getenv("LINKED_VIEW_ID");

    private final String DOMAIN = System.getenv("DOMAIN");

    private final String HOST_URL = "https://"+DOMAIN;

    private final String API_KEY = System.getenv("TOKEN");

    private final ApitableApiClient apitableApiClient = new ApitableApiClient(HOST_URL, new ApiCredential(API_KEY));

    private String cacheFieldId;

    @Test
    void testCreateSingleTextField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        SingleTextFieldProperty singleTextFieldProperty = new SingleTextFieldProperty();
        CreateFieldRequest<SingleTextFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.SingleText)
                .withName("singleText")
                .withProperty(singleTextFieldProperty)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("singleText");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateSingleTextFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        SingleTextFieldProperty singleTextFieldProperty = new SingleTextFieldProperty();
        singleTextFieldProperty.setDefaultValue("defaultValue");
        CreateFieldRequest<SingleTextFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.SingleText)
                .withName("singleTextWithOtherInfo")
                .withProperty(singleTextFieldProperty)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("singleTextWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateTextField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        TextFieldProperty property = new TextFieldProperty();
        CreateFieldRequest<TextFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Text)
                .withName("text")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("text");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateTextFieldWithoutProperty() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Text)
                .withName("textWithoutProperty")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("textWithoutProperty");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateSingleSelectField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        List<SelectOption> options = new ArrayList<>();
        SelectOption firstOption = new SelectOption();
        firstOption.setName("first");
        SelectOption secondOption = new SelectOption();
        secondOption.setName("second");
        options.add(firstOption);
        options.add(secondOption);
        SingleSelectFieldProperty property = new SingleSelectFieldProperty();
        property.setOptions(options);
        CreateFieldRequest<SingleSelectFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.SingleSelect)
                .withName("singleSelect")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("singleSelect");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateSingleSelectFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        List<SelectOption> options = new ArrayList<>();
        SelectOption firstOption = new SelectOption();
        firstOption.setName("first");
        firstOption.setColor("deepPurple_0");
        SelectOption secondOption = new SelectOption();
        secondOption.setName("second");
        options.add(firstOption);
        options.add(secondOption);
        SingleSelectFieldProperty property = new SingleSelectFieldProperty();
        property.setDefaultValue("first");
        property.setOptions(options);
        CreateFieldRequest<SingleSelectFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.SingleSelect)
                .withName("singleSelectWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("singleSelectWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateNumberField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        NumberFieldProperty property = new NumberFieldProperty();
        property.setPrecision(PrecisionEnum.POINT0);
        CreateFieldRequest<NumberFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Number)
                .withName("number")
                .withProperty(property).build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("number");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateNumberFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        NumberFieldProperty property = new NumberFieldProperty();
        property.setPrecision(PrecisionEnum.POINT1);
        property.setDefaultValue("1");
        property.setSymbol("million");
        CreateFieldRequest<NumberFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Number)
                .withName("numberWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("numberWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateCurrencyField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CurrencyFieldProperty property = new CurrencyFieldProperty();
        CreateFieldRequest<CurrencyFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Currency)
                .withName("currency")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("currency");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateCurrencyFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CurrencyFieldProperty property = new CurrencyFieldProperty();
        property.setPrecision(PrecisionEnum.POINT0);
        property.setDefaultValue("1");
        property.setSymbol("$");
        property.setSymbolAlign(SymbolAlignEnum.Default);
        CreateFieldRequest<CurrencyFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Currency)
                .withName("currencyWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("currencyWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreatePercentField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        PercentFieldProperty property = new PercentFieldProperty();
        property.setPrecision(PrecisionEnum.POINT0);
        CreateFieldRequest<PercentFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Percent)
                .withName("percent")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("percent");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreatePercentFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        PercentFieldProperty property = new PercentFieldProperty();
        property.setPrecision(PrecisionEnum.POINT0);
        property.setDefaultValue("1");
        CreateFieldRequest<PercentFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Percent)
                .withName("percentWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("percentWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateDateTimeFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        DateTimeFieldProperty property = new DateTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        property.setTimeFormat(TimeFormatEnum.HOUR_MINUTE_24);
        property.setAutoFill(true);
        property.setIncludeTime(true);
        CreateFieldRequest<DateTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.DateTime)
                .withName("dateTimeWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("dateTimeWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateDateTimeField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        DateTimeFieldProperty property = new DateTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        CreateFieldRequest<DateTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.DateTime)
                .withName("DateTime")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("DateTime");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateAttachmentField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Attachment)
                .withName("attachment")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("attachment");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateMemberField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        MemberFieldProperty property = new MemberFieldProperty();
        CreateFieldRequest<MemberFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Member)
                .withName("member")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("member");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateMemberFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        MemberFieldProperty property = new MemberFieldProperty();
        property.setMulti(false);
        property.setShouldSendMsg(false);
        CreateFieldRequest<MemberFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Member)
                .withName("memberWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("memberWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateCheckboxField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CheckboxFieldProperty property = new CheckboxFieldProperty();
        property.setIcon("sweat_smile");
        CreateFieldRequest<CheckboxFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Checkbox)
                .withName("checkbox")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("checkbox");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateRatingField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        RatingFieldProperty property = new RatingFieldProperty();
        property.setIcon("sweat_smile");
        property.setMax(5);
        CreateFieldRequest<RatingFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Rating)
                .withName("rating")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("rating");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateURLField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.URL)
                .withName("url")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("url");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreatePhoneField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Phone)
                .withName("phone")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("phone");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateEmailField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Email)
                .withName("email")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("email");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateMagicLinkField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        MagicLinkFieldProperty property = new MagicLinkFieldProperty();
        property.setForeignDatasheetId(LINKED_DATASHEET_ID);
        CreateFieldRequest<MagicLinkFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.MagicLink)
                .withName("magicLink")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("magicLink");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateMagicLinkFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        MagicLinkFieldProperty property = new MagicLinkFieldProperty();
        property.setForeignDatasheetId(LINKED_DATASHEET_ID);
        property.setLimitToViewId(LINKED_VIEW_ID);
        property.setLimitSingleRecord(true);
        CreateFieldRequest<MagicLinkFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.MagicLink)
                .withName("magicLinkWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("magicLinkWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateFormalField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        FormulaFieldProperty formulaFieldProperty = new FormulaFieldProperty();
        formulaFieldProperty.setExpression("1 / 3");
        Format<NumberFormat> numberFormatFormat = new Format<>();
        numberFormatFormat.setType(FormatTypeEnum.Number);
        NumberFormat numberFormat = new NumberFormat();
        numberFormat.setPrecision(3);
        numberFormatFormat.setFormat(numberFormat);
        formulaFieldProperty.setFormat(numberFormatFormat);
        CreateFieldRequest<FormulaFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.Formula)
                .withName("formula")
                .withProperty(formulaFieldProperty)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("formula");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateAutoNumberField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.AutoNumber)
                .withName("autoNumber")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("autoNumber");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreatedTimeField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreatedTimeFieldProperty property = new CreatedTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        CreateFieldRequest<CreatedTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.CreatedTime)
                .withName("createdTime")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("createdTime");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreatedTimeFieldWithOtherInfo() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreatedTimeFieldProperty property = new CreatedTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        property.setTimeFormat(TimeFormatEnum.HOUR_MINUTE_24);
        property.setIncludeTime(true);
        CreateFieldRequest<CreatedTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.CreatedTime)
                .withName("createdTimeWithOtherInfo")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("createdTimeWithOtherInfo");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateLastModifiedTimeFieldWithRemindAllField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        LastModifiedTimeFieldProperty property = new LastModifiedTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        property.setCollectType(CollectTypeEnum.ALL);
        CreateFieldRequest<LastModifiedTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.LastModifiedTime)
                .withName("lastModifiedTime")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("lastModifiedTime");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateLastModifiedTimeFieldWithRemindSpecifiedField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        LastModifiedTimeFieldProperty property = new LastModifiedTimeFieldProperty();
        property.setDateFormat(DateFormatEnum.DATE_SLASH);
        property.setCollectType(CollectTypeEnum.SPECIFIED);
        List<String> fieldIds = new ArrayList<>();
        fieldIds.add("fld00fkxDmSfj");
        property.setFieldIdCollection(fieldIds);
        CreateFieldRequest<LastModifiedTimeFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.LastModifiedTime)
                .withName("lastModifiedTimeWithRemindSpecifiedField")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("lastModifiedTimeWithRemindSpecifiedField");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateCreatedByField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        CreateFieldRequest<EmptyProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.CreatedBy)
                .withName("createdByField")
                .withoutProperty()
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("createdByField");
        cacheFieldId = response.getId();
    }

    @Test
    void testCreateLastModifiedByFieldField() {
        FieldApi fieldApi = apitableApiClient.getFieldApi();
        LastModifiedByFieldProperty property = new LastModifiedByFieldProperty();
        property.setCollectType(CollectTypeEnum.ALL);
        CreateFieldRequest<LastModifiedByFieldProperty> request = CreateFieldRequestBuilder
                .create()
                .ofType(FieldTypeEnum.LastModifiedBy)
                .withName("LastModifiedBy")
                .withProperty(property)
                .build();
        CreateFieldResponse response = fieldApi.addField(SPACE_ID, DATASHEET_ID, request);
        assertThat(response.getName()).isEqualTo("LastModifiedBy");
        cacheFieldId = response.getId();
    }

    @AfterEach
    void tearDown() {
        if (cacheFieldId != null) {
            FieldApi fieldApi = apitableApiClient.getFieldApi();
            fieldApi.deleteField(SPACE_ID, DATASHEET_ID, cacheFieldId);
        }
    }

}
