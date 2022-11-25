package icesi.plantapiloto.controlLayer.common;
import java.util.HashMap;
import java.util.List;

public interface PluginI {
    public List<Message> getValues();
    public HashMap<String,String> getSettings();
}
