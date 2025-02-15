/**
Cart for users to store classes
*/
CREATE TABLE "cart" (
	"id"	INTEGER,
	"course_id"	INTEGER,
	"user_id"	INTEGER,
	"purchased"	TEXT,
	"purchaseID"	TEXT,
	PRIMARY KEY("id")
);

/**
Classes metadata
*/
CREATE TABLE "classes" (
	"class_id"	INTEGER NOT NULL UNIQUE,
	"name"	TEXT NOT NULL,
	"shortname"	TEXT NOT NULL UNIQUE,
	"tags"	TEXT NOT NULL,
	"desc"	STRING,
	PRIMARY KEY("class_id" AUTOINCREMENT)
);

/**
More metadata about classes
*/
CREATE TABLE "classes2" (
	"class_id"	INTEGER NOT NULL UNIQUE,
	"age_lower"	INTEGER NOT NULL,
	"age_upper"	INTEGER NOT NULL,
	"school_lvl"	TEXT NOT NULL,
	"size"	INTEGER NOT NULL,
	"seats_left"	INTEGER NOT NULL,
	"date_time"	TEXT,
	"hours"	INTEGER,
	"tuition"	INTEGER,
	FOREIGN KEY("class_id") REFERENCES "classes"("class_id"),
	PRIMARY KEY("class_id")
);

/*
Storing emails for mailing list
*/
CREATE TABLE "emails" (
	"email_id"	INTEGER NOT NULL UNIQUE,
	"email"	TEXT NOT NULL,
	"fullname"	TEXT NOT NULL,
	PRIMARY KEY("email_id" AUTOINCREMENT)
);

/*
User data
*/
CREATE TABLE "users" (
	"username"	TEXT UNIQUE,
	"password"	TEXT,
	"fullname"	TEXT,
	"id"	INTEGER,
	PRIMARY KEY("id")
);