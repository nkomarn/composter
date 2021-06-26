package xyz.nkomarn.composter.protocol;

import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientAuth {

    private JSONObject parse(InputStream response) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject)jsonParser.parse(new InputStreamReader(response, StandardCharsets.UTF_8));
    }

    // TODO Find a better way to get UUID
    public UUID getUUID(String username) {
        try {
            URL mojang = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) mojang.openConnection();
            connection.setRequestMethod("GET");

            var response = parse(connection.getInputStream());

            String s = (String) response.get("id");
            String s2 = s.replace("-", "");
            return new UUID(
                    new BigInteger(s2.substring(0, 16), 16).longValue(),
                    new BigInteger(s2.substring(16), 16).longValue());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
