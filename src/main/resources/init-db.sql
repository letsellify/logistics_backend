-- CREATE DATABASE logistics_db;
-- CREATE USER letsellify WITH ENCRYPTED PASSWORD 'KfiwSDrL3VPP8s15';
-- GRANT ALL PRIVILEGES ON DATABASE logistics_db TO letsellify;


-- CREATE TABLE homeState (
--                        id BIGSERIAL PRIMARY KEY,
--                        name VARCHAR(255) NOT NULL UNIQUE
-- );
--
-- CREATE TABLE homeLga (
--                      id BIGSERIAL PRIMARY KEY,
--                      name VARCHAR(255) NOT NULL,
--                      state_id BIGINT NOT NULL,
--                      FOREIGN KEY (state_id) REFERENCES homeState (id)
-- );