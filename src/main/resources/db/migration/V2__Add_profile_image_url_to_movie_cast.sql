-- Add profile_image_url column to movie_cast table
ALTER TABLE movie_cast ADD COLUMN PROFILE_IMAGE_URL VARCHAR(1024) AFTER ROLE;