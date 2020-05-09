INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (1, now(), 'USER', 'testuser@email.com', '$2y$10$h6yG9iVB4YxLFtChB1E2zuGBZMDxvhBWuwuMeSeEjJ22cOHrMXDUe', 'Test', 'User', 'testuser');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (2, now(), 'USER', 'testuser2@email.com', '$2y$10$h6yG9iVB4YxLFtChB1E2zuGBZMDxvhBWuwuMeSeEjJ22cOHrMXDUe', 'Test', 'User2', 'testuser2');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (3, now(), 'USER', 'testuser3@email.com', '$2y$10$h6yG9iVB4YxLFtChB1E2zuGBZMDxvhBWuwuMeSeEjJ22cOHrMXDUe', 'Test', 'User3', 'testuser3');

INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'TERM_AND_CONDITIONS', now());
INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'PRIVACY_POLICY', now());
