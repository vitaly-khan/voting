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


#### Get all (without menus), sorted by name        
Access: ADMIN

GET     /restaurants

    curl -s http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

#### Get by ID (without menus)
Access: ADMIN

GET     /restaurants/[_restaurant id_]

    curl -s http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

#### Delete by ID
Access: ADMIN

DELETE  /restaurants/[_restaurant id_]

    curl -s -X DELETE http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

#### Create
Access: ADMIN

POST  /restaurants

Body: {"name":"[_restaurant name_]"}

    curl -s -X POST -d '{"name":"French"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

#### Update
Access: ADMIN

PUT  /restaurants/[_restaurant id_]

Body: {"name":"[_restaurant name_]"}

    curl -s -X PUT -d '{"name":"Italian"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants/100006 -u admin1@gmail.com:admin1

* * *

## REST API for Menus

#### Get by ID
Access: ADMIN

GET     /menus/[_menu id_] 

    curl -s http://localhost:8080/voting/menus/100010 -u admin1@gmail.com:admin1

#### Get today's menus (with restaurants and dishes), sorted by restaurant name

Access: EVERYONE

GET     /menus/today

    curl -s http://localhost:8080/voting/menus/todays -u user1@gmail.com:password1
#### Get menus by date (with restaurants and dishes), sorted by restaurant name
 
Access: ADMIN

GET     /menus/?date=[_ISO date_]

    curl -s http://localhost:8080/voting/menus/?date=2020-05-03 -u admin1@gmail.com:admin1

#### Delete by ID 
Access: ADMIN

DELETE  /menus/[_menu id_]

    curl -s -X DELETE http://localhost:8080/voting/menus/100008 -u admin1@gmail.com:admin1

#### Create 
Access: ADMIN

POST  /menus  

Body: {"date":"[_ISO date_"], "restaurantId":[_restaurant id_]}

    curl -s -X POST -d '{"date":"2020-05-30","restaurantId":100006}' -H 'Content-Type:application/json' http://localhost:8080/voting/menus -u admin1@gmail.com:admin1

* * *

## REST API for Dishes

#### Get by ID
Access: ADMIN

GET     /dishes/[_dish id_]

    curl -s http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Delete by ID
Access: ADMIN

DELETE  /dishes/[_dish id_]

    curl -s -X DELETE http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

#### Create
Access: ADMIN

POST  /dishes

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X POST -d '{"name":"Japanese New Dish","price": 9900,"menuId": 100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes -u admin1@gmail.com:admin1

#### Update
Access: ADMIN

PUT  /dishes/[_dish id]

Body: {"name":"[_dish name_]", "price":[_dish price_], "menuId":[_menu id_]}

    curl -s -X PUT -d '{"name": "Japanese Updated Dish","price":77000,"menuId":100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes/100016 -u admin1@gmail.com:admin1

* * *

## REST API for Votes


#### Get all votes on a specific date (with Restaurants, without users and dishes)
Access: ADMIN

GET     /votes/filter

    curl -s http://localhost:8080/voting/votes/filter?date=2020-05-03 -u admin1@gmail.com:admin1
#### Get all today's votes (with Restaurants, without users and dishes)
Access: ADMIN

GET     /votes/todays

    curl -s http://localhost:8080/voting/votes/todays -u admin1@gmail.com:admin1
#### Get all votes of an authenticated user (with Restaurants, without users and dishes) 
Access: REGULAR USER

GET     /votes

    curl -s http://localhost:8080/voting/votes/ -u user1@gmail.com:password1
#### Vote for a specific menu 
Access: REGULAR USER

POST /votes?menuId={_menu id_}

    curl -s -X POST http://localhost:8080/voting/votes?menuId=100010 -u user1@gmail.com:password1
    
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
    
