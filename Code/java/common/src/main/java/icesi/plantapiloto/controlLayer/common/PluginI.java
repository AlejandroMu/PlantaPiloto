package icesi.plantapiloto.controlLayer.common;

import java.util.HashMap;

import icesi.plantapiloto.controlLayer.common.entities.Message;

public interface PluginI {
    public Message getMessage();

    public HashMap<String, String> getSettings();

    public long getDelay();

    public long getInitialDelay();
}
