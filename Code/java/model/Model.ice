["java:package:icesi.plantapiloto"]
module controllers{

    interface ValueController{
        string getValues();
        void saveValues(string data);
    }
}