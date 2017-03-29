# java-auth
Standard auth API created with maven as a school project

## Configuration settings:
* `AUTH_PORT`
    * integer
    * default: 8000
    * Port which the application listens to
* `AUTH_SECRET`
    * string
    * default: macilaci
    * Secret of the JWT token encryption
* `AUTH_FILE_NAME`
    * string
    * default: ./users.json
    * Path and filename to the file storing
* `AUTH_FILE_STORE`
    * boolean
    * default: true
    * If true application will try to store users in
    in the given json file
* `AUTH_LOG_LEVEL`
    * string
    * default: debug
    * If set to debug, debug logs will be printed to stdout
    if not, only errors and infos will be printed


## Endpoints:
### POST /registration
Registrates a user

Needs a request body of json with the following properties:
* `email` - Email for the user. Have to be unique.
* `name` - Name of the user. Optional.
* `password` - Password of the user.

### POST /login
Logs the user in, sends back JWT token in
`Authentication` header.

Needs a request body of json with the following properties:
* `email` - Email for the user.
* `password` - Password of the user.

### GET /me
Needs authentication header received at login.
Gives back the information stored about the user.


## Server gives back consistent responses from the following:
```
{
    "success": true
}
```
If operation was successful but there is no data to return

```
{
    "error": "Message",
    "success": false
}
```
If error happened during the request

```
{
    "data": {},
    "success": true
}
```
If request was successful and there is data in the response