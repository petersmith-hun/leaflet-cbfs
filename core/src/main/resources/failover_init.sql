create table cbfs_entry_page (
  page_number int(11) not null,
  category_id int(11) default null,
  link_list text,
  content text
);

create table cbfs_entry (
  link varchar(255) not null primary key,
  content text
);

create table cbfs_document (
  link varchar(255) not null primary key,
  content text
);

create table cbfs_category (
  id int(11) not null primary key,
  title varchar(255)
)