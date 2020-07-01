--------------------------------------------------------------
-- Filename:  V1.4__reportsAudTable.sql
--------------------------------------------------------------

-- Create the reports_aud table

CREATE TABLE reports_aud
(
    id                  INTEGER,
    version             INTEGER,
    description         TEXT,
    display_name        VARCHAR(255),
    reviewed            BOOLEAN,
    reference_source    INTEGER,
    priority            INTEGER,
    created_date        TIMESTAMP,
    last_modified_date  TIMESTAMP,
    is_custom_report    BOOLEAN DEFAULT FALSE,
    reserved            BOOLEAN,
    reserved_by         VARCHAR(255),
    rev                 INTEGER,
    rev_type            INTEGER,
    username            varchar(100),
    timestamp           TIMESTAMP
);