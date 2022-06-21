import opto22.mmp.Opto22MMP;

public class Main {
    public static void main(String[] args) {

        int module = 0;
        int channel =0;
        Opto22MMP opto= new Opto22MMP("epic1");
        try {
            System.out.println("get digitalPointState "+opto.getDigitalPointState(1, 0));
            System.out.println("set digitalPointState "+opto.setDigitalPointState(1, 1, 1));
            System.out.println("get Analog Max "+opto.getAnalogPointMax(module,channel));
            System.out.println("get Analog min "+opto.getAnalogPointMin(module,channel));
            System.out.println("get Analog poin value "+opto.getAnalogPointValue(module,channel));
            System.out.println("set Analog poin value "+opto.setAnalogPointValue(module,channel,(float) 0.1));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
