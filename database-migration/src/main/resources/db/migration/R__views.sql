--Repeatable SQL views

Drop view if exists view_all_reports;

create view view_all_reports as
    select r.id, r.display_name, r.description, l.name as priority, created_date, l.name as reference_source
    from reports r
    left join lookup l on r.priority = l.id
    left join lookup l2 on r.reference_source = l2.id;
