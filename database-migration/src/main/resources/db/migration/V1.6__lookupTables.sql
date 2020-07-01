--------------------------------------------------------------
-- Filename:  V1.6__lookupTables.sql
--------------------------------------------------------------

-- Create this table:  LookupType
create table lookup_type
(
    id         integer      not null,
    version    integer      not null,
    name       varchar(256) not null,
    primary key (id),
    constraint lookup_type_name_uniq UNIQUE(name) -- Each lookup type name must be unique
);
comment on table  lookup_type      is 'This lookup_type table holds all of the lookup type names.  Every lookup must have a type';
comment on column lookup_type.name is 'Lookup_type.name holds the name or category of this lookup -- e.g., priority.';



-- Create this table:  Lookup
create table lookup
(
    id           integer      not null,
    version      integer      not null,
    lookup_type  integer      not null,
    display_order integer      null,
    name         varchar(256) not null,
    primary key(id),
    constraint lookup_name_uniq UNIQUE(lookup_type, name),                           -- Each lookup name and type must be unique
    constraint lookup_type_fkey FOREIGN KEY(lookup_type) references lookup_type(id)  -- Each lookup type must exist in the lookup_type table
);
comment on table  lookup         is 'The lookup table holds all of the lookup values';
comment on column lookup.name    is 'Lookup.name holds the actual lookup name -- low, medium, high';
comment on column lookup.display_order is 'A possible order to display the lookups on the front-end';


-- Insert Starting Lookup Types
insert into lookup_type(id, version, name) values(100, 1, 'priority');
insert into lookup_type(id, version, name) values(101, 1, 'report_type');


-- Insert Starting Lookup Values for priority
insert into lookup(id, version, lookup_type, display_order, name)  values(1, 1,  100, 1, 'low');
insert into lookup(id, version, lookup_type, display_order, name)  values(2, 1,  100, 2, 'medium');
insert into lookup(id, version, lookup_type, display_order, name)  values(3, 1,  100, 3, 'high');
insert into lookup(id, version, lookup_type, display_order, name)  values(4, 1,  100, 4, 'critical');


-- Insert Starting Lookup Values for report_type
insert into lookup(id, version, lookup_type, name)  values(5, 1, 101, 'Marketing');
insert into lookup(id, version, lookup_type, name)  values(6, 1, 101, 'H&R');
insert into lookup(id, version, lookup_type, name)  values(7, 1, 101, 'CEO');