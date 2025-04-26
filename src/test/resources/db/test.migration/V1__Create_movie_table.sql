-- Drop existing movie table and recreate with new structure
DROP TABLE IF EXISTS movie;

-- Create Movie table with new structure
CREATE TABLE movie (
    ID UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    NAME VARCHAR(255),
    DURATION_MINUTES INT,
    DESCRIPTION TEXT,
    IMAGE_URL VARCHAR(1024),
    TRAILER_URL VARCHAR(1024),
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED_AT TIMESTAMP
);

-- Create MovieLanguage table
CREATE TABLE movie_language (
    ID UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    MOVIE_ID UUID,
    NAME VARCHAR(100),
    DESCRIPTION TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED_AT TIMESTAMP,
    FOREIGN KEY (MOVIE_ID) REFERENCES movie(ID)
);

-- Create MovieFormat table
CREATE TABLE movie_format (
    ID UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    MOVIE_ID UUID,
    NAME VARCHAR(100),
    DESCRIPTION TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED_AT TIMESTAMP,
    FOREIGN KEY (MOVIE_ID) REFERENCES movie(ID)
);

-- Create MovieGenre table
CREATE TABLE movie_genre (
    ID UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    MOVIE_ID UUID,
    NAME VARCHAR(100),
    DESCRIPTION TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED_AT TIMESTAMP,
    FOREIGN KEY (MOVIE_ID) REFERENCES movie(ID)
);

-- Create MovieCast table
CREATE TABLE movie_cast (
    ID UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    MOVIE_ID UUID,
    ACTOR_NAME VARCHAR(255),
    CHARACTER_NAME VARCHAR(255),
    ROLE VARCHAR(100),
    DESCRIPTION TEXT,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DELETED_AT TIMESTAMP,
    FOREIGN KEY (MOVIE_ID) REFERENCES movie(ID)
);

-- Add indexes for better performance
CREATE INDEX idx_movie_name ON movie(NAME);
CREATE INDEX idx_movielanguage_movie_id ON movie_language(MOVIE_ID);
CREATE INDEX idx_movieformat_movie_id ON movie_format(MOVIE_ID);
CREATE INDEX idx_moviegenre_movie_id ON movie_genre(MOVIE_ID);
CREATE INDEX idx_moviecast_movie_id ON movie_cast(MOVIE_ID);

-- Create triggers to update UPDATED_AT columns
CREATE TRIGGER movie_update_trigger
BEFORE UPDATE ON movie
FOR EACH ROW
CALL "io.demo.MovieCatalog.trigger.UpdateTimestampTrigger";

CREATE TRIGGER movie_language_update_trigger
BEFORE UPDATE ON movie_language
FOR EACH ROW
CALL "io.demo.MovieCatalog.trigger.UpdateTimestampTrigger";

CREATE TRIGGER movie_format_update_trigger
BEFORE UPDATE ON movie_format
FOR EACH ROW
CALL "io.demo.MovieCatalog.trigger.UpdateTimestampTrigger";

CREATE TRIGGER movie_genre_update_trigger
BEFORE UPDATE ON movie_genre
FOR EACH ROW
CALL "io.demo.MovieCatalog.trigger.UpdateTimestampTrigger";

CREATE TRIGGER movie_cast_update_trigger
BEFORE UPDATE ON movie_cast
FOR EACH ROW
CALL "io.demo.MovieCatalog.trigger.UpdateTimestampTrigger";
