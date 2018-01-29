create table cbfs_status_tracking (
  id int(11) auto_increment not null primary key,
  created_at datetime not null,
  status varchar(20) not null,
  parameter varchar(20) default null
);

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