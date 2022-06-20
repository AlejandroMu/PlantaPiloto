public class Main {
    public static void main(String[] args) {

        int module = 0;
        int channel =0;
        Opto22MMP opto= new Opto22MMP("epic1");
        try {
            System.out.println(opto.getDigitalPointState(1, 0));
            System.out.println(opto.setDigitalPointState(1, 1, 1));
            System.out.println(opto.getDigitalPointState(1, 0));
            System.out.println(opto.getAnalogPointMax(module,channel));
            System.out.println(opto.getAnalogPointMin(module,channel));
            System.out.println(opto.getAnalogPointValue(module,channel));
            System.out.println(opto.setAnalogPointValue(module,channel,(float) 0.1));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
