# Movie Catalog API

This is a RESTful API with HATEOAS for managing a movie catalog.

## Technologies Used

- Spring Boot 3.4.4
- Spring Data JPA
- Spring HATEOAS
- MySQL (Production)
- H2 (Testing)
- Flyway for database migrations
- JUnit 5 for testing

## API Endpoints

### Get All Movies

```
GET /api/movies
```

Response:
```json
{
  "_embedded": {
    "movies": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "name": "Inception",
        "durationMinutes": 148,
        "description": "A thief who steals corporate secrets through the use of dream-sharing technology.",
        "createdAt": "2023-06-15T10:30:00",
        "updatedAt": "2023-06-15T10:30:00",
        "languages": ["English", "French"],
        "formats": ["IMAX", "Digital"],
        "genres": ["Sci-Fi", "Action"],
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/movies/123e4567-e89b-12d3-a456-426614174000"
          },
          "collection": {
            "href": "http://localhost:8080/api/movies"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/movies"
    }
  }
}
```

### Get Movie by ID

```
GET /api/movies/{id}
```

Response:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Inception",
  "durationMinutes": 148,
  "description": "A thief who steals corporate secrets through the use of dream-sharing technology.",
  "createdAt": "2023-06-15T10:30:00",
  "updatedAt": "2023-06-15T10:30:00",
  "languages": ["English", "French"],
  "formats": ["IMAX", "Digital"],
  "genres": ["Sci-Fi", "Action"],
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/movies/123e4567-e89b-12d3-a456-426614174000"
    },
    "collection": {
      "href": "http://localhost:8080/api/movies"
    }
  }
}
```

### Create Movie

```
POST /api/movies
```

Request Body:
```json
{
  "name": "Inception",
  "durationMinutes": 148,
  "description": "A thief who steals corporate secrets through the use of dream-sharing technology.",
  "languages": ["English", "French"],
  "formats": ["IMAX", "Digital"],
  "genres": ["Sci-Fi", "Action"]
}
```

Response:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Inception",
  "durationMinutes": 148,
  "description": "A thief who steals corporate secrets through the use of dream-sharing technology.",
  "createdAt": "2023-06-15T10:30:00",
  "updatedAt": "2023-06-15T10:30:00",
  "languages": ["English", "French"],
  "formats": ["IMAX", "Digital"],
  "genres": ["Sci-Fi", "Action"],
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/movies/123e4567-e89b-12d3-a456-426614174000"
    },
    "collection": {
      "href": "http://localhost:8080/api/movies"
    }
  }
}
```

### Update Movie

```
PUT /api/movies/{id}
```

Request Body:
```json
{
  "name": "Inception Updated",
  "durationMinutes": 150,
  "description": "Updated description",
  "languages": ["English", "French", "Spanish"],
  "formats": ["IMAX", "Digital", "3D"],
  "genres": ["Sci-Fi", "Action", "Thriller"]
}
```

Response:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Inception Updated",
  "durationMinutes": 150,
  "description": "Updated description",
  "createdAt": "2023-06-15T10:30:00",
  "updatedAt": "2023-06-15T11:45:00",
  "languages": ["English", "French", "Spanish"],
  "formats": ["IMAX", "Digital", "3D"],
  "genres": ["Sci-Fi", "Action", "Thriller"],
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/movies/123e4567-e89b-12d3-a456-426614174000"
    },
    "collection": {
      "href": "http://localhost:8080/api/movies"
    }
  }
}
```

### Delete Movie

```
DELETE /api/movies/{id}
```

Response: 204 No Content

## Running the Application

### Prerequisites

- Java 17
- Maven
- MySQL

### Steps

1. Clone the repository
2. Configure MySQL database in `application.properties`
3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

## Running Tests

```
./mvnw test
```

## HATEOAS Implementation

This API implements HATEOAS (Hypermedia as the Engine of Application State) principles:

1. Each resource representation includes links to related resources
2. The API provides a way to navigate the API without prior knowledge of the URI structure
3. Clients can use these links to discover available actions

Example of HATEOAS links in responses:
- `self`: Link to the resource itself
- `collection`: Link to the collection containing the resource