package xyz.nkomarn.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class YamlOptions {

    /*Call the correct methods to get the correct yaml result*/
    public String getString(String arg) {
        return yamlResult(arg);
    }

    public Boolean getBoolean(String arg) throws Exception{
        String get = yamlResult(arg);
        if(get.equals("true") || get.equals("false"))
        {
            return Boolean.parseBoolean(get);
        }else{
            throw new Exception("Not a Boolean");
        }
    }

    public int getInt(String arg) throws Exception {
        String get = yamlResult(arg);
        if(isNumeric(get)){
            return Integer.parseInt(get);
        }
        else{
            throw new Exception("Not an integer");
        }
    }

    public Double getDouble(String arg) throws Exception {
        String get = yamlResult(arg);
        if(isNumeric(get)){
            return Double.parseDouble(get);
        }
        else{
            throw new Exception("Not an integer");
        }
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


    /*The methods below are slaves*/
    private static boolean isNumeric(String value){
        try{
            double d = Double.parseDouble(value);
        }catch(NumberFormatException | NullPointerException nfe){
            return false;
        }
        return true;
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

