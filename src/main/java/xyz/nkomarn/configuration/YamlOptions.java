package xyz.nkomarn.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class YamlOptions {


    public static void main(String[] args){
        String get = yamlResult("motd.enabled");
        System.out.println(Boolean.parseBoolean(get));
    }


    public String getString(String arg) {
        return yamlResult(arg);
    }

    public Boolean getBoolean(String arg){
        String get = yamlResult(arg);
        return Boolean.parseBoolean(get);
    }

    public int getInt(String arg){
        String get = yamlResult(arg);
        return Integer.parseInt(get);
    }

    public Double getDouble(String arg){
        String get = yamlResult(arg);
        return Double.parseDouble(get);
    }

    public Float getFloat(String arg){
        String get = yamlResult(arg);
        return Float.parseFloat(get);
    }

    public ArrayList<String> getList(String arg){
        ArrayList<String> ar = new ArrayList<String>();
        ar.add(arg);
        return ar;
    }



    private static String yamlResult(String arg){

        String parse = stringSplit(arg, 0);
        String yaml = yaml(parse);
        String[] children = makeArray(yaml);

        String result = "";
        for(int i=0; i<children.length; i++){
            if(children[i].contains(stringSplit(arg, 1))){
                result = children[i].substring(children[i].indexOf("=") + 1);
            }
        }
        return result;
    }

    private static String stringSplit(String arg, int index){
        String[] split = arg.split("\\.");
        return split[index];
    }

    private static String[] makeArray(String arg) {
        String[] arr = arg.split(",");
        ArrayList<String> a = new ArrayList<String>();

        for (int i = 0; i < arr.length; i++) {
            String yaml;
            yaml = arr[i].substring(arr[i].indexOf("{") + 1);
            a.add(yaml.split("}")[0]);
        }

        String[] ar = new String[a.size()];
        ar = a.toArray(ar);
        return ar;
    }

    private static String yaml(String yamlArgument) {
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

