package xyz.nkomarn.configuration;

public class Messages extends YamlOptions{
    private Messages obj = new Messages();
    private String[] yaml = obj.msg;
    private YamlOptions parent = new YamlOptions();

    public String protocol_kick(){
        parent.messages();
        return yaml[0];
    }
}
