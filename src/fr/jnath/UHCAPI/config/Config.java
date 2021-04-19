package fr.jnath.UHCAPI.config;


import fr.virthia.utils.GUI.GUI;

import java.io.Serializable;
import java.util.List;

public class Config implements Serializable {
    public static Config toConfig(List<GUI> guis){
        return new Config();
    }
}
