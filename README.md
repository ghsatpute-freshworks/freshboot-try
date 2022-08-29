# Freshworks Boot samples

This repo contains sample projects for building different types of microservices using
[freshworks-boot](https://github.com/freshdesk/freshworks-boot/) starters/libraries.

For up-to date instructions, please refer [Confluence page](https://confluence.freshworks.com/display/FDCORE/Getting+Started+-+Freshworks+Boot)

Steps for starting a new micro-service development
==============
1. Create a new Git repo for your project
1. Copy `./samples/*` from freshworks-boot-samples into the root directory in master branch.
1. Start developing your modules based on the files in the root.

Setting up development environment
==================================
* Install [Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/macos-install.html)
* Configure our corporate maven repository by creating `~/.m2/settings.xml` with following content.
 Replace SYSTEM_USERNAME and SYSTEM_PASSWORD with `nexusread` username and password following the instructions in [Nexus Central access & troubleshooting steps](https://confluence.freshworks.com/pages/viewpage.action?pageId=242305410).

        <settings>
          <mirrors>
            <mirror>
              <!--This sends everything else to /public -->
              <id>nexus</id>
              <mirrorOf>*</mirrorOf>
              <url>https://nexuscentral.runwayci.com/repository/maven-public/</url>
            </mirror>
          </mirrors>
          <profiles>
            <profile>
              <id>nexus</id>
              <!--Enable snapshots for the built in central repo to direct -->
              <!--all requests to nexus via the mirror -->
              <repositories>
                <repository>
                  <id>central</id>
                  <url>http://central</url>
                  <releases><enabled>true</enabled></releases>
                  <snapshots><enabled>true</enabled></snapshots>
                </repository>
              </repositories>
             <pluginRepositories>
                <pluginRepository>
                  <id>central</id>
                  <url>http://central</url>
                  <releases><enabled>true</enabled></releases>
                  <snapshots><enabled>true</enabled></snapshots>
                </pluginRepository>
              </pluginRepositories>
            </profile>
          </profiles>
          <servers>
            <server>
                <id>nexus</id>
                <username>SYSTEM_USERNAME</username>
                <password>SYSTEM_PASSWORD</password>
            </server>
          </servers>
          <activeProfiles>
            <!--make the profile active all the time -->
            <activeProfile>nexus</activeProfile>
          </activeProfiles>
        </settings>
        
* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw flyway:migrate -pl db/
        
* For running an API server, please refer [API readme](./api/README.md).
* For running an SQS message processor, please refer [SQS Processor readme](./sqs-processor/README.md).
* For running a Kafka message processor, please refer [Kafka Processor readme](./kafka-processor/README.md).

Setting up development environment using Docker
===============================================
* Run `docker-compose up` command to create and start docker containers for all micro services and mysql-db container.
* ssh to mysql-db instance and create a database.
* To run the migrations, ssh to any app container and use the below flyway cli commands to run the migrations

        flyway -configFiles=/build/flyway.conf migrate

