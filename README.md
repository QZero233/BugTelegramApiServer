# Api Server of  BugTelegram

## 	What is it

It's the api server of BugTelegram

It provides api interfaces, through which client can interact with BugTelegram server

By now it has had several modules as follows

Module Name | Module Description
--- | ---
Authorize Module | Provide authorize services such as login, token check, freeze account etc. 
Admin Module | Provide interfaces for admin.Supported functions such as add user, delete user etc. (**P.S. Update user's password is not supported**) 

## Environment and standard

It's written with [Springboot](https://spring.io/projects/spring-boot)

Api standard: Meet REST standard

Database requirement: Mysql (Recommended version: 8.0.15)

Java requirement: Version over or equal to 1.8



## How to start

### 1.Install mysql and java, configurate environment variables

### 2.Configurate database connection properties

Go to dao/src/main/resources/

Edit **application-prod.yml** as follows

```yaml
spring:
  datasource:
    type: org.springframework.jdbc.datasource.DriverManagerDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${Your username}
    url: jdbc:mysql://localhost:3306/nasa?serverTimezone=${Your timezone(such as GMT%2B8)}&useUnicode=true&characterEncoding=utf8
    password: ${Your password}
```

Replace \${Your username},\${Your timezone(such as GMT%2B8)},\${Your password} with yours

### 3.Package project and deploy

Use

```shell
mvn package
```

in project root to package project

find the product jar and upload to your server

### 4.Run your server

Run with --spring.profiles.active=prod

```shell
java -jar server.jar --spring.profiles.active=prod
```



## Contact

Email 2540384328@qq.com