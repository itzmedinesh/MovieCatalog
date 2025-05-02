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

Optional query parameters:
- `page`: Page number (default: 0)
- `size`: Number of items per page (default: 10)
- `sort`: Field to sort by (default: name)
- `direction`: Sort direction, either "asc" or "desc" (default: asc)

Examples:
```
GET /api/movies?page=0&size=20&sort=name&direction=asc
GET /api/movies?sort=durationMinutes&direction=desc
```

Response:
```json
{
   "_embedded": {
      "movies": [
         {
            "id": "62074153-0856-4905-8071-e24d725846c7",
            "name": "Inception",
            "durationMinutes": 148,
            "description": "A mind-bending thriller about dream invasion.",
            "imageUrl": "https://example.com/images/inception.jpg",
            "trailerUrl": "https://example.com/trailers/inception.mp4",
            "createdAt": "2025-04-25T20:26:45",
            "updatedAt": "2025-04-25T20:26:45",
            "languages": [
               {
                  "id": "24afa3ff-a485-438f-92a3-4d2acfcd5445",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "French",
                  "description": "Dubbed version",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-languages/24afa3ff-a485-438f-92a3-4d2acfcd5445"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-languages"
                     }
                  }
               },
               {
                  "id": "b3d0f694-48f1-4ca1-8b7e-807a8f55869a",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "English",
                  "description": "Original language",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-languages/b3d0f694-48f1-4ca1-8b7e-807a8f55869a"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-languages"
                     }
                  }
               }
            ],
            "formats": [
               {
                  "id": "cf865b40-5e68-4298-b860-fdb9392f530f",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "3D",
                  "description": "Three-dimensional format",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-formats/cf865b40-5e68-4298-b860-fdb9392f530f"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-formats"
                     }
                  }
               },
               {
                  "id": "e2761cc5-04dd-44a8-b863-5fd0d7c33314",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "IMAX",
                  "description": "High-resolution format",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-formats/e2761cc5-04dd-44a8-b863-5fd0d7c33314"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-formats"
                     }
                  }
               }
            ],
            "genres": [
               {
                  "id": "17e73342-29d5-4867-b289-1be31801c897",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "Thriller",
                  "description": "Exciting and suspenseful",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-genres/17e73342-29d5-4867-b289-1be31801c897"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-genres"
                     }
                  }
               },
               {
                  "id": "1a893e34-fd03-426b-b324-11134d11a2c7",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "name": "Science Fiction",
                  "description": "Futuristic and imaginative themes",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45",
                  "_links": {
                     "self": {
                        "href": "http://localhost:8080/api/movie-genres/1a893e34-fd03-426b-b324-11134d11a2c7"
                     },
                     "collection": {
                        "href": "http://localhost:8080/api/movie-genres"
                     }
                  }
               }
            ],
            "cast": [
               {
                  "id": "a1844741-4100-4130-a6cb-b24d9d2b4164",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "actorName": "Leonardo DiCaprio",
                  "characterName": "Dom Cobb",
                  "role": "Protagonist",
                  "profileImageUrl": "https://example.com/images/leonardo.jpg",
                  "description": "A skilled thief who steals secrets from within dreams.",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45"
               },
               {
                  "id": "a2b223f7-11c6-4309-941c-a961a8199256",
                  "movieId": "62074153-0856-4905-8071-e24d725846c7",
                  "actorName": "Joseph Gordon-Levitt",
                  "characterName": "Arthur",
                  "role": "Point Man",
                  "profileImageUrl": "https://example.com/images/joseph.jpg",
                  "description": "Cobb's partner and organizer of the missions.",
                  "createdAt": "2025-04-25T20:26:45",
                  "updatedAt": "2025-04-25T20:26:45"
               }
            ],
            "_links": {
               "self": {
                  "href": "http://localhost:8080/api/movies/62074153-0856-4905-8071-e24d725846c7"
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
   "id": "62074153-0856-4905-8071-e24d725846c7",
   "name": "Inception",
   "durationMinutes": 148,
   "description": "A mind-bending thriller about dream invasion.",
   "imageUrl": "https://example.com/images/inception.jpg",
   "trailerUrl": "https://example.com/trailers/inception.mp4",
   "createdAt": "2025-04-25T20:26:45",
   "updatedAt": "2025-04-25T20:26:45",
   "languages": [
      {
         "id": "24afa3ff-a485-438f-92a3-4d2acfcd5445",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "French",
         "description": "Dubbed version",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/24afa3ff-a485-438f-92a3-4d2acfcd5445"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      },
      {
         "id": "b3d0f694-48f1-4ca1-8b7e-807a8f55869a",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "English",
         "description": "Original language",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/b3d0f694-48f1-4ca1-8b7e-807a8f55869a"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      }
   ],
   "formats": [
      {
         "id": "cf865b40-5e68-4298-b860-fdb9392f530f",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "3D",
         "description": "Three-dimensional format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/cf865b40-5e68-4298-b860-fdb9392f530f"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      },
      {
         "id": "e2761cc5-04dd-44a8-b863-5fd0d7c33314",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "IMAX",
         "description": "High-resolution format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/e2761cc5-04dd-44a8-b863-5fd0d7c33314"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      }
   ],
   "genres": [
      {
         "id": "17e73342-29d5-4867-b289-1be31801c897",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Thriller",
         "description": "Exciting and suspenseful",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/17e73342-29d5-4867-b289-1be31801c897"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      },
      {
         "id": "1a893e34-fd03-426b-b324-11134d11a2c7",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Science Fiction",
         "description": "Futuristic and imaginative themes",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/1a893e34-fd03-426b-b324-11134d11a2c7"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      }
   ],
   "cast": [
      {
         "id": "a1844741-4100-4130-a6cb-b24d9d2b4164",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Leonardo DiCaprio",
         "characterName": "Dom Cobb",
         "role": "Protagonist",
         "profileImageUrl": "https://example.com/images/leonardo.jpg",
         "description": "A skilled thief who steals secrets from within dreams.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      },
      {
         "id": "a2b223f7-11c6-4309-941c-a961a8199256",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Joseph Gordon-Levitt",
         "characterName": "Arthur",
         "role": "Point Man",
         "profileImageUrl": "https://example.com/images/joseph.jpg",
         "description": "Cobb's partner and organizer of the missions.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      }
   ],
   "_links": {
      "self": {
         "href": "http://localhost:8080/api/movies/62074153-0856-4905-8071-e24d725846c7"
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
   "description": "A mind-bending thriller about dream invasion.",
   "imageUrl": "https://example.com/images/inception.jpg",
   "trailerUrl": "https://example.com/trailers/inception.mp4",
   "languages": [
      {
         "name": "English",
         "description": "Original language"
      },
      {
         "name": "French",
         "description": "Dubbed version"
      }
   ],
   "formats": [
      {
         "name": "IMAX",
         "description": "High-resolution format"
      },
      {
         "name": "3D",
         "description": "Three-dimensional format"
      }
   ],
   "genres": [
      {
         "name": "Science Fiction",
         "description": "Futuristic and imaginative themes"
      },
      {
         "name": "Thriller",
         "description": "Exciting and suspenseful"
      }
   ],
   "cast": [
      {
         "actorName": "Leonardo DiCaprio",
         "characterName": "Dom Cobb",
         "role": "Protagonist",
         "profileImageUrl": "https://example.com/images/leonardo.jpg",
         "description": "A skilled thief who steals secrets from within dreams."
      },
      {
         "actorName": "Joseph Gordon-Levitt",
         "characterName": "Arthur",
         "role": "Point Man",
         "profileImageUrl": "https://example.com/images/joseph.jpg",
         "description": "Cobb's partner and organizer of the missions."
      }
   ]
}
```

Response:
```json
{
   "id": "62074153-0856-4905-8071-e24d725846c7",
   "name": "Inception",
   "durationMinutes": 148,
   "description": "A mind-bending thriller about dream invasion.",
   "imageUrl": "https://example.com/images/inception.jpg",
   "trailerUrl": "https://example.com/trailers/inception.mp4",
   "createdAt": "2025-04-25T20:26:45",
   "updatedAt": "2025-04-25T20:26:45",
   "languages": [
      {
         "id": "24afa3ff-a485-438f-92a3-4d2acfcd5445",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "French",
         "description": "Dubbed version",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/24afa3ff-a485-438f-92a3-4d2acfcd5445"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      },
      {
         "id": "b3d0f694-48f1-4ca1-8b7e-807a8f55869a",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "English",
         "description": "Original language",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/b3d0f694-48f1-4ca1-8b7e-807a8f55869a"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      }
   ],
   "formats": [
      {
         "id": "cf865b40-5e68-4298-b860-fdb9392f530f",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "3D",
         "description": "Three-dimensional format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/cf865b40-5e68-4298-b860-fdb9392f530f"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      },
      {
         "id": "e2761cc5-04dd-44a8-b863-5fd0d7c33314",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "IMAX",
         "description": "High-resolution format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/e2761cc5-04dd-44a8-b863-5fd0d7c33314"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      }
   ],
   "genres": [
      {
         "id": "17e73342-29d5-4867-b289-1be31801c897",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Thriller",
         "description": "Exciting and suspenseful",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/17e73342-29d5-4867-b289-1be31801c897"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      },
      {
         "id": "1a893e34-fd03-426b-b324-11134d11a2c7",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Science Fiction",
         "description": "Futuristic and imaginative themes",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/1a893e34-fd03-426b-b324-11134d11a2c7"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      }
   ],
   "cast": [
      {
         "id": "a1844741-4100-4130-a6cb-b24d9d2b4164",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Leonardo DiCaprio",
         "characterName": "Dom Cobb",
         "role": "Protagonist",
         "profileImageUrl": "https://example.com/images/leonardo.jpg",
         "description": "A skilled thief who steals secrets from within dreams.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      },
      {
         "id": "a2b223f7-11c6-4309-941c-a961a8199256",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Joseph Gordon-Levitt",
         "characterName": "Arthur",
         "role": "Point Man",
         "profileImageUrl": "https://example.com/images/joseph.jpg",
         "description": "Cobb's partner and organizer of the missions.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      }
   ],
   "_links": {
      "self": {
         "href": "http://localhost:8080/api/movies/62074153-0856-4905-8071-e24d725846c7"
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
   "name": "Inception",
   "durationMinutes": 148,
   "description": "A truly mind-bending thriller about dream invasion.",
   "imageUrl": "https://example.com/images/inception.jpg",
   "trailerUrl": "https://example.com/trailers/inception.mp4"
}
```

Response:
```json
{
   "id": "62074153-0856-4905-8071-e24d725846c7",
   "name": "Inception",
   "durationMinutes": 148,
   "description": "A truly mind-bending thriller about dream invasion.",
   "imageUrl": "https://example.com/images/inception.jpg",
   "trailerUrl": "https://example.com/trailers/inception.mp4",
   "createdAt": "2025-04-25T20:26:45",
   "updatedAt": "2025-04-26T02:02:13.797548",
   "languages": [
      {
         "id": "24afa3ff-a485-438f-92a3-4d2acfcd5445",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "French",
         "description": "Dubbed version",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/24afa3ff-a485-438f-92a3-4d2acfcd5445"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      },
      {
         "id": "b3d0f694-48f1-4ca1-8b7e-807a8f55869a",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "English",
         "description": "Original language",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-languages/b3d0f694-48f1-4ca1-8b7e-807a8f55869a"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-languages"
            }
         }
      }
   ],
   "formats": [
      {
         "id": "cf865b40-5e68-4298-b860-fdb9392f530f",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "3D",
         "description": "Three-dimensional format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/cf865b40-5e68-4298-b860-fdb9392f530f"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      },
      {
         "id": "e2761cc5-04dd-44a8-b863-5fd0d7c33314",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "IMAX",
         "description": "High-resolution format",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-formats/e2761cc5-04dd-44a8-b863-5fd0d7c33314"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-formats"
            }
         }
      }
   ],
   "genres": [
      {
         "id": "17e73342-29d5-4867-b289-1be31801c897",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Thriller",
         "description": "Exciting and suspenseful",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/17e73342-29d5-4867-b289-1be31801c897"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      },
      {
         "id": "1a893e34-fd03-426b-b324-11134d11a2c7",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "name": "Science Fiction",
         "description": "Futuristic and imaginative themes",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45",
         "_links": {
            "self": {
               "href": "http://localhost:8080/api/movie-genres/1a893e34-fd03-426b-b324-11134d11a2c7"
            },
            "collection": {
               "href": "http://localhost:8080/api/movie-genres"
            }
         }
      }
   ],
   "cast": [
      {
         "id": "a1844741-4100-4130-a6cb-b24d9d2b4164",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Leonardo DiCaprio",
         "characterName": "Dom Cobb",
         "role": "Protagonist",
         "profileImageUrl": "https://example.com/images/leonardo.jpg",
         "description": "A skilled thief who steals secrets from within dreams.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      },
      {
         "id": "a2b223f7-11c6-4309-941c-a961a8199256",
         "movieId": "62074153-0856-4905-8071-e24d725846c7",
         "actorName": "Joseph Gordon-Levitt",
         "characterName": "Arthur",
         "role": "Point Man",
         "profileImageUrl": "https://example.com/images/joseph.jpg",
         "description": "Cobb's partner and organizer of the missions.",
         "createdAt": "2025-04-25T20:26:45",
         "updatedAt": "2025-04-25T20:26:45"
      }
   ],
   "_links": {
      "self": {
         "href": "http://localhost:8080/api/movies/62074153-0856-4905-8071-e24d725846c7"
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

## Build
Trigger Build 2
