create table indicators(
                           id INTEGER PRIMARY KEY NOT NULL,
                           type INTEGER,
                           value varchar
);

INSERT into indicators(id, type, value)
values (100, 2, '1.2.3.4'),
       (101, 2, '2.2.2.5'),
       (102, 3, 'google.com'),
       (103, 7, NULL),
       (104, NULL, 'stuff');

