package xyz.nkomarn.configuration;

import java.util.Arrays;

public class Motd extends YamlOptions{
    private Motd obj = new Motd();
    private String[] yaml = obj.mtd;
    private YamlOptions parent = new YamlOptions();

    public Boolean enabled(){
        parent.motd();
        String enabled = yaml[0];
        return Boolean.parseBoolean(enabled);
    }

    public String msg(){
        parent.motd();
        return yaml[1];
    }
}
