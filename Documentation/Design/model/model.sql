-- This script was generated by a beta version of the ERD tool in pgAdmin 4.
-- Please log an issue at https://redmine.postgresql.org/projects/pgadmin4/issues/new if you find any bugs, including reproduction steps.
CREATE SCHEMA icesi_bionic_plantapiloto;

CREATE TABLE icesi_bionic_plantapiloto.asset
(
    id integer NOT NULL,
    name character varying NOT NULL,
    description character varying NOT NULL,
    type integer NOT NULL,
    work_space integer NOT NULL,
    asset_sup integer,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.measurement
(
    id integer NOT NULL,
    value double precision NOT NULL,
    "time" timestamp with time zone NOT NULL,
    process_asset integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.type
(
    id integer NOT NULL,
    name character varying NOT NULL,
    description character varying NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.work_space
(
    name character varying NOT NULL,
    id integer NOT NULL,
    description character varying NOT NULL,
    department integer,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.process
(
    id integer NOT NULL,
    name character varying NOT NULL,
    description character varying,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.process_asset
(
    process_id integer NOT NULL,
    asset_id integer NOT NULL,
    init_date timestamp with time zone NOT NULL,
    id integer NOT NULL,
    end_date timestamp with time zone,
    PRIMARY KEY (id)
);

CREATE TABLE icesi_bionic_plantapiloto.instruction
(
    name_tech character varying NOT NULL,
    name_user character varying NOT NULL,
    predicate character varying NOT NULL,
    type_inst char NOT NULL,
    PRIMARY KEY (name_tech)
);

CREATE TABLE icesi_bionic_plantapiloto.instruction_process
(
    instruction_name_tech character varying NOT NULL,
    process_id integer NOT NULL
);

CREATE TABLE icesi_bionic_plantapiloto.action
(
    name_tech character varying NOT NULL,
    name_user "char" NOT NULL,
    function_act character varying NOT NULL,
    PRIMARY KEY (name_tech)
);

CREATE TABLE icesi_bionic_plantapiloto.action_instruction
(
    action_name_tech character varying NOT NULL,
    instruction_name_tech character varying NOT NULL
);

CREATE TABLE icesi_bionic_plantapiloto.action_process_asset
(
    action_name_tech character varying NOT NULL,
    process_asset_id integer NOT NULL,
    action_date timestamp with time zone NOT NULL,
    id integer NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE icesi_bionic_plantapiloto.asset
    ADD FOREIGN KEY (asset_sup)
    REFERENCES icesi_bionic_plantapiloto.asset (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.asset
    ADD FOREIGN KEY (type)
    REFERENCES icesi_bionic_plantapiloto.type (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.asset
    ADD FOREIGN KEY (work_space)
    REFERENCES icesi_bionic_plantapiloto.work_space (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.work_space
    ADD FOREIGN KEY (department)
    REFERENCES icesi_bionic_plantapiloto.work_space (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.process_asset
    ADD FOREIGN KEY (process_id)
    REFERENCES icesi_bionic_plantapiloto.process (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.process_asset
    ADD FOREIGN KEY (asset_id)
    REFERENCES icesi_bionic_plantapiloto.asset (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.measurement
    ADD FOREIGN KEY (process_asset)
    REFERENCES icesi_bionic_plantapiloto.process_asset (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.instruction_process
    ADD FOREIGN KEY (instruction_name_tech)
    REFERENCES icesi_bionic_plantapiloto.instruction (name_tech)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.instruction_process
    ADD FOREIGN KEY (process_id)
    REFERENCES icesi_bionic_plantapiloto.process (id)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.action_instruction
    ADD FOREIGN KEY (action_name_tech)
    REFERENCES icesi_bionic_plantapiloto.action (name_tech)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.action_instruction
    ADD FOREIGN KEY (instruction_name_tech)
    REFERENCES icesi_bionic_plantapiloto.instruction (name_tech)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.action_process_asset
    ADD FOREIGN KEY (action_name_tech)
    REFERENCES icesi_bionic_plantapiloto.action (name_tech)
    NOT VALID;


ALTER TABLE icesi_bionic_plantapiloto.action_process_asset
    ADD FOREIGN KEY (process_asset_id)
    REFERENCES icesi_bionic_plantapiloto.process_asset (id)
    NOT VALID;

COMMENT ON COLUMN icesi_bionic_plantapiloto.instruction.type_inst
    IS 'identificar si la instuccion se hace de forma automatica A o Manual M';