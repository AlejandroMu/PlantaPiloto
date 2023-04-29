CREATE TABLE "process_asset"(
    "asset_id" INTEGER NOT NULL,
    "process_id" INTEGER NOT NULL,
    "delay_read" BIGINT NOT NULL
);
ALTER TABLE
    "process_asset" ADD PRIMARY KEY("asset_id");
ALTER TABLE
    "process_asset" ADD PRIMARY KEY("process_id");
CREATE TABLE "work_space"(
    "name" VARCHAR(255) NOT NULL,
    "id" INTEGER NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "department" INTEGER NULL
);
ALTER TABLE
    "work_space" ADD PRIMARY KEY("id");
CREATE TABLE "instruction"(
    "name_tech" VARCHAR(255) NOT NULL,
    "predicate" VARCHAR(255) NOT NULL,
    "type" INTEGER NOT NULL
);
ALTER TABLE
    "instruction" ADD PRIMARY KEY("name_tech");
CREATE TABLE "type"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "table_name" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "type" ADD PRIMARY KEY("id");
CREATE TABLE "meta_data"(
    "id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "value" VARCHAR(255) NOT NULL,
    "description" BIGINT NOT NULL,
    "asset" INTEGER NOT NULL
);
ALTER TABLE
    "meta_data" ADD PRIMARY KEY("id");
CREATE TABLE "action_instruction"(
    "action" VARCHAR(255) NOT NULL,
    "instruction" VARCHAR(255) NOT NULL
);
CREATE TABLE "asset"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "type" INTEGER NOT NULL,
    "driver" INTEGER NOT NULL,
    "asset_sup" INTEGER NULL,
    "asset_state" CHAR(255) NOT NULL
);
ALTER TABLE
    "asset" ADD PRIMARY KEY("id");
CREATE TABLE "execution"(
    "id" INTEGER NOT NULL,
    "process" INTEGER NOT NULL,
    "start_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "end_date" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "oper_username" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "execution" ADD PRIMARY KEY("id");
CREATE TABLE "measurement"(
    "id" INTEGER NOT NULL,
    "value" DOUBLE PRECISION NOT NULL,
    "time" TIMESTAMP(0) WITH
        TIME zone NOT NULL,
        "asset" INTEGER NOT NULL,
        "execution" INTEGER NOT NULL
);
ALTER TABLE
    "measurement" ADD PRIMARY KEY("id");
CREATE TABLE "instruction_process"(
    "instruction" VARCHAR(255) NOT NULL,
    "process" INTEGER NOT NULL
);
CREATE TABLE "driver"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "service_proxy" VARCHAR(255) NOT NULL,
    "work_space" INTEGER NOT NULL
);
ALTER TABLE
    "driver" ADD PRIMARY KEY("id");
COMMENT
ON COLUMN
    "driver"."service_proxy" IS 'ubicación del servicio usando la definicion de proxy de ICE: service:protocol -h host -p port';
CREATE TABLE "process"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NULL,
    "work_space" INTEGER NOT NULL
);
ALTER TABLE
    "process" ADD PRIMARY KEY("id");
CREATE TABLE "execution_instruction"(
    "execution" INTEGER NOT NULL,
    "instruction" VARCHAR(255) NOT NULL,
    "exc_time" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);
ALTER TABLE
    "execution_instruction" ADD PRIMARY KEY("execution");
ALTER TABLE
    "execution_instruction" ADD PRIMARY KEY("instruction");
CREATE TABLE "action"(
    "name_tech" VARCHAR(255) NOT NULL,
    "name_user" CHAR(255) NOT NULL,
    "function_act" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "action" ADD PRIMARY KEY("name_tech");
ALTER TABLE
    "meta_data" ADD CONSTRAINT "meta_data_asset_foreign" FOREIGN KEY("asset") REFERENCES "asset"("id");
ALTER TABLE
    "instruction_process" ADD CONSTRAINT "instruction_process_process_foreign" FOREIGN KEY("process") REFERENCES "process"("id");
ALTER TABLE
    "measurement" ADD CONSTRAINT "measurement_asset_foreign" FOREIGN KEY("asset") REFERENCES "asset"("id");
ALTER TABLE
    "process" ADD CONSTRAINT "process_work_space_foreign" FOREIGN KEY("work_space") REFERENCES "work_space"("id");
ALTER TABLE
    "action_instruction" ADD CONSTRAINT "action_instruction_action_foreign" FOREIGN KEY("action") REFERENCES "action"("name_tech");
ALTER TABLE
    "work_space" ADD CONSTRAINT "work_space_department_foreign" FOREIGN KEY("department") REFERENCES "work_space"("id");
ALTER TABLE
    "driver" ADD CONSTRAINT "driver_work_space_foreign" FOREIGN KEY("work_space") REFERENCES "work_space"("id");
ALTER TABLE
    "process_asset" ADD CONSTRAINT "process_asset_process_id_foreign" FOREIGN KEY("process_id") REFERENCES "process"("id");
ALTER TABLE
    "action_instruction" ADD CONSTRAINT "action_instruction_instruction_foreign" FOREIGN KEY("instruction") REFERENCES "instruction"("name_tech");
ALTER TABLE
    "measurement" ADD CONSTRAINT "measurement_execution_foreign" FOREIGN KEY("execution") REFERENCES "execution"("id");
ALTER TABLE
    "asset" ADD CONSTRAINT "asset_type_foreign" FOREIGN KEY("type") REFERENCES "type"("id");
ALTER TABLE
    "execution" ADD CONSTRAINT "execution_process_foreign" FOREIGN KEY("process") REFERENCES "process"("id");
ALTER TABLE
    "instruction_process" ADD CONSTRAINT "instruction_process_instruction_foreign" FOREIGN KEY("instruction") REFERENCES "instruction"("name_tech");
ALTER TABLE
    "process_asset" ADD CONSTRAINT "process_asset_asset_id_foreign" FOREIGN KEY("asset_id") REFERENCES "asset"("id");
ALTER TABLE
    "asset" ADD CONSTRAINT "asset_asset_sup_foreign" FOREIGN KEY("asset_sup") REFERENCES "asset"("id");
ALTER TABLE
    "execution_instruction" ADD CONSTRAINT "execution_instruction_instruction_foreign" FOREIGN KEY("instruction") REFERENCES "instruction"("name_tech");
ALTER TABLE
    "execution_instruction" ADD CONSTRAINT "execution_instruction_execution_foreign" FOREIGN KEY("execution") REFERENCES "execution"("id");
ALTER TABLE
    "asset" ADD CONSTRAINT "asset_driver_foreign" FOREIGN KEY("driver") REFERENCES "driver"("id");