CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL NOT NULL PRIMARY KEY, 
    name VARCHAR(8) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL NOT NULL PRIMARY KEY, 
    group_id BIGINT REFERENCES groups (id), 
    last_name VARCHAR(35) NOT NULL, 
    first_name VARCHAR(35) NOT NULL, 
    CONSTRAINT student_flname UNIQUE (last_name, first_name)
);

CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL NOT NULL PRIMARY KEY, 
    name VARCHAR(50) NOT NULL UNIQUE, 
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student_course (
    student_id BIGINT NOT NULL REFERENCES students (id),
    course_id BIGINT NOT NULL REFERENCES courses (id),
    PRIMARY KEY (student_id, course_id)
);
