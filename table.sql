CREATE TABLE playo.player
(
	pname varchar(40),
    email varchar(40) PRIMARY KEY,
    region varchar(10),
    gender varchar(6),
    city varchar(12),
    password varchar(30),
    dob date
);

CREATE TABLE playo.owner
(
	oname varchar(40),
    mobile varchar(14) PRIMARY KEY,
    password varchar(30),
    dob date,
    gender varchar(6)
);

CREATE TABLE playo.sports
(
	sname varchar(20),
    sid int NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE playo.arena
(
	aid int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    opening time,
    closing time,
    aname varchar(20),
    rating real,
    city varchar(12),
    region varchar(10),
    sid int,
    mobile varchar(14),
    FOREIGN KEY(sid) REFERENCES playo.sports(sid) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY(mobile) REFERENCES playo.owner(mobile) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE playo.court
(
	cid int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cost int,
    aid int,
    FOREIGN KEY(aid) REFERENCES playo.arena(aid) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE playo.booking
(
	bid int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    start_slot int,
    end_slot int,
    bdate date,
    hosting bool,
    email varchar(40),
    cid int,
    FOREIGN KEY(email) REFERENCES playo.player(email) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY(cid) REFERENCES playo.court(cid) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE playo.hosting
(
	bid int,
    email varchar(40),
    max_players int,
	PRIMARY KEY(bid,email),
    FOREIGN KEY(bid) REFERENCES playo.booking(bid) ON UPDATE CASCADE,
    FOREIGN KEY(email) REFERENCES playo.player(email) ON UPDATE CASCADE
);