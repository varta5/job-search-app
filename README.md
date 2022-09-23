# Job search application

Job search backend API using Spring Boot, Gradle and JSON

This API enables clients to create and search job positions. Request and response bodies need to contain data in JSON format

## Available endpoints

### POST /client

Register new Client on the API server

Request body parameters:

- `name` - string, the name of the client to be saved, should not exceed 100 characters,
- `emailAddress` - string, the e-mail address of the client to be saved, should be a valid e-mail address as per RFC 5322

Response body parameters:

- `apiKey` - string, a <abbr title="Universal Unique Identifier">UUID</abbr> generated randomly, this should be saved by the client for identification of upcoming requests

Example request body:

    {
        "name": "Example Client Name",
        "emailAddress": "example@client.com"
    }

Example response body:

    {
        "apiKey": "2696d582-0532-4796-b6f0-45a60cd55892"
    }

### POST /position

Create new job / Position on the API server

Header parameters:

- `Authorization` - string, the API key / ID which is created at Client registration (POST /client endpoint), in <abbr title="Universal Unique Identifier">UUID</abbr> format, hexadecimal digits between 'a' and 'f' are accepted both as lowercase and as uppercase letters

Request body parameters:

- `name` - string, the name of the position to be saved, should not exceed 50 characters,
- `location` - string, the geographic location of the job position to be saved, should not exceed 50 characters

Response body parameters:

- `positionUrl` - string, the URL under which the position will be available with GET requests

Example request body:

    {
        "name": "Example Position Name",
        "location": "London, United Kingdom"
    }

Example response body:

    {
        "apiKey": "https://github.com/varta5/job-search-app/api/v1/position/9"
    }

### GET /position/{id}

Read a specific job position from the API server

Path variables:

- `id` - integer, the ID of the position

Header parameters:

- `Authorization` - string, the API key / ID which is created at Client registration (POST /client endpoint), in <abbr title="Universal Unique Identifier">UUID</abbr> format, hexadecimal digits between 'a' and 'f' are accepted both as lowercase and as uppercase letters

Response body parameters:

- `id` - integer, the ID of the position
- `nameOfPosition` - string, the name of the position
- `location` - string, the geographical location of the position
- `nameOfClientPostingTheJob` - string, the name of the client / company posting the job position

Example response body:

    {
        "id": 2,
        "nameOfPosition": "Example Position Name",
        "location": "London, United Kingdom",
        "nameOfClientPostingTheJob": "Example Client Name"
    }
