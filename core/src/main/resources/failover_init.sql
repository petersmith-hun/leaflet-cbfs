create table cbfs_entry_page (
  page_number int(11) not null,
  category_id int(11) not null,
  link_list text,
  constraint page_pk primary key(page_number, category_id)
);

create table cbfs_entry (
  link varchar(255) not null primary key,
  content text
);

create table cbfs_document (
  link varchar(255) not null primary key,
  content text
);