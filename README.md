## Task

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and curl commands to get data for voting and vote.
* * *
## REST API for Restaurants

***(for ADMINs only)***

#### Get all
GET     /restaurants

    curl -s http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

#### Get by ID
GET     /restaurants/[_restaurant id_]

    curl -s http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

#### Delete by ID
DELETE  /restaurants/[_restaurant id_]

    curl -s -X DELETE http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

#### Create
POST  /restaurants

Body: {"name":"[_restaurant name_]"}

    curl -s -X POST -d '{"name":"French"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

#### Update
PUT  /restaurants/[_restaurant id_]

Body: {"name":"[_restaurant name_]"}

    curl -s -X PUT -d '{"name":"Italian"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

* * *

## REST API for Menus

#### Get by ID
GET     /menus/[_menu id_] ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/menus/100010 -u admin1@gmail.com:admin1

#### Get today's menus 

GET     /menus/today ***(for EVERYONE)***

    curl -s http://localhost:8080/voting/menus/todays -u user1@gmail.com:password1
#### Get menus by date
 
GET     /menus/?date=[_ISO date_] ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/menus/?date=2020-05-03 -u admin1@gmail.com:admin1

#### Delete by ID 
DELETE  /menus/[_menu id_] ***(for ADMINs only)***

    curl -s -X DELETE http://localhost:8080/voting/menus/100008 -u admin1@gmail.com:admin1

#### Create 
POST  /menus ***(for ADMINs only)***  

Body: {"date":"[_ISO date_"], "restaurantId":[_restaurant id_]}

    curl -s -X POST -d '{"date":"2020-05-30","restaurantId":100006}' -H 'Content-Type:application/json' http://localhost:8080/voting/menus -u admin1@gmail.com:admin1

* * *

## REST API for Dishes
***(for ADMINs only)***

#### Get by ID
GET     /dishes/[_dish id_]

    curl -s http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Delete by ID
DELETE  /dishes/[_dish id_]

    curl -s -X DELETE http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Create
POST  /dishes

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X POST -d '{"name":"Japanese New Dish","price": 9900,"menuId": 100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes -u admin1@gmail.com:admin1

#### Update
PUT  /dishes/[_dish id]

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X PUT -d '{"name": "Japanese Updated Dish","price":77000,"menuId":100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes/100016 -u admin1@gmail.com:admin1

* * *

## REST API for Votes


#### Get all votes on a specific date 
GET     /votes/filter ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/votes/filter?date=2020-05-08 -u admin1@gmail.com:admin1
#### Get all votes of an authenticated user 
GET     /votes ***(for REGULAR USERs only)***

    curl -s http://localhost:8080/voting/votes/ -u user1@gmail.com:password1
#### Vote for a specific menu 
POST /votes?menuId={_menu id_} ***(for REGULAR USERs only)***

    curl -s -X POST http://localhost:8080/voting/votes?menuId=100010 -u user1@gmail.com:password1
    
* * *

## REST API for Users


#### Register as a Regular User 
POST /profile/register ***(for ANONYMOUS only)***
    
Body: {"name":"[_user name_]", "email":"[_e-mail_]", "password":"[_password_]"}

    curl -s -X POST -d '{"name": "New User", "email":"tEsT@gmail.com","password":"test-password"}' -H 'Content-Type:application/json' http://localhost:8080/voting/profile/register

