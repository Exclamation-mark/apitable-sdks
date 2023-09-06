
# Makefile for convinient code run on local development

.PHONY: install
install:
	cd apitable.js && yarn
	cd apitable.py && make install
	cd apitable.java && mvn --batch-mode --update-snapshots verify

.PHONY: test
test:
	cd apitable.js && yarn test:coverage
	cd apitable.py && make test
	cd apitable.java && mvn --batch-mode test -Dmaven.test.skip=false

.PHONY: test_js
test_js:
	make setup_env && cd apitable.js && yarn test:coverage

setup_env:
    export DOMAIN=integration.vika.ltd; \
    export TOKEN=uskM9c6MzfkHMeCJVipM1zv; \
    export DATASHEET_ID=dstcxPgGaxbx6vW4fZ; \
    export FOLDER_ID=fodg5Cqug2i1D; \
    export SPACE_ID=spcNmQArL7SDk; \
    export VIEW_ID=viwEt4bW0cBRE