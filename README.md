### Clonar
git clone https://github.com/arcariel/sql-extension.git sql-db-store

### Compilar
mvn clean install -DskipTests

### Ejecutar integración
cd sql-db-store-integration-tests
mvn verify -DskipTests=false
