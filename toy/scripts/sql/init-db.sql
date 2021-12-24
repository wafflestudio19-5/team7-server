CREATE DATABASE toyproject;
CREATE USER 'team7-server'@'localhost' IDENTIFIED BY 'velog';
GRANT ALL PRIVILEGES ON toyproject.* TO 'team7-server'@'localhost';