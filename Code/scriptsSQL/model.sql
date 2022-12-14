CREATE SCHEMA IF NOT EXISTS icesi_bionic_plantapiloto;
SET search_path to icesi_bionic_plantapiloto;
DROP TABLE IF EXISTS ControllableDevice CASCADE;
DROP TABLE IF EXISTS IOModule CASCADE;
DROP TABLE IF EXISTS Channel CASCADE;
DROP TABLE IF EXISTS Value CASCADE;
DROP SEQUENCE IF EXISTS seq_controllable;
DROP SEQUENCE IF EXISTS seq_IOModule;
DROP SEQUENCE IF EXISTS seq_Channel;
DROP SEQUENCE IF EXISTS seq_Value;



CREATE TABLE ControllableDevice (
	id integer NOT NULL,
	type varchar,
	processor varchar,
	CONSTRAINT ControllableDevice_pk PRIMARY KEY (id)
);
ALTER TABLE ControllableDevice OWNER TO postgres;

CREATE SEQUENCE seq_controllable
INCREMENT 1
START 1;

CREATE TABLE IOModule (
	id integer NOT NULL,
	name varchar,
	type varchar,
	device_id integer,
	CONSTRAINT IOModule_pk PRIMARY KEY (id)
);
ALTER TABLE IOModule OWNER TO postgres;

CREATE SEQUENCE seq_IOModule
INCREMENT 1
START 1;

ALTER TABLE IOModule ADD CONSTRAINT ControllableDevice_fk FOREIGN KEY (device_id)
REFERENCES ControllableDevice (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;

CREATE TABLE Channel (
	id integer NOT NULL,
	range varchar,
	signal varchar,
	type varchar,
	unit varchar,
	module_id integer,
	name varchar,
	CONSTRAINT Channel_pk PRIMARY KEY (id)
);
ALTER TABLE Channel OWNER TO postgres;

CREATE SEQUENCE seq_Channel
INCREMENT 1
START 1;

ALTER TABLE Channel ADD CONSTRAINT IOModule_fk FOREIGN KEY (module_id)
REFERENCES IOModule (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;

CREATE TABLE Value (
	id integer NOT NULL,
	timestamp timestamp,
	value double precision,
	channel_id integer,
	CONSTRAINT value_pk PRIMARY KEY (id)
);
ALTER TABLE Value OWNER TO postgres;

CREATE SEQUENCE seq_Value
INCREMENT 1
START 1;

ALTER TABLE Value ADD CONSTRAINT Channel_fk FOREIGN KEY (channel_id)
REFERENCES Channel (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;


