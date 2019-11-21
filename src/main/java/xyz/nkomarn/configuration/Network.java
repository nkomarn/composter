package xyz.nkomarn.configuration;

public class Network extends YamlOptions{
    private Network obj = new Network();
    private String[] yaml = obj.ntw;
    private YamlOptions parent = new YamlOptions();

    public String host(){
        parent.network();
        return yaml[0];
    }

    public String port(){
        parent.network();
        return yaml[1];
    }

}
