CREATE DATABASE toy_project_test;
CREATE USER 'team7-test'@'localhost' IDENTIFIED BY 'velog';
GRANT ALL PRIVILEGES ON toy_project_test.* TO 'team7-test'@'localhost';
