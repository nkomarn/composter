package xyz.nkomarn.composter.protocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mojang.authlib.GameProfile;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

//TODO Figure out a better way to Authenticate user

public class Auth {

    private JSONObject parse(InputStream response) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject)jsonParser.parse(new InputStreamReader(response, StandardCharsets.UTF_8));
    }

    private static String formatUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public void clientAuth(byte[] token, UUID uuid, String hash, String username) {
        try {
            GameProfile gameProfile = new GameProfile(uuid, username); // Maybe we can use this
            URL mojang = new URL("https://sessionserver.mojang.com/session/minecraft/join");
            HttpURLConnection connection = (HttpURLConnection) mojang.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Very shitty code as you can tell, but we can probably fine a better way through here https://wiki.vg/Lastlogin

            String jsonInputString = "{\"accessToken\": \"" + Arrays.toString(token) + "\", \"selectedProfile\": \"" + formatUUID(uuid) + "\", \"serverId\": \"" + hash + "\"}";
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
