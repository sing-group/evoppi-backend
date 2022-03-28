# EvoPPI Backend

EvoPPI allows the easy comparison of publicly available data from the main Protein-Protein Interaction (PPI) databases for distinct species. EvoPPI allows two types of queries: (i) same species comparisons, for those queries involving two or more interactomes from a single species, and (ii) distinct species comparisons, for those queries involving two or more interactomes from two distinct species.

## Development
### Running the application
The application has been configured to be easily run locally, by just invoking
a Maven command.

To do so, Maven will download (if it is not already) a clean WildFly
distribution to the `target` folder, configure it, start it and deploy the
application on it.

This makes very easy and straightforward to manually test the application.

#### Configure a local MySQL
To execute the application you need a MySQL server running in `localhost` and
using the default port (3306).

In this server you have to create a database named `evoppi` accessible for the
`evoppi` user using the `evoppipass` password.

This can be configured executing the follow SQL sentences in your MySQL:

```SQL
CREATE DATABASE evoppi;
GRANT ALL ON evoppi.* TO evoppi@localhost IDENTIFIED BY 'evoppipass';
FLUSH PRIVILEGES;
```

Of course, this configuration can be changed in the POM file.

#### Building the application
The application can be built with the following Maven command:

```
mvn clean install
```

This will build the application launching the tests on a **Wildfly 10.1.0**
server.

#### Starting the application
The application can be started with the following Maven command:

```
mvn package wildfly:start wildfly:deploy-only -P wildfly-mysql-run
```

This will start a **WildFly 10.1.0**.

#### Redeploying the application
Once it is running, the application can be re-deployed with the following Maven
command:

```
mvn package wildfly:deploy-only -P wildfly-mysql-run
```

#### Stopping the application
The application can be stopped with the following Maven command:

```
mvn wildfly:shutdown
```

### REST API documentation
The REST API is documented using the [Swagger](https://swagger.io/) framework.
It can be browsed using the [Swagger UI](http://petstore.swagger.io/)
application to access the following URL:

```
http://localhost:8080/evoppi/rest/api/swagger.json
```

## Server configuration

EvoPPI can be installed in your own server. We recommend using a WildFly 10+
server. In addition, a Docker installation listening in a TCP port is required.

To install your own EvoPPI instance you should follow these steps.

### 1. Configure your WildFly

In the `additional-material/wildfly10/standalone.xml` file you can see a sample
configuration. This file includes the security, email and naming bindings
required by the application.

### 2. Create the database

A SQL script for MySQL to create the structure of the database required can be
downloaded from
[here](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/evoppi-mysql.sql).

### 3. Populate database (optional)

If you want to add the data of all the species currently supported by EvoPPI,
you can also download and import
[this SQL file](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/evoppi.sql.gz).

However, if you only want to add the data of some specific species, you can
download and import these SQL files:

* *[Bos taurus](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Bos_taurus.sql.gz)*
* *[Caenorhabditis elegans](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Caenorhabditis_elegans.sql.gz)*
* *[Danio rerio](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Danio_rerio.sql.gz)*
* *[Drosophila melanogaster](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Drosophila_melanogaster.sql.gz)*
* *[Gallus gallus](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Gallus_gallus.sql.gz)*
* *[Homo sapiens](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Homo_sapiens.sql.gz)*
* *[Mus musculus](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Mus_musculus.sql.gz)*
* *[Oryctolagus cuniculus](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Oryctolagus_cuniculus.sql.gz)*
* *[Rattus norvegicus](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Rattus_norvegicus.sql.gz)*
* *[Xenopus laevis](http://static.sing-group.org/EvoPPI/db/sql/1.0.0/Xenopus_laevis.sql.gz)*

### 4. Deploy the application

The last step is to deploy the EvoPPI application in the WildFly server.

Packaged application can be downloaded from
[here](https://maven.sing-group.org/repository/maven-releases/org/sing_group/evoppi-ear/1.0.0/evoppi-ear-1.0.0.ear).

This file can be directly deployed in the WildFly server, for example, using the
administration web interface.

## Source code

Source code of this and **EvoPPI Frontend** projects can be found at:

* [EvoPPI Frontend](https://github.com/sing-group/evoppi-frontend)
* [EvoPPI Backend](https://github.com/sing-group/evoppi-backend)

## Troubleshooting and debugging

Some tips for troubleshooting and debugging issues with the database:

1. [This post](https://stackoverflow.com/a/678310/1821422) shows how to set MySQL to show the last queries being executed.
2. When loading the complete DB, the following error may arise: `The total number of locks exceeds the lock table size`. Following some of the suggestions of [this post](https://stackoverflow.com/questions/6901108/the-total-number-of-locks-exceeds-the-lock-table-size), it usually works editing the `my.cnf` file to set the following settings:

```[mysqld]                                                                                                                          
wait_timeout = 31536000
interactive_timeout = 31536000
max_allowed_packet = 1G
innodb_buffer_pool_size = 1G 
