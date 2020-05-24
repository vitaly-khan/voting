## Test Data

### Admins and Regular Users
ID | Username | E-Mail | Password | Role | Comments
-- | -------- | ------ | -------- | ---- | --------
100000 | User 2 | user2@gmail.com | password2 | USER 
100001 | User 1 | user1@gmail.com | password1 | USER | _(hasn't voted today)_ 
100002 | Admin 2 | admin2@gmail.com | admin2 | ADMIN 
100003 | Admin 1 | admin1@gmail.com | admin1 | ADMIN 
100004 | User 3 | user3@gmail.com | password3 | USER 


### Restaurants
ID | Name | Comments
-- | ---- | --------
100005 | Korean 
100006 | Japanese
100007 | Georgian | _(has no today's menu)_
100008 | Italian
100009 | French
100010 | Thai | _(disabled, contains no menu)_
 
### Menus
ID | Date | Restaurant ID | Comments
-- | ---- | ------------- | --------
100011 | 2020-05-03 | 100005 
100012 | 2020-05-03 | 100006
100013 | 2020-05-03 | 100007
100014 | _today_ | 100005
100015 | _today_ | 100006
100016 | _today_ | 100008 | _(contains no dishes)_
100017 | _today_ | 100009 | _(disabled)_

### Dishes
ID | Name | Price | Menu ID
-- | ---- | ----- | -------
100018 | Korean Dish 1 | 20 000 | 100011 
100019 | Korean Dish 3 | 15 000 | 100011
100020 | Korean Dish 2 | 35 000 | 100011
100021 | Japanese Dish 1 | 35 000 | 100012
100022 | Georgian Dish 2 | 13 000 | 100013
100023 | Georgian Dish 1 | 30 000 | 100013
100024 | Korean Dish 1 | 20 000 | 100014
100025 | Korean Dish 5 | 30 000 | 100014
100026 | Korean Dish 4 | 40 000 | 100014
100027 | Japanese Dish 2 | 75 000 | 100015
100028 | Japanese Dish 3 | 55 000 | 100015
100029 | Japanese Dish 5 | 42 000 | 100015
100030 | Japanese Dish 4 | 30 000 | 100015
100031 | Italian Dish 1 | 30 000 | 100017 | _(disabled)_
 
### Votes
ID | Date | Menu ID | User ID
-- | ---- | ----- | -------
100032 | 2020-05-03 | 100011 | 100001 
100033 | 2020-05-03 | 100012 | 100004 
100034 | 2020-05-03 | 100012 | 100000 
100035 | _today_ | 100014 | 100000 
100036 | _today_ | 100014 | 100004 
