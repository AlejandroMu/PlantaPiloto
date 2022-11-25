["java:package:icesi.plantapiloto.controller"]
module entityManager{

    ["java:serializable:icesi.plantapiloto.model.EntityWrapper"]
	sequence<byte> Message;

    interface ManagerI{
        void begin();
        void commit();
        void save(string elemnt);
        Message getById();
    }
}