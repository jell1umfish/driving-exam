CREATE TABLE exam_reservations (
                                   id BIGSERIAL PRIMARY KEY,
                                   student_id BIGINT NOT NULL REFERENCES students(id),
                                   exam_type VARCHAR(50) NOT NULL,
                                   exam_date_time TIMESTAMP NOT NULL,
                                   status VARCHAR(50) NOT NULL,
                                   created_at TIMESTAMP,
                                   updated_at TIMESTAMP
);