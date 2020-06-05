# MoneySplittingBot

[![](https://img.shields.io/badge/Spring_Boot-2.3.0-yellowgreen)](https://spring.io/projects/spring-boot)
[![](https://img.shields.io/badge/Telegrambots_Abilities-4.7-lightgrey)](https://github.com/rubenlagus/TelegramBots/tree/master/telegrambots-abilities)
[![](https://img.shields.io/badge/Lombok-1.18.8-red)](https://projectlombok.org)
[![](https://img.shields.io/badge/Maven-4.0.0-green)](https://maven.apache.org)
[![](https://img.shields.io/badge/JUnit-5.0-blue)](https://junit.org/junit5)
[![](https://img.shields.io/badge/de.flapdoodle.embed.mongo-2.2.0-orange)](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)


Telegram bot for easier management expenses in groups of people. 

#### Technologies stack:

- Application: We used Spring Boot framework to create a telegram Bot using this [library](https://github.com/rubenlagus/TelegramBots/tree/master/telegrambots-abilities) <br>
- Database: MongoDB Atlas. We used [Spring Boot Data Mongodb](https://spring.io/guides/gs/accessing-data-mongodb/) 
to connect to the database and creating collections with documents.

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
spring.data.mongodb.password: <your_password>
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
  * Spring boot mongo data does not provide utility for making ids auto-generated (incremented). So we created our own collection and service for handling records id. Service implementation are located [here](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/serviceImpl/SequenceGeneratorServiceImpl.java) 

Database models are in [model.db](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/model/db)

## Features:

1. Creating group of Telegram people.
2. Adding purchase with information about product, buyer, person who that purchase is made for and total amount; current balances are updated after adding the new purchase.
3. Adding new payment with update of current balances when some user paid to another user.
4. Displaying the information about current balances of the user, i.e. displaying the list of all users belonging to a grup with total amount of debt, where positive numbers mean that someone owes that user and negative numbers mean that the user owes someone.
5. Displaying performed payments of the user.

## Project guide

### Repositories

### Services

[Services](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/service) are responsible for receiving and updating current information in the database. They are used in [abilities](#handlers) handling user’s requests.  
- [GroupService](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/GroupService.java) is responsible for handling requests related to group chats. It can add a new user to the group, check if the user belongs to a certain group, add a new group, update group or find group by its ID.
- [PersonService](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/PersonService.java) is responsible for handling requests related to users. It can add new users to the database, update user states or get its states.
- [PaymentService](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/PaymentService.java) is responsible for handling requests related to user’s payments. It can find payments of a certain user, confirmed and unconfirmed payments of certain recipient or payer, confirmed or unconfirmed payments of a group or find a payment by ID.
- [PurchaseService](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/PurchaseService.java) is responsible for handling requests related to purchases. It can add new purchases or remove them from the database, return all purchases of a certain group, return purchases of a certain buyer or receiver and find a purchase by ID.
- [BalanceService](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/service/BalanceService.java) is responsible for getting balance states of a certain user, i.e. how much this user owes others or how much others owe him/her (this difference is shown by the sing before amount: positive amounts mean that others owe to the user and negative numbers mean that the user owes them).


### Handlers

Handlers respond to various user's actions and events. We use them directly in [MoneySplittingBot](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/bot/MoneySplittingBot.java) class.

There are two situations when bot gets in contact with users. 
1. When user directly asks bot to do something - [Ability handlers](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/controller/ability)
    
    User types one of these commands in Telegram chat:
    - /addpurchase - [AddPurchaseHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/ability/AddPurchaseHandler.java).
    - /addpayment - [AddPaymentHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/ability/AddPaymentHandler.java).
    - /checkbalance - [CheckBalanceHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/ability/CheckBalanceHandler.java).
    - /checkpayments - [CheckPaymentsHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/ability/CheckPaymentsHandler.java).
    
    Then bot notices them and appropriately responds to the user asking for more information or displaying handy buttons.
    
    Every one of these handlers implements [CommandHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/ability/CommandHandler.java) interface.
    The most important methods from this interface are:
    ```java
           List<BotApiMethod<?>> primaryAction(Update update);
           List<BotApiMethod<?>> secondaryAction(Update update);
    ```
    The primaryAction method is called as soon as user texts command starting with '/'.
    
    The secondaryAction method is called later, after user specifies what exactly he/she wants to get or to do or clicks a button.
    
    Both of these methods use the update from Telegram and return list of BotApiMethods.
    BotApiMethod is something that displays information to the user.
    We execute them in the mentioned above MoneySplittingBot.
    
2. When something related to the bot happens - [Reply handlers](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/controller/reply).
   
   The bot constantly gets a lot of updates from Telegram with different events.
   To separate important from unimportant ones we use [conditions](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/controller/reply/condition).
   To respond to the events we choose, we use [handlers](https://github.com/nazkord/MoneySplittingBot/tree/master/src/main/java/com/dbteam/controller/reply/handlers).
   There two types of these handlers:
   1. When bot responds to the events generated solely by user.
        - user adds bot to the group chat - [BotAddedToGroupChatHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/reply/handlers/event/BotAddedToGroupChatHandler.java).
   2. When bot responds to the events generated by user interacting with bot messages.
        - user clicks a button to say that user wants to be involved in group expenses management - [NewMemberInGroupHandler](https://github.com/nazkord/MoneySplittingBot/blob/master/src/main/java/com/dbteam/controller/reply/handlers/callback/NewMemberInGroupHandler.java).


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

