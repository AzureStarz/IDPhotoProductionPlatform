drop database if exists photoplatform;
create database photoplatform;
use photoplatform;

create table photo
(
    `photoID`   int primary key auto_increment,
    `userId`   int,
    `photoName` varchar(100) not null,
    `photoPath` varchar(100) not null
) ENGINE = InnoDB,
  charset = UTF8;
