insert into cbfs_entry_page
  (page_number, category_id, link_list, content)
values
  (1, null, 'testentry-1-171130,testentry-2-171130', '{json.page.1.null}'),
  (2, null, 'testentry-3-171130', '{json.page.2.null}'),
  (1, 1, 'testentry-1-171130,testentry-3-171130', '{json.page.1.1}'),
  (2, 1, '', '{json.page.2.1}');