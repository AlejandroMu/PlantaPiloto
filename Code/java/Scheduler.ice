["java:package:icesi.plantapiloto.scheduleManager"]
module controllers{
    interface SchedulerController{
        void startProcess(string data);
        void stopProccess(string data);
        bool changeSetPoint(string data);
    }
}