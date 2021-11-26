CREATE DATABASE toyproject;
CREATE USER 'team7-server'@'localhost' IDENTIFIED BY 'velog';
GRANT ALL PRIVILEGES ON seminar.* TO 'team7-server'@'localhost';