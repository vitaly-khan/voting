## Test Data

### Admins and Regular Users
ID | Username | E-Mail | Password | Role
-- | -------- | ------ | -------- | ----
100000 | User 2 | user2@gmail.com | password2 | USER 
100001 | User 1 | user1@gmail.com | password1 | USER 
100002 | Admin 2 | admin2@gmail.com | admin2 | ADMIN 
100003 | Admin 1 | admin1@gmail.com | admin1 | ADMIN 
100004 | User 3 | user3@gmail.com | password3 | USER 


### Restaurants
ID | Name
-- | ---- 
100005 | Korean 
100006 | Japanese
100007 | Georgian
100008 | Italian
_100009_ | _Disabled_
 
### Menus
ID | Date | Restaurant ID
-- | ---- | -------------
100010 | 2020-05-03 | 100005 
100011 | 2020-05-03 | 100006
100012 | 2020-05-03 | 100007
100013 | _today_ | 100005
100014 | _today_ | 100006
100015 | _today_ | 100008

### Dishes
ID | Name | Price | Menu ID
-- | ---- | ----- | -------
100016 | Korean Dish 1 | 20 000 | 100010 
100017 | Korean Dish 3 | 15 000 | 100010
100018 | Korean Dish 2 | 35 000 | 100010
100019 | Japanese Dish 1 | 35 000 | 100011
100020 | Georgian Dish 2 | 13 000 | 100012
100021 | Georgian Dish 1 | 30 000 | 100012
100022 | Korean Dish 1 | 20 000 | 100013
100023 | Korean Dish 5 | 30 000 | 100013
100024 | Korean Dish 4 | 40 000 | 100013
100025 | Japanese Dish 2 | 75 000 | 100014
100026 | Japanese Dish 3 | 55 000 | 100014
100027 | Japanese Dish 5 | 42 000 | 100014
100028 | Japanese Dish 4 | 30 000 | 100014
 
### Votes
ID | Date | Menu ID | User ID
-- | ---- | ----- | -------
100029 | 2020-05-03 | 100010 | 100001 
100030 | 2020-05-03 | 100011 | 100004 
100031 | 2020-05-03 | 100011 | 100000 
100032 | _today_ | 100013 | 100000 
100033 | _today_ | 100013 | 100004 
