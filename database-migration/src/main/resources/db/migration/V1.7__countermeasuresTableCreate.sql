
insert into lookup_type (id, version, name)
values (104, 1, 'countermeasures_status');

insert into lookup (id, version, lookup_type, display_order, name)
VALUES (17, 1, 104, null, 'active'),
       (18, 1, 104, null, 'inactive'),
       (19, 1, 104, null, 'pending');


create table countermeasures
(
    id          integer not null,
    value       varchar(255) not null,
    start_date  timestamp not null,
    end_date    timestamp not null,
    status      integer not null,
    PRIMARY KEY(id),
    constraint status_fkey foreign key (status) references lookup(id)
);