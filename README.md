# tempvs-image
[![Circle CI](https://circleci.com/gh/ahlinist/tempvs-image/tree/master.svg?&style=shield)](https://circleci.com/gh/ahlinist/tempvs-image/tree/master)

An image microservice for tempvs (see: https://github.com/ahlinist/tempvs). Based on tempvs-rest client (see: https://github.com/ahlinist/tempvs-rest).
 
## Configuration

The following env variables need to be set:
 * PORT (8080 - default)
 * TOKEN (security token that matches the one being set up in the host app)
 * MONGODB_URI (mongodb://\<user\>:\<pass\>@\<host\>:\<port\>/\<db_name\>)

## Running installations
### Stage
http://stage.image.tempvs.club
### Prod
http://image.tempvs.club
