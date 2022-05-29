# Book Service

## Initializing environment

Install PostgreSQL using Docker

```shell
# Create directory for PostgreSQL database
PGDIR=$HOME/Data/books-pgsql
mkdir -p PGDIR

# Run PosgreSQL docker container
docker run -d --restart unless-stopped --name "books-pgsql" \
    -e POSTGRES_USER="books" \
    -e POSTGRES_PASSWORD="books" \
    -e POSTGRES_DB="books" \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v $PGDIR:/var/lib/postgresql/data \
    -p 5432:5432 \
    postgres:14.2
```

Download Glassfish and start domain

```shell
INSTALL_DIR=$(mktemp -d)
wget -O $INSTALL_DIR/'glassfish-6.2.5.zip' \
    'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.2.5.zip'
unzip $INSTALL_DIR/glassfish-6.2.5.zip -d $INSTALL_DIR

GLASSFISH_HOME="$INSTALL_DIR/glassfish6"
wget -O $GLASSFISH_HOME/glassfish/lib/postgresql-42.3.5.jar \
    'https://repo1.maven.org/maven2/org/postgresql/postgresql/42.3.5/postgresql-42.3.5.jar'
```

Then you need to add executable to `PATH`

```shell
export PATH="$GLASSFISH_HOME/bin:$PATH"
```

... or make an alias
```shell
alias asadmin="$GLASSFISH_HOME/bin/asadmin"
```

After Glassfish was installed, new domain without password should be created and started

```shell
asadmin create-domain --adminport 4848 --nopassword=true anokhin
asadmin start-domain anokhin
```

Then open <http://localhost:4848> and configure JDBC DataSource

![Glassfish Books Pool properties](docs/img/glassfish_books_pool.png)

![Glassfish Books Pool properties](docs/img/glassfish_books_pool_additional_properties.png)

![Glassfish default JDBC resource](docs/img/glassfish_default_jdbc_resource.png)

Then you need to build artifacts

```shell
git clone <repository_url> web-services-technologies-anokhin
cd web-services-technologies-anokhin
./gradlew build

git clone https://github.com/eclipse/transformer.git transformer
git config --global --add safe.directory $PWD/transformer
cd transformer
git checkout 0.4.0
mvn package
cd -
```

---

## Deploying services

Deploy JAX WS service

```shell
VERSION=$(awk -F= '$1=="version"{print $2}' gradle.properties)
WAR_FILENAME="jaxws-j2ee-${VERSION}.war"

./gradlew build
asadmin undeploy ${WAR_FILENAME%.*} 2> /dev/null >&2 || true
asadmin deploy \
    --contextroot '/jaxws' \
    jax-ws-service/bundles/j2ee/build/libs/${WAR_FILENAME}
```

Deploy REST service

```shell
VERSION=$(awk -F= '$1=="version"{print $2}' gradle.properties)
WAR_FILENAME="replaced-rest-j2ee-${VERSION}.war"
TRANSFORMER_CLI="$PWD/transformer/org.eclipse.transformer.cli/target/org.eclipse.transformer.cli-0.4.0.jar"

./gradlew build
$TRANSFORMER_CLI --overwrite \
    rest-service/bundles/j2ee/build/libs/rest-j2ee-${VERSION}.war \
    rest-service/bundles/j2ee/build/libs/${WAR_FILENAME}
    
asadmin undeploy ${WAR_FILENAME%.*} 2> /dev/null >&2 || true
asadmin deploy \
    --contextroot '/rest' \
    rest-service/bundles/j2ee/build/libs/${WAR_FILENAME}
```

---

## Install CLIs

Install JAX WS CLI
```shell
VERSION=$(awk -F= '$1=="version"{print $2}' gradle.properties)
ARCHIVE_ID="cli-$VERSION"
JAXWS_INSTALL_DIR=$(mktemp -d)
TEMP_DIR=$(mktemp -d)

./gradlew build
unzip -q "jax-ws-service/bundles/cli/build/distributions/$ARCHIVE_ID.zip" -d "$TEMP_DIR"
rsync --remove-source-files -r "$TEMP_DIR"/"$ARCHIVE_ID"/* "$JAXWS_INSTALL_DIR"

alias jaxws_cli="$JAXWS_INSTALL_DIR/bin/cli"
```

Install REST CLI
```shell
VERSION=$(awk -F= '$1=="version"{print $2}' gradle.properties)
ARCHIVE_ID="cli-$VERSION"
REST_INSTALL_DIR=$(mktemp -d)
TEMP_DIR=$(mktemp -d)

./gradlew build
unzip -q "rest-service/bundles/cli/build/distributions/$ARCHIVE_ID.zip" -d "$TEMP_DIR"
rsync --remove-source-files -r "$TEMP_DIR"/"$ARCHIVE_ID"/* "$REST_INSTALL_DIR"

alias rest_cli="$REST_INSTALL_DIR/bin/cli"
```

## Use CLIs

Use JAX WS CLI
```shell
# Create book
jaxws_cli create \
    --name 'Волновые процессы. Основные законы' \
    --author 'И.Е.Иродов' \
    --publisher 'Лаборатория знаний' \
    --publication-date 2014-05-08 \
    --page-count 266

# Find book
jaxws_cli find --publisher 'Лаборатория знаний'

# Update book
jaxws_cli update \
    --id 1 \
    --page-count 265
    
# Remove book
jaxws_cli remove --id 1
```

Use REST CLI
```shell
# Create book
rest_cli create \
    --name 'Волновые процессы. Основные законы' \
    --author 'И.Е.Иродов' \
    --publisher 'Лаборатория знаний' \
    --publication-date 2014-05-08 \
    --page-count 266

# Find book
rest_cli find --publisher 'Лаборатория знаний'

# Update book
rest_cli update \
    --id 1 \
    --page-count 265
    
# Remove book
rest_cli remove --id 1
```
