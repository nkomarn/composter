package xyz.nkomarn.util.configuration;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

// TODO this file is an absolute circlejerk and needs to be rewritten to be less shit
public class Config {

    /*Call the correct methods to get the correct yaml result*/

    public String getString(String path) {
        return yamlResult(path);
    }

    public Boolean getBoolean(@NotNull String path) {
        String get = yamlResult(path);
        return Boolean.parseBoolean(get);
    }

    public int getInteger(String path) {
        String get = yamlResult(path);
        if (isNumeric(get)) {
            return Integer.parseInt(get);
        } else {
            return 0; // log error
        }
    }

    public Double getDouble(String path) throws Exception {
        String get = yamlResult(path);
        if (isNumeric(get)) {
            return Double.parseDouble(get);
        } else {
            throw new Exception("Not an integer");
        }
    }

    public Float getFloat(String path) {
        String get = yamlResult(path);
        return Float.parseFloat(get);
    }

    public ArrayList<String> getList(String path) {
        ArrayList<String> ar = new ArrayList<String>();
        ar.add(path);
        return ar;
    }


    /*The methods below are slaves*/

    private static boolean isNumeric(String value) {
        try {
            double d = Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private static String yamlResult(String arg) {
        String[] yArr;
        String target = stringSplit(arg, 0, true);
        String[] split = arg.split("\\.");
        split = Arrays.copyOfRange(split, 1, split.length);
        String parse = stringSplit(arg, 0, false);

        String yaml = yaml(parse);
        yArr = makeArray(yaml);

        String result = "";
        for (String value : yArr) {
            for (String s : split) {
                if (value.contains(s) && value.contains(target + "=")) {

                    String[] ar;

                    String separate = value.split("}")[0];
                    ar = separate.split("\\{");
                    String res = ar[ar.length - 1];

                    result = res.substring(res.indexOf("=") + 1);

                }
            }
        }

        return result;
    }

    private static String stringSplit(String arg, int index, boolean lastIndex) {
        String[] split = arg.split("\\.");
        if (lastIndex) {
            return split[split.length - 1];
        } else {
            return split[index];
        }
    }

    private static String[] makeArray(String arg) {
        return arg.split(",");
    }

    private static String yaml(String yamlArgument) {
        final String filePath = "src/main/resources/composter.yml";
        ArrayList<String> value = new ArrayList<String>();
        Yaml yaml = new Yaml();

        try {
            Reader yamlFile = new FileReader(filePath);
            Map<String, Object> yamlMaps = yaml.load(yamlFile);
            value.add(yamlMaps.get(yamlArgument).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return " " + value;
    }

}

