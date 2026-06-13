CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          iin VARCHAR(12) NOT NULL UNIQUE,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);