alter table student
add constraint age_check check (age >= 16);

alter table student
add constraint name_not_null check (name IS NOT NULL),
add constraint name_unique UNIQUE (name);

alter table faculty
add constraint color_name_unique UNIQUE (name, color);

alter table student
alter column age set default 20;