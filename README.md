api-ws-spring
===========
"api-ws-spring" is an api that provides a set of web services easily implementable. An example of the implementation of this project is available here : https://github.com/florian-bellotti/api-ws-spring-example. 

Web services 
==========
Query String
----------------------
### Web services
3 web services are available:
- `/{item}`: return all the items.
- `/{item}/count`: return the total items number.
- `/{item}/first`: return the first item.

### Parameters
Api provides query string parameters:
- **asc** or **desc** allow you to sort resources by ascending or descending order. The value about this parameter is the name of the field. `ex : GET /products?asc=price`
- **range** allow you to get items in a specific range. `ex : GET /products?range=10-110`
- **fields** allow you to get all needed fields. `ex : GET /products?fields=price`
- **{fieldName}** allow you to add filters. The web service will return items where the fieldName's value is equals to the given value. `ex : GET /products?price=11`

### Response
The header contains:
- **content-range**: is the response range (format: from-to/total). `ex : content-range: 0-100/1054`
- **accept-range**: is the maximum limit of items that can be return. `ex : accept-range: 1000`

The body return a list of items.

### Errors
This is the list of possible errors :

| Status | Code                | Message                                    |
|------  |-------------------- |-------------                               |
|400     |ASC_AND_DESC_EXIST   | Asc and desc can't exist in the same query |
|400     |ASC_OR_DESC_REQUIRED | Asc or desc is required                    |
|400     |INVALID_RANGE_FORMAT | Range format is invalid                    |
|400     |INVALID_RANGE        | Range is invalid                           |
|500     |EXECUTION_ERROR      | Failed during the request execution        |

CRUD
----------------------
In development ....
