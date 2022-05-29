#!/bin/bash
set -e

SCRIPTS_DIR=$PWD
PROJECT_DIR=$(dirname $SCRIPTS_DIR)

WSDL_URL=${WSDL_URL:-'http://localhost:8080/jaxws/BookService?wsdl'}
WSDL_CLASSES_PACKAGE="ru.anokhin.jaxws.client"

WSDL_CLASSES_MODULE_DIR="$PROJECT_DIR/jax-ws-service/client/src/main/java"
WSDL_CLASSES_DIR="$WSDL_CLASSES_MODULE_DIR/${WSDL_CLASSES_PACKAGE//.//}"

if ! command -v wsimport &> /dev/null; then
    echo "<wsimport> could not be found" 1>&2
    exit 100
fi

if [[ ! -d $WSDL_CLASSES_MODULE_DIR ]]; then
    echo "WSDL classes module directory was not found: $WSDL_CLASSES_MODULE_DIR" 1>&2
    exit 101
fi

if [[ -d $WSDL_CLASSES_DIR ]]; then
    echo "Removing all classes in directory: $WSDL_CLASSES_DIR"
    find $WSDL_CLASSES_DIR -type f -a \( -name '*.java' -o -name '*.class' \) -delete
fi

cd $WSDL_CLASSES_MODULE_DIR
wsimport -keep -p $WSDL_CLASSES_PACKAGE $WSDL_URL
cd $SCRIPTS_DIR

# Perform migration from javax to jakarta by replacing imports
# javax.jws -> jakarta.jws
# javax.xml.bind -> jakarta.xml.bind
# javax.xml.ws -> jakarta.xml.ws
find $WSDL_CLASSES_DIR -type f -name '*.java' | xargs sed -i  's/javax\.jws/jakarta\.jws/g'
find $WSDL_CLASSES_DIR -type f -name '*.java' | xargs sed -i  's/javax\.xml\.bind/jakarta\.xml\.bind/g'
find $WSDL_CLASSES_DIR -type f -name '*.java' | xargs sed -i  's/javax\.xml\.ws/jakarta\.xml\.ws/g'
