requirements:
    Artemis ActiveMQ
        - https://activemq.apache.org/components/artemis/download/
        - ./artemis create assetsBroker
    Base de datos:
        - instalar postgres.
        - scripts: Code/scriptsSQL
            * psql -U postgres -h xhgrid2 -c "\i user-db.sql"
            * psql -U ingswi40 -h xhgrid2 -d assets -c "\i model.sql"
            * psql -U ingswi40 -h xhgrid2 -d assets -c "\i model-ddl.sql"
    java: version de java 11 LTS
    gradle: version de gradle 7.*


