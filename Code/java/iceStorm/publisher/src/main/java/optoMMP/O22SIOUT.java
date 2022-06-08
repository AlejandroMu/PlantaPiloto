package optoMMP;

public class O22SIOUT{
    //Transaction Codes (tcode)
    public final static int TCODE_WRITE_QUAD_REQUEST = 0;
    public final static int TCODE_WRITE_BLOCK_REQUEST   = 1;
    public final static int TCODE_WRITE_RESPONSE        = 2;
    public final static int TCODE_READ_QUAD_REQUEST     = 4;
    public final static int TCODE_READ_BLOCK_REQUEST    = 5;
    public final static int TCODE_READ_QUAD_RESPONSE    = 6;
    public final static int TCODE_READ_BLOCK_RESPONSE   = 7;
    // Opto 22 mem-map package sizes;
    public final static int SIZE_WRITE_QUAD_REQUEST     = 16;
    public final static int SIZE_WRITE_BLOCK_REQUEST    = 16;
    public final static int SIZE_WRITE_RESPONSE         = 12;
    public final static int SIZE_READ_QUAD_REQUEST      = 12;
    public final static int SIZE_READ_BLOCK_REQUEST     = 16;
    public final static int SIZE_READ_QUAD_RESPONSE     = 16;
    public final static int SIZE_READ_BLOCK_RESPONSE    = 16;
    
    // Digital IO area
    public final static int BASE_DPOINT_WRITE           = 0xF0220000;
    public final static int BASE_DPOINT_READ            = 0xF01E0000;
    public final static int OFFSET_DPOINT_MOD           = 0x00001000;
    public final static int OFFSET_DPOINT               = 0x00000040;
    //Analog IO area
    public final static int BASE_APOINT_WRITE           = 0xF02A0000;
    public final static int BASE_APOINT_READ            = 0xF0260000;
    public final static int OFFSET_APOINT_MOD           = 0x00001000;
    public final static int OFFSET_APOINT               = 0x00000040;
    public final static int OFFSET_APOINT_MIN           = 0x00000008;
    public final static int OFFSET_APOINT_MAX           = 0x0000000C;
    
    // System status area
    public final static int  BASE_IP_ADDRESS_ETH0        = 0xF0300034;
    public final static int  BASE_MAC_ADDRESS_ETH0       = 0xF030002E;
    public final static int BASE_IP_ADDRESS_ETH1        = 0xFFFFF050;
    public final static int BASE_MAC_ADDRESS_ETH1       = 0xFFFFF060;
    public final static int BASE_FIRMWARE_VERSION       = 0xF030001C;
    public final static int BASE_UNIT_DESCRIPTION       = 0xF0300080;
    public final static int BASE_LAST_ERROR             = 0xF030000C;
    
    // ScratchPad area
    //   string
    public final static int BASE_SCRATCHPAD_STRING      = 0xF0D83000;
    public final static int OFFSET_SCRATCHPAD_STRING    = 0x00000082;
    public final static int MAX_BYTES_STRING            = 0x00002080;
    //   float
    public final static int BASE_SCRATCHPAD_FLOAT       = 0xF0D82000;
    public final static int BASE_SCRATCHPAD_FLOAT_1     = 0xF0D82000;
    public final static int  BASE_SCRATCHPAD_FLOAT_2     = 0xF0DC0000;
    public final static int BASE_SCRATCHPAD_FLOAT_3     = 0xF0DC2000;
    public final static int MAX_ELEMENTS_FLOAT_1        = 0x00000400;
    public final static int MAX_ELEMENTS_FLOAT_2        = 0x00000800;
    public final static int MAX_ELEMENTS_FLOAT_3        = 0x00001C00;
    public final static int MAX_ELEMENTS_FLOAT          = MAX_ELEMENTS_FLOAT_1 + MAX_ELEMENTS_FLOAT_2 + MAX_ELEMENTS_FLOAT_3;
    public final static int MAX_BYTES_FLOAT             = MAX_ELEMENTS_FLOAT * 4;
    //   integer
    public final static int BASE_SCRATCHPAD_INTEGER     = 0xF0D81000;
    public final static int BASE_SCRATCHPAD_INTEGER_1   = 0xF0D81000;
    public final static int BASE_SCRATCHPAD_INTEGER_2   = 0xF0DA0000;
    public final static int BASE_SCRATCHPAD_INTEGER_3   = 0xF0DA2000;
    // integer
    public final static int MAX_ELEMENTS_INTEGER_1      = 0x00000400;
    public final static int MAX_ELEMENTS_INTEGER_2      = 0x00000800;
    public final static int MAX_ELEMENTS_INTEGER_3      = 0x00001C00;
    public final static int MAX_ELEMENTS_INTEGER        = MAX_ELEMENTS_INTEGER_1 + MAX_ELEMENTS_INTEGER_2 + MAX_ELEMENTS_INTEGER_3;
    public final static int MAX_BYTES_INTEGER           = MAX_ELEMENTS_INTEGER * 4;
    
}