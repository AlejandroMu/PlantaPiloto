DROP SCHEMA  IF EXISTS asset_manager CASCADE;
CREATE SCHEMA asset_manager;
SET search_path to asset_manager;

CREATE TABLE action (
    id integer NOT NULL,
    name_tech character varying(255),
    name_user character varying(255),
    function_act character varying(255)
);

CREATE TABLE action_instruction (
    action integer NOT NULL,
    instruction integer NOT NULL
);

CREATE SEQUENCE action_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE asset (
    id integer NOT NULL,
    asset_state character varying(255),
    name character varying(255),
    description character varying(255),
    type integer,
    work_space integer,
    asset_sup integer
);

CREATE SEQUENCE asset_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE driver (
    id integer NOT NULL,
    name character varying(255),
    service_proxy character varying(255),
    work_space integer
);

CREATE SEQUENCE driver_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE execution (
    id integer NOT NULL,
    end_date timestamp without time zone,
    oper_username character varying(255),
    start_date timestamp without time zone,
    process integer,
    status character varying(255)
);

CREATE SEQUENCE execution_inst_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE execution_instruction (
    id integer NOT NULL,
    exc_time timestamp without time zone,
    execution integer,
    instruction integer
);


CREATE SEQUENCE execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE instruction (
    id integer NOT NULL,
    name_tech character varying(255),
    predicate character varying(255),
    type character varying(255)
);

CREATE TABLE instruction_process (
    instruction integer NOT NULL,
    process integer NOT NULL
);

CREATE SEQUENCE instruction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE measure_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE measurement (
    id integer NOT NULL,
    "time" timestamp without time zone,
    value double precision NOT NULL,
    asset integer,
    execution integer
);

CREATE TABLE meta_data (
    id bigint NOT NULL,
    name character varying(255),
    value character varying(255),
    description character varying(255),
    asset integer
);

CREATE SEQUENCE meta_data_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE process (
    id integer NOT NULL,
    description character varying(255),
    name character varying(255),
    work_space integer
);

CREATE TABLE process_asset (
    process_id integer NOT NULL,
    asset_id integer NOT NULL,
    delay_read bigint
);

CREATE SEQUENCE process_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE asset_type (
    id integer NOT NULL,
    name character varying(255),
    description character varying(255),
    driver integer
);

CREATE SEQUENCE type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE work_space (
    id integer NOT NULL,
    name character varying(255),
    description character varying(255),
    department integer
);

CREATE SEQUENCE work_space_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE action
    ADD CONSTRAINT action_pkey PRIMARY KEY (id);

ALTER TABLE asset
    ADD CONSTRAINT asset_pkey PRIMARY KEY (id);

ALTER TABLE driver
    ADD CONSTRAINT driver_pkey PRIMARY KEY (id);

ALTER TABLE execution_instruction
    ADD CONSTRAINT execution_instruction_pkey PRIMARY KEY (id);

ALTER TABLE execution
    ADD CONSTRAINT execution_pkey PRIMARY KEY (id);

ALTER TABLE instruction
    ADD CONSTRAINT instruction_pkey PRIMARY KEY (id);

ALTER TABLE measurement
    ADD CONSTRAINT measurement_pkey PRIMARY KEY (id);

ALTER TABLE meta_data
    ADD CONSTRAINT meta_data_pkey PRIMARY KEY (id);

ALTER TABLE process_asset
    ADD CONSTRAINT process_asset_pkey PRIMARY KEY (asset_id, process_id);

ALTER TABLE process
    ADD CONSTRAINT process_pkey PRIMARY KEY (id);

ALTER TABLE asset_type
    ADD CONSTRAINT type_pkey PRIMARY KEY (id);

ALTER TABLE work_space
    ADD CONSTRAINT work_space_pkey PRIMARY KEY (id);

ALTER TABLE process_asset
    ADD CONSTRAINT process_process_id FOREIGN KEY (process_id) REFERENCES process(id);

ALTER TABLE instruction_process
    ADD CONSTRAINT process_process_id FOREIGN KEY (process) REFERENCES process(id);

ALTER TABLE asset
    ADD CONSTRAINT worker_space_id FOREIGN KEY (work_space) REFERENCES work_space(id);

ALTER TABLE measurement
    ADD CONSTRAINT execution_id FOREIGN KEY (execution) REFERENCES execution(id);

ALTER TABLE process
    ADD CONSTRAINT work_space_id FOREIGN KEY (work_space) REFERENCES work_space(id);

ALTER TABLE work_space
    ADD CONSTRAINT department_id FOREIGN KEY (department) REFERENCES work_space(id);

ALTER TABLE execution_instruction
    ADD CONSTRAINT execution_id FOREIGN KEY (execution) REFERENCES execution(id);

ALTER TABLE execution
    ADD CONSTRAINT process_id FOREIGN KEY (process) REFERENCES process(id);

ALTER TABLE action_instruction
    ADD CONSTRAINT action_id FOREIGN KEY (action) REFERENCES action(id);

ALTER TABLE meta_data
    ADD CONSTRAINT asset_id FOREIGN KEY (asset) REFERENCES asset(id);

ALTER TABLE asset
    ADD CONSTRAINT asset_sup_id FOREIGN KEY (asset_sup) REFERENCES asset(id);

ALTER TABLE process_asset
    ADD CONSTRAINT asset_id_id FOREIGN KEY (asset_id) REFERENCES asset(id);

ALTER TABLE driver
    ADD CONSTRAINT work_space_id FOREIGN KEY (work_space) REFERENCES work_space(id);

ALTER TABLE instruction_process
    ADD CONSTRAINT instruction_id FOREIGN KEY (instruction) REFERENCES instruction(id);

ALTER TABLE execution_instruction
    ADD CONSTRAINT instruction_id FOREIGN KEY (instruction) REFERENCES instruction(id);

ALTER TABLE asset
    ADD CONSTRAINT type_id FOREIGN KEY (type) REFERENCES asset_type(id);

ALTER TABLE action_instruction
    ADD CONSTRAINT instruction_id FOREIGN KEY (instruction) REFERENCES instruction(id);

ALTER TABLE measurement
    ADD CONSTRAINT asset_id FOREIGN KEY (asset) REFERENCES asset(id);

ALTER TABLE asset_type
    ADD CONSTRAINT driver_id FOREIGN KEY (driver) REFERENCES driver(id);




