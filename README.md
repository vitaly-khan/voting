[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2810ceb6cddd485089bbc6da1d80899d)](https://www.codacy.com/manual/vitaly-khan/voting?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=vitaly-khan/voting&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/vitaly-khan/voting.svg?branch=master)](https://travis-ci.org/vitaly-khan/voting)
## Task

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring Boot) without frontend.

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and curl commands to get data for voting and vote.
* * *
#### [Test data is available here](test_data.md)
* * *

## REST API for Restaurants


#### Get all enabled (without menus) ordered by name        
Access: ADMIN

GET     /restaurants

    curl -s http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1
    
#### Get all (without menus)
Access: ADMIN

GET     /restaurants/history

    curl -s http://localhost:8080/voting/restaurants/history -u admin1@gmail.com:admin1

#### Get by ID (without menus)
Access: ADMIN

GET     /restaurants/[_restaurant id_]

    curl -s http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

#### Delete by ID
Access: ADMIN
<br>Not allowed if restaurant has at least one menu.

DELETE  /restaurants/[_restaurant id_]

    curl -s -X DELETE http://localhost:8080/voting/restaurants/100010 -u admin1@gmail.com:admin1

#### Enable/disable (cascade on disable)
Access: ADMIN

PATCH  /restaurants/[_restaurant id_]?enabled=[_boolean value_]

    curl -s -X PATCH http://localhost:8080/voting/restaurants/100008?enabled=false -u admin1@gmail.com:admin1

#### Create
Access: ADMIN

POST  /restaurants

Body: {"name":"[_restaurant name_]"}

    curl -s -X POST -d '{"name":"Greek"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

#### Update
Access: ADMIN

PUT  /restaurants/[_restaurant id_]

Body: {"name":"[_restaurant name_]"}

    curl -s -X PUT -d '{"name":"Spanish"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants/100009 -u admin1@gmail.com:admin1

* * *

## REST API for Menus

#### Get by ID
Access: ADMIN

GET     /menus/[_menu id_] 

    curl -s http://localhost:8080/voting/menus/100011 -u admin1@gmail.com:admin1

#### Get today's (with restaurants and dishes) ordered by restaurant name, enabled & not empty only

Access: EVERYONE

GET     /menus/today

    curl -s http://localhost:8080/voting/menus/todays
    
#### Get enabled by date (with restaurants and dishes)
Access: ADMIN

GET     /menus/?date=[_ISO date_]

    curl -s http://localhost:8080/voting/menus/?date=2020-05-03 -u admin1@gmail.com:admin1

#### Get all by date (with restaurants and dishes)
Access: ADMIN

GET     /menus/history?date=[_ISO date_]

    curl -s http://localhost:8080/voting/menus/history?date=2020-05-03 -u admin1@gmail.com:admin1

#### Delete by ID
Access: ADMIN
<br>Not allowed if menu has at least one dish.

DELETE  /menus/[_menu id_]

    curl -s -X DELETE http://localhost:8080/voting/menus/100016 -u admin1@gmail.com:admin1

#### Enable/disable (cascade on disable)
Access: ADMIN
<br>Enabling isn't allowed while restaurant is disabled.

PATCH  /menus/[_menu id_]?enabled=[_boolean value_]

    curl -s -X PATCH http://localhost:8080/voting/menus/100015?enabled=false -u admin1@gmail.com:admin1

#### Create 
Access: ADMIN
<br>Creating backdated menu isn't allowed.

POST  /menus  

Body: {"date":"[_ISO date_]", "restaurantId":[_restaurant id_]}

    curl -s -X POST -d '{"date":"2021-05-30","restaurantId":100006}' -H 'Content-Type:application/json' http://localhost:8080/voting/menus -u admin1@gmail.com:admin1

#### Update 
Access: ADMIN
<br>Is allowed if it affects today's or future menus only.

POST  /menus/[_menu id_]

Body: {"date":"[_ISO date_]", "restaurantId":[_restaurant id_]}


    curl -s -X PUT -d '{"date":"2021-05-30","restaurantId":100007}' -H 'Content-Type:application/json' http://localhost:8080/voting/menus/100017 -u admin1@gmail.com:admin1

* * *

## REST API for Dishes

#### Get by ID
Access: ADMIN

GET     /dishes/[_dish id_]

    curl -s http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Delete by ID
Access: ADMIN
<br>Is allowed if dish belongs to today's or future menus only.

DELETE  /dishes/[_dish id_]

    curl -s -X DELETE http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Enable/disable
Access: ADMIN
<br>Enabling isn't allowed while menu is disabled.


PATCH  /dishes/[_dish id_]?enabled=[_boolean value_]

    curl -s -X PATCH http://localhost:8080/voting/dishes/100023?enabled=false -u admin1@gmail.com:admin1

#### Create
Access: ADMIN
<br>Is allowed for today's or future menus only.

POST  /dishes

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X POST -d '{"name":"Korean New Dish","price": 9900,"menuId": 100014}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes -u admin1@gmail.com:admin1

#### Update
Access: ADMIN
<br>Is allowed if it affects today's or future menus only.

PUT  /dishes/[_dish id_]

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X PUT -d '{"name": "Korean Updated Dish","price":77000,"menuId":100017}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes/100026 -u admin1@gmail.com:admin1

* * *

## REST API for Votes


#### Get all by date (with restaurants, without users and dishes)
Access: ADMIN

GET     /votes/filter

    curl -s http://localhost:8080/voting/votes/filter?date=2020-05-03 -u admin1@gmail.com:admin1
#### Get today's (with restaurants, without users and dishes)
Access: ADMIN

GET     /votes/todays

    curl -s http://localhost:8080/voting/votes/todays -u admin1@gmail.com:admin1
#### Get all of the authenticated user (with restaurants, without users and dishes) 
Access: REGULAR USER

GET     /votes

    curl -s http://localhost:8080/voting/votes/ -u user1@gmail.com:password1
#### Vote for menu 
Access: REGULAR USER

PUT /votes?menuId=[_menu id_]

    curl -s -X PUT http://localhost:8080/voting/votes?menuId=100014 -u user1@gmail.com:password1
    
* * *

## REST API for Users

#### Get the current profile 
Access: AUTHENTICATED

GET /profile
    
    curl -s http://localhost:8080/voting/profile -u user1@gmail.com:password1

#### Disable the current profile
Access: AUTHENTICATED

PUT /profile/disable
    
    curl -s -X PUT http://localhost:8080/voting/profile/disable -u user1@gmail.com:password1

#### Register as a Regular User 
Access: ANONYMOUS

POST /profile/register 
    
Body: {"name":"[_user name_]", "email":"[_e-mail_]", "password":"[_password_]"}

    curl -s -X POST -d '{"name": "New User", "email":"nEw@gmail.com","password":"new-password"}' -H 'Content-Type:application/json' http://localhost:8080/voting/profile/register

#### Update the current profile 
Access: AUTHENTICATED

PUT /profile/update

Body: {"name":"[_user name_]", "email":"[_e-mail_]", "password":"[_password_]"}
    
    curl -s -X PUT -d '{"name": "Updated User", "email":"uPdAtEd@gmail.com","password":"updated-password"}' -H 'Content-Type:application/json' http://localhost:8080/voting/profile -u user2@gmail.com:password2
    
