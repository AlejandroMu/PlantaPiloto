DROP TABLE IF EXISTS public."ControllableDevice" CASCADE;
DROP TABLE IF EXISTS public."IOModule" CASCADE;
DROP TABLE IF EXISTS public."Channel" CASCADE;
DROP TABLE IF EXISTS public."Value" CASCADE;
DROP SEQUENCE IF EXISTS seq_controllable;
DROP SEQUENCE IF EXISTS seq_IOModule;
DROP SEQUENCE IF EXISTS seq_Channel;
DROP SEQUENCE IF EXISTS seq_Value;



CREATE TABLE public."ControllableDevice" (
	id integer NOT NULL,
	type varchar,
	processor varchar,
	CONSTRAINT "ControllableDevice_pk" PRIMARY KEY (id)
);
ALTER TABLE public."ControllableDevice" OWNER TO postgres;

CREATE SEQUENCE seq_controllable
INCREMENT 1
START 1;

CREATE TABLE public."IOModule" (
	id integer NOT NULL,
	name varchar,
	type varchar,
	"id_ControllableDevice" integer,
	CONSTRAINT "IOModule_pk" PRIMARY KEY (id)
);
ALTER TABLE public."IOModule" OWNER TO postgres;

CREATE SEQUENCE seq_IOModule
INCREMENT 1
START 1;

ALTER TABLE public."IOModule" ADD CONSTRAINT "ControllableDevice_fk" FOREIGN KEY ("id_ControllableDevice")
REFERENCES public."ControllableDevice" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;

CREATE TABLE public."Channel" (
	id integer NOT NULL,
	range varchar,
	signal varchar,
	type varchar,
	unit varchar,
	"id_IOModule" integer,
	name varchar,
	CONSTRAINT "Channel_pk" PRIMARY KEY (id)
);
ALTER TABLE public."Channel" OWNER TO postgres;

CREATE SEQUENCE seq_Channel
INCREMENT 1
START 1;

ALTER TABLE public."Channel" ADD CONSTRAINT "IOModule_fk" FOREIGN KEY ("id_IOModule")
REFERENCES public."IOModule" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;

CREATE TABLE public."Value" (
	id integer NOT NULL,
	"timestamp" timestamp,
	value double precision,
	"id_Channel" integer,
	CONSTRAINT value_pk PRIMARY KEY (id)
);
ALTER TABLE public."Value" OWNER TO postgres;

CREATE SEQUENCE seq_Value
INCREMENT 1
START 1;

ALTER TABLE public."Value" ADD CONSTRAINT "Channel_fk" FOREIGN KEY ("id_Channel")
REFERENCES public."Channel" (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;


