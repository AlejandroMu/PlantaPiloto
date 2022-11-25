import opto22.mmp.Opto22MMP;

public class Main {
    public static void main(String[] args) {

        // int module = 0;
        // int channel = 0;
        Opto22MMP opto = new Opto22MMP("xepic1");
        try {
            System.out.println("get digitalPointState " + opto.getDigitalPointState(1, 0));
            System.out.println("set digitalPointState " + opto.setDigitalPointState(1, 1, 1));
            // System.out.println("get Analog Max " + opto.getAnalogPointMax(module,
            // channel));
            // System.out.println("get Analog min " + opto.getAnalogPointMin(module,
            // channel));
            // System.out.println("get Analog point value " +
            // opto.getAnalogPointValue(module, channel));
            // System.out.println("set Analog point value " +
            // opto.setAnalogPointValue(module, channel, (float) 0.1));
            // double[] val = opto.getAnalogPointValues(module, 2);
            // for (int i = 0; i < val.length; i++) {
            // System.out.println("--> get Analog points value " + i + "- " + val[i]);
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
