
<h2>REST API for Restaurant</h2>

<h4>Get all</h4>
GET     /restaurants

<i>curl:</i>

<h4>Get by ID</h4>
GET     /restaurants/{restaurantId}

<i>curl:</i>

<h4>Delete by ID</h4>
DELETE  /restaurants/{restaurantId}

<i>curl:</i>

<h4>Create</h4>
POST  /restaurants

Body: {"name":[<i>restaurant name</i>]}

<i>curl:</i>

<h4>Update</h4>
PUT  /restaurants/{restaurantId}

Body: {"name":[<i>restaurant name</i>]}

<i>curl:</i>

<hr>
<h2>REST API for Menu</h2>


<h4>Get by ID</h4>
GET     /menus/{menuId}

<i>curl:</i>

<h4>Get today's menus</h4>
GET     /menus/today

<h4>Get menus by date</h4>
GET     /menus/?date={ISO date}

<i>curl:</i>

<h4>Delete by ID</h4>
DELETE  /menus/{menuId}

<i>curl:</i>

<h4>Create</h4>
POST  /menus

Body: {"date":"[<i>ISO date</i>"], "restaurantId":[<i>restaurant id</i>]}

<i>curl:</i>

<hr>
<h2>REST API for Restaurant</h2>

<h4>Get all</h4>
GET     /restaurants

<i>curl:</i>

<h4>Get by ID</h4>
GET     /restaurants/{restaurantId}

<i>curl:</i>

<h4>Delete by ID</h4>
DELETE  /restaurants/{restaurantId}

<i>curl:</i>

<h4>Create</h4>
POST  /restaurants

Body: {"name":"[<i>restaurant name</i>]"}

<i>curl:</i>

<h4>Update</h4>
PUT  /restaurants/{restaurantId}

Body: {"name":"[<i>restaurant name</i>]"}

<i>curl:</i>

<hr>
<h2>REST API for Dishes</h2>


<h4>Get by ID</h4>
GET     /dishes/{dishId}

<i>curl:</i>
<h4>Get by Menu ID</h4>
GET     /dishes/?menuid={menu id}

<i>curl:</i>

<h4>Delete by ID</h4>
DELETE  /dishes/{dishId}

<i>curl:</i>

<h4>Create</h4>
POST  /dishes

Body: {"name":"[<i>Dish name</i>]", "price":[<i>price id</i>], "menuId":[<i>menu id</i>]}

<i>curl:</i>

<h4>Update</h4>
PUT  /dishes/{dishId}

Body: {  "name": "[<i>Dish name</i>]", "price":[<i>price id</i>], "menuId":[<i>menu id</i>]}

<i>curl:</i>
