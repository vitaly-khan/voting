
##REST API for Restaurant
***(for ADMINs only)***

####Get all
GET     /restaurants

    curl -s http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

####Get by ID
GET     /restaurants/{restaurantId}

    curl -s http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

####Delete by ID
DELETE  /restaurants/{restaurantId}

    curl -s -X DELETE http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

####Create
POST  /restaurants

Body: {"name":[<i>restaurant name</i>]}

    curl -s -X POST -d '{"name":"French"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants -u admin1@gmail.com:admin1

####Update
PUT  /restaurants/{restaurantId}

Body: {"name":[<i>restaurant name</i>]}

    curl -s -X PUT -d '{"name":"Italian"}' -H 'Content-Type:application/json' http://localhost:8080/voting/restaurants/100005 -u admin1@gmail.com:admin1

<hr>

##REST API for Menu

####Get by ID 
GET     /menus/{menuId} ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/menus/100010 -u admin1@gmail.com:admin1

####Get today's menus ***(for EVERYONE)***
GET     /menus/today

    curl -s http://localhost:8080/voting/menus/todays -u user1@gmail.com:password1
####Get menus by date 
GET     /menus/?date={ISO date} ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/menus/?date=2020-05-03 -u admin1@gmail.com:admin1

####Delete by ID 
DELETE  /menus/{menuId} ***(for ADMINs only)***

    curl -s -X DELETE http://localhost:8080/voting/menus/100008 -u admin1@gmail.com:admin1

####Create 
POST  /menus ***(for ADMINs only)***  

Body: {"date":"[<i>ISO date</i>"], "restaurantId":[<i>restaurant id</i>]}

    curl -s -X POST -d '{"date":"2020-05-30","restaurantId":100006}' -H 'Content-Type:application/json' http://localhost:8080/voting/menus -u admin1@gmail.com:admin1

<hr>

##REST API for Dishes
***(for ADMINs only)***

####Get by ID
GET     /dishes/{dishId}

    curl -s http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

####Delete by ID
DELETE  /dishes/{dishId}

    curl -s -X DELETE http://localhost:8080/voting/dishes/100024 -u admin1@gmail.com:admin1

####Create
POST  /dishes

Body: {"name":"[<i>dish name</i>]", "price":[<i>dish price</i>], "menuId":[<i>menu id</i>]}

    curl -s -X POST -d '{"name":"Japanese New Dish","price": 9900,"menuId": 100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes -u admin1@gmail.com:admin1

####Update
PUT  /dishes/{dishId}

Body: {"name":"[<i>dish name</i>]", "price":[<i>dish price</i>], "menuId":[<i>menu id</i>]}

    curl -s -X PUT -d '{"name": "Japanese Updated Dish","price":77000,"menuId":100009}' -H 'Content-Type:application/json' http://localhost:8080/voting/dishes/100016 -u admin1@gmail.com:admin1

<hr>

##REST API for Votes


####Get all votes on specific date 
GET     /votes/filter ***(for ADMINs only)***

    curl -s http://localhost:8080/voting/votes/filter?date=2020-05-08 -u admin1@gmail.com:admin1
####Get all votes of authenticated user 
GET     /votes ***(for USERs only)***

    curl -s http://localhost:8080/voting/votes/ -u user1@gmail.com:password1
####Vote for specific menu (for USERs only)
POST /votes?menusId={menu id} ***(for USERs only)

    curl -s -X POST http://localhost:8080/voting/votes?menuId=100010 -u user1@gmail.com:password1
    
<hr>

##REST API for Users


####Register as Regular User 
POST /profile/register ***(for ANONYMOUS only)***
    
Body: {"name":"[<i>dish name</i>]", "price":[<i>dish price</i>], "menuId":[<i>menu id</i>]}

    curl -s -X POST -d '{"name": "New User", "email":"tEsT@gmail.com","password":"test-password"}' -H 'Content-Type:application/json' http://localhost:8080/voting/profile/register

