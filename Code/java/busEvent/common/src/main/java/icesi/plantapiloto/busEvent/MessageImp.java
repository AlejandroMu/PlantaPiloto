package icesi.plantapiloto.busEvent;

public class MessageImp implements Message {
    private String value;


    public MessageImp(String value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    
}
