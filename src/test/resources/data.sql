-- 密碼的值為 password
INSERT INTO member (member_id, name, email, password, role, created_at, updated_at)
VALUES
    ('a5ff12fd-f83c-4243-8445-7b59f8dcf8ed','AdminAccount','admin@gmail.com','$2a$10$x5UYNqh6Tz76Ay0UdRk2uuD20hc367bmmIWFLrSjtPs5qa1c2AcKy','admin','2026-04-22 10:40:56.196950','2026-04-22 10:40:56.196950'),
    ('b6aa23fd-f83c-4243-8445-7b59f8dcf8ed','UserAccount','user@gmail.com','$2a$10$x5UYNqh6Tz76Ay0UdRk2uuD20hc367bmmIWFLrSjtPs5qa1c2AcKy','user','2026-04-22 10:40:56.196950','2026-04-22 10:40:56.196950');


INSERT INTO enterprise (enterprise_id, name) VALUES ('fdda207b-e2be-4384-aa1c-6e2bcf1e5a16','好愛我企業');