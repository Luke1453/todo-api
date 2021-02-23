# TODO-API

## API Methods:

    GET /api/v1/users  
Returns JSON containing array of user objects 

---
    GET /api/v1/users/{userID}
Returns JSON containing user object with matching userID

---
    GET /api/v1/loginUser
Requires 2 parameters: email and password  
Returns JSON containing user object with matching email and password

---
    POST /api/v1/registerUser
Requires JSON request body with user email and password  
Returns JSON user object with user email, password and ID  

Valid request body example:


```
{
    "email": "email@email.com",
    "password": "password"
}
```

---
    PUT api/v1/changeUserPassword/{userID}
Requires JSON request body with new email and/or password  
Returns JSON user object with user email, password and ID

Valid request body example:


```
{
    "email": "email@email.com",
    "password": "password"
}
```

---
    DELETE /api/v1/deleteUser/{userID}
Returns JSON with confirmation

---
    GET /api/v1/todos
Returns JSON containing array of todo objects

--- 
    GET /api/v1/users/{userID}/todos
Returns JSON containing array of todo objects belonging to user with given userID

---
    GET /api/v1/users/{userID}/todos/{todoID}
Returns JSON containing todo object with given todoID and belonging to user with usedID

---
    POST /api/v1/createTodo
Requires request body with valid todo JSON  
Returns JSON todo object

Valid request body example:

```
{
    "title": "Todo title",
    "body": "Todo body",
    "addedDate": "test addedDate",
    "deadlineDate": "test deadlineDate",
    "userID": 10
}
```
---
    PUT api/v1/users/{userID}/updateTodo/{todoID}
Requires valid JSON request body  
Returns same JSON

Valid request body example:

```
{
    "title": "Todo title",
    "body": "Todo body",
    "addedDate": "test addedDate",
    "deadlineDate": "test deadlineDate",
    "userID": 10
}
```
---
    DELETE /api/v1/users/{userID}/deleteTodo/{todoID}
Returns JSON with confirmation
