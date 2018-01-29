insert into cbfs_status_tracking
  (id, created_at, status, parameter)
values
  (1, '2018-01-29 20:00:00', 'SERVING', null),
  (2, '2018-01-29 20:05:00', 'STANDBY', null),
  (3, '2018-01-29 22:00:00', 'MIRRORING', null),
  (4, '2018-01-29 22:00:02', 'MIRRORING_FAILURE', 'DOCUMENT');
