BIN_NAME = python3 -m
PROTO_DIR = protos

ifeq ($(OS), Windows_NT)
	BIN = ${BIN_NAME}.exe
else
	BIN = ${BIN_NAME}
endif

run:	generate
	${BIN} server.py

generate:
	protoc -I${PROTO_DIR} --python_out=${PROTO_DIR} ${PROTO_DIR}/*.proto

clean:
	rm ${PROTO_DIR}/*_pb2.py
