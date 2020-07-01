--------------------------------------------------------------
-- Filename:  V1.5__indicatorTypes.sql
-- ---------------------------------------------

-- Create the indicator types table

CREATE TABLE indicator_types
(
    id         INTEGER,
    name       VARCHAR(255)
);

INSERT INTO indicator_types(id, name)
    VALUES (5, 'ip'),
           (3, 'domain');

