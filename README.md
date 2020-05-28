# MoneySplittingBot

[![](https://img.shields.io/badge/Spring_Boot-2.3.0-yellowgreen)](https://spring.io/projects/spring-boot)
[![](https://img.shields.io/badge/Telegrambots_Abilities-4.7-lightgrey)](https://github.com/rubenlagus/TelegramBots/tree/master/telegrambots-abilities)
[![](https://img.shields.io/badge/Lombok-1.18.8-red)](https://projectlombok.org)
[![](https://img.shields.io/badge/Maven-4.0.0-green)](https://maven.apache.org)
[![](https://img.shields.io/badge/JUnit-5.0-blue)](https://junit.org/junit5)
[![](https://img.shields.io/badge/de.flapdoodle.embed.mongo-2.2.0-orange)](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)


Telegram bot for easier management expenses in groups of people. 

#### Technologies stack:

- Database: MongoDB Atlas
- Bot: Spring Boot

## Database

For the purpose of our project we chose MongoDB, mainly because of its flexibility of data model. <br>
In order to gain the availability of the database between contributors we decided to create MongoDB Atlas Cluster which is running in AWS / Frankfurt (eu-central-1)

### Setup

In order to connect your MongoDB Atlas Cluster fill the following 
[propetries](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/resources/application.properties)
with: 
```java
spring.data.mongodb.username: <your_username>
```

 And add the following line to the envorinment variables of your project:

```java
spring.data.mongodb.password: <your_pasword>
```

If you are not fimiliar with MongoDB Atlas please checkout the tutorail [here](https://docs.atlas.mongodb.com/connect-to-cluster/)

### Structure

We design a database structure with following collections:

* Users
  * Fields: username, botStates, list of groupChatIds
* Groups
  * Fields: groupTitle, list of users
* Purchases
  * When you buy something for the some group of people and you want to split the bill between them
* Payments
  * When a user made a payment to somebody, user should register it in telegram group
* Sequence_id
  * Spring boot mongo data does not provide utility for making ids auto-generated (incremented). So we created your own collection and service for handling records id.

Database models are in [model.db](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/model/db)

## Features:

1. Creating group of Telegram people.
2. Adding purchase with information about product, buyer, person who that purchase is made for and total amount; current balances are updated after adding the new purchase.
3. Adding new payment with update of current balances when some user paid to another user.
4. Displaying the information about current balances of the user, i.e. displaying the list of all users belonging to a grup with total amount of debt, where positive numbers mean that someone owes that user and negative numbers mean that the user owes someone.
5. Displaying performed payments of the user.

## Contributors 

* <a href="https://github.com/xenoteo"><b>Ksenia Fiodarava</b></a>:
  * Development of user and group services.
  * Development of /checkbalance bot feature.
* <a href="https://github.com/nazkord"><b>Nazar Kordiumov</b></a>
  * Configuration of MongoDB Atlas database.
  * Incorporating Spring Boot into the project.
  * Securing project credentials.
  * Initial creation of repositories and services.
  * Development of unit tests for repositories
  * Development of /addpayment bot feature
* <a href="https://github.com/szelemeh"><b>Stanislav Shelemekh</b></a>
  * Design of the structure of the database.
  * Design of the bot features.
  * Creation of state, balance services more specific to bot’s features.
  * Development of  /addpurchase, /checkpayments bot features.
  * Development of bot’s reactions to being added to a group chat.

