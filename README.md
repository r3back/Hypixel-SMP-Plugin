# Hypixel-SMP Plugin

Spigot Plugin that allow players to create their own mc-server using docker image and also giving them access to the server trough vsftpd server.

# Used Tools

* [Google-Guice](https://github.com/google/guice) is used as dependency injection framework.
* [FasterXML-Jackson](https://github.com/FasterXML/jackson) is used to handle configuration serialization and deserialization.
* [Docker-Java](https://github.com/docker-java/docker-java) is used with linux terminal commands to handle containers.
* [Lettuce-Core](https://github.com/lettuce-io/lettuce-core) is used to handle redis pub/sub communication.
* [docker-vsftpd](https://github.com/r3back/docker-vsftpd) is used as vsftpd server image and [db-util](https://packages.ubuntu.com/search?keywords=db-util) to save users credentials. 

# What do you need to use these plugins?

* Spigot Server 1.8 or newest
* Redis
* Docker
* MySQL/SQLite DB

## Run application
This App uses Maven to handle dependencies & building.

* You can create a profile with maven directory and deploy automatically project.
* You can run it through maven command

#### Build Requirements
* Java 8 JDK or newest
* Maven
* Git

```
mvn clean:install
```

## License
This App is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/r3back/users-service/blob/master/LICENSE.txt) for more info.