CREATE TABLE state (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE lga (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       state_id BIGINT NOT NULL,
                                       FOREIGN KEY (state_id) REFERENCES state (id)
);
