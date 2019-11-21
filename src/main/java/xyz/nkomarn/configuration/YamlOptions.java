package xyz.nkomarn.configuration;

import com.sun.istack.internal.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class YamlOptions {
    public String[] ntw;
    public String[] msg;
    public String[] mtd;

    /* The three methods get the YAML values */
    public void network(){
        String net;
        net = yaml("network");
        ntw = makeArray(net);
    }
    public void messages(){
        String message;
        message = yaml("message");
        msg = makeArray(message);
    }

    public void motd(){
        String mot;
        mot = yaml("motd");
        mtd = makeArray(mot);
    }


    /*Just simply turns a string into a String array. */
    private static String[] makeArray(String arg) {
        String[] arr = arg.split(",");
        ArrayList<String> a = new ArrayList<String>();

        for (int i = 0; i < arr.length; i++) {

            String yaml;
            yaml = arr[i].substring(arr[i].indexOf("=") + 1);
            a.add(yaml.split("}")[0]);

        }
        String[] ar = new String[a.size()];
        ar = a.toArray(ar);
        return ar;
    }


    /* Gets the YAML */
    @NotNull
    private static String yaml(String yamlArgument){
        final String filePath = "src/main/resources/composter.yml";
        ArrayList<String> value = new ArrayList<String>();
        Yaml yaml = new Yaml();

        try {

            Reader yamlFile = new FileReader(filePath);
            Map<String, Object> yamlMaps = yaml.load(yamlFile);
            value.add(yamlMaps.get(yamlArgument).toString());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return " " + value;
    }

}
