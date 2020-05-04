
<h2>REST API for Menu</h2>

<h4>Get all</h4>
GET     /menus

<i>curl:</i>

<h4>Get by ID</h4>
GET     /menus/{menuid}

<h4>Get today's menus</h4>
GET     /menus/today

<h4>Get menus by date</h4>
GET     /menus/filter?date={ISO date}

<h4>Delete all</h4>
DELETE  /menus

<i>curl:</i>

<h4>Delete by ID</h4>
DELETE  /menus/{menuId}

<i>curl:</i>

<h4>Create</h4>
POST  /menus/restaurant/{restaurauntId}

Body: {"date":"{ISO date}"}

<i>curl:</i>

