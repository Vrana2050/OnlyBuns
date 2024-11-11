-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01 21:58:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01 18:57:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('a', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'a@example.com', true, '2017-10-01 18:57:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('b', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'b@example.com', true, '2017-10-01 18:57:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('c', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'c@example.com', true, '2017-10-01 18:57:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('d', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'd@example.com', true, '2017-10-01 18:57:58.508-07',0,0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date,number_of_following,number_of_posts) VALUES ('e', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'e@example.com', true, '2017-10-01 18:57:58.508-07',0,0);





INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_ZEC');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1); -- admin-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (7, 1);