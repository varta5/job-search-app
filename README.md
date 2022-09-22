# Job search application

Job search backend API using Spring Boot, Gradle and JSON

This API enables clients to create and search job positions. Request and response bodies need to contain data in JSON format

## Available endpoints

### POST /client

Querystring parameters are not used

Request body parameters:

- `name` - string, the name of the client to be saved, should not exceed 100 characters,
- `emailAddress` - string, the e-mail address of the client to be saved, should be a valid e-mail address as per RFC 5322

Response body parameters:

- `apiKey` - string, a UUID generated randomly, this should be saved by the client for identification of upcoming requests

Example request body:

    {
        "name": "Example Client Name",
        "emailAddress": "example@client.com"
    }

Example response body:

    {
        "apiKey": "2696d582-0532-4796-b6f0-45a60cd55892"
    }
