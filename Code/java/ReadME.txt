requirements:
    Artemis ActiveMQ
        - correr el script broker segun el sistema.
            * broker.sh : instala e inicia un broker de artemis
            * broker.sh stop: para el servicio
            * broker.sh start: inicia
            * broker.sh status: indica el estado
    Base de datos:
        - instalar postgres.
        - scripts: Code/scriptsSQL
            * psql -U postgres -h xhgrid2 -c "\i user-db.sql"
            * psql -U ingswi40 -h xhgrid2 -d assets -c "\i model.sql"
            * psql -U ingswi40 -h xhgrid2 -d assets -c "\i model_ddl.sql"
    java: version de java 11 LTS
    gradle: version de gradle 7.*
