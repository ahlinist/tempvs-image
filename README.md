# tempvs-image
[![Circle CI](https://circleci.com/gh/ahlinist/tempvs-image/tree/master.svg?&style=shield)](https://circleci.com/gh/ahlinist/tempvs-image/tree/master)

An image microservice for tempvs (see: https://github.com/ahlinist/tempvs). Based on tempvs-rest client (see: https://github.com/ahlinist/tempvs-rest).

## Discovery
To be discovered by tempvs services this component should have the following env variables set correctly:
 * DOMAIN_NAME (domain name specific for this group of instances, defaults to "localhost")
 * EUREKA_URL (Eureka server url, defaults to "http://user:password@localhost:8084")
 * EUREKA_PASSWORD (Eureka server password, defaults to "password")

## Configuration
The following env variables need to be set:
 * PORT (8080 - default)
 * TOKEN (security token that matches the one being set up in the host app)
 * MONGODB_URI (mongodb://\<user\>:\<pass\>@\<host\>:\<port\>/\<db_name\>)
