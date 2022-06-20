
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Opto22MMP {
    private int tlabel;
    private Socket socket;
    private OutputStream outS;
    private InputStream intS;

    public Opto22MMP(String host){
        try {
            socket=new Socket(host, 2001);
            outS = socket.getOutputStream();
            intS = socket.getInputStream();
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Digital module
     * @param module
     * @param channel
     * @return
     * @throws Exception
     * 
     */
    public double getDigitalPointState(int module, int channel) throws Exception{
        long offset = (O22SIOUT.BASE_DPOINT_READ
                + (module * O22SIOUT.OFFSET_DPOINT_MOD)
                + (channel * O22SIOUT.OFFSET_DPOINT));
        byte[] data = readBlock(offset, 4);
        return unpackReadResponse(data,'i');
    }

    // SetDigitalPointState
    public double setDigitalPointState(int module, int channel, int state) throws Exception{
        long offset = (O22SIOUT.BASE_DPOINT_WRITE
                + (module * O22SIOUT.OFFSET_DPOINT_MOD)
                + (channel * O22SIOUT.OFFSET_DPOINT));
        byte[] data = writeBlock(offset, new int[]{0,0,0,state});
        return unpackReadResponse(data,'i');
    }
  
    /**
     * Analog module
     * @param module
     * @param channel
     * @return
     * @throws Exception
     */
    public double getAnalogPointMax(int module, int channel) throws Exception{
        long offset = (O22SIOUT.BASE_APOINT_READ
                + (O22SIOUT.OFFSET_APOINT_MOD * module)
                + (O22SIOUT.OFFSET_APOINT * channel)
                + O22SIOUT.OFFSET_APOINT_MAX);
        byte[] data = readBlock(offset, 4);
        return unpackReadResponse(data,'f');
    }

    public double getAnalogPointMin(int module, int channel) throws Exception{
        long offset = (O22SIOUT.BASE_APOINT_READ
                + (O22SIOUT.OFFSET_APOINT_MOD * module)
                + (O22SIOUT.OFFSET_APOINT * channel)
                + O22SIOUT.OFFSET_APOINT_MIN);
        byte[] data = readBlock(offset, 4);
        return unpackReadResponse(data, 'f');
    }

    public double getAnalogPointValue(int module, int channel) throws Exception{
        long offset = (O22SIOUT.BASE_APOINT_READ
                + (O22SIOUT.OFFSET_APOINT_MOD * module)
                + (O22SIOUT.OFFSET_APOINT * channel));
        byte[] data = readBlock(offset, 4);
        return unpackReadResponse(data, 'f');
    }

    // SetAnalogPointValue
    public double setAnalogPointValue(int module, int channel, float value) throws Exception{
        long offset = (O22SIOUT.BASE_APOINT_WRITE
                + (O22SIOUT.OFFSET_APOINT_MOD * module)
                + (O22SIOUT.OFFSET_APOINT * channel));
        byte[] data = writeBlock(offset, packFloat(value));
        return unpackReadResponse(data, 'i');
    }

    private int[] packFloat(float value) {
        int val= Float.floatToIntBits( value);
        int[] ret=new int[4];
        for (int i = 0; i < ret.length; i++) {
            int tmp = val>>(8*(3-i));
            tmp = tmp &255;
            ret[i] = tmp;
        }
        return ret;
    }
    public double unpackReadResponse(byte[] data,char type) throws Exception{
        int dataI[] = readInts(data);
        int rcode = dataI[6] >> 4;
        int tcode = dataI[3] >>4;
        if(rcode!=0){
            throw new Exception("Codigo de error: "+rcode);
        }
        if(tcode == 7){

            int res =0;
            for (int i = 0; i < 4; i++) {
                int tem = dataI[dataI.length-4+i] << (8*(3-i));
                res+=tem;
            }
            if(type == 'f'){
                return Float.intBitsToFloat(res);
            }
            return res;
        }else if(tcode == 2){
            return tcode;
        }
        throw new Exception("Codigo de respuesta invalido: "+tcode);
    }
    public byte[] readBlock(long address, int size){
        byte[] block = buildReadBlockRequest(address, size);
        byte[] response = new byte[O22SIOUT.SIZE_READ_BLOCK_RESPONSE+size];
        try {
            outS.write(block);
            intS.read(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    private byte[] writeBlock(long offset, int[] is) {
        byte[] block = buildWiteBlockRequest(offset, is);
        byte[] response = new byte[O22SIOUT.SIZE_WRITE_RESPONSE];
        try {
            outS.write(block);
            intS.read(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
    public byte[] buildWiteBlockRequest(long dest,int[] data){
        int tcode = O22SIOUT.TCODE_WRITE_BLOCK_REQUEST;
        int[] destAr = hexaByte(dest);
        int[] block = {
            0, 0, tlabel << 2, tcode << 4, 0, 0, 255, 255,
            destAr[0], destAr[1],destAr[2],destAr[3],
            0,data.length, 0,0,data[0],data[1],data[2],data[3]};
        
        return writeInts(block);
    }
    public byte[] buildReadBlockRequest(long dest, int size){
        int tcode = O22SIOUT.TCODE_READ_BLOCK_REQUEST;
        int[] destAr=hexaByte(dest);
        int[] block = {
            0, 0, tlabel << 2, tcode << 4, 0, 0, 255, 255,
            destAr[0], destAr[1],destAr[2],destAr[3],
            0,size, 0,0
        };
        byte[] bytes = writeInts(block);

        return bytes;    
    }
    public int[] hexaByte(long n){
        String hex = Long.toHexString(n);
        int length = hex.length();
        hex=hex.substring(length-8);
        int[] ret=new int[4];
        for(int i=0,j=0;i<hex.length();i+=2,j++){
            String tmp=hex.substring(i,i+2);
            ret[j]=Integer.parseInt(tmp,16);
        }
        return ret;
    }
   
    public int[] readInts(byte[] arr){
        int[] values=new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            values[i] = arr[i] & 255;
        }
        return values;
    }
    private byte[] writeInts(int[] array) {
        byte[] res =new byte[array.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte)array[i];
        }
        return res;
    }
}
