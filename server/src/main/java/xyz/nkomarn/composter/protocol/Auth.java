package xyz.nkomarn.composter.protocol;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mojang.authlib.GameProfile;
import xyz.nkomarn.composter.protocol.objects.EncryptionResponseObject;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.net.InetAddress;

//TODO Figure out a better way to Authenticate user

public class Auth {

    private JSONObject parse(InputStream response) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject)jsonParser.parse(new InputStreamReader(response, StandardCharsets.UTF_8));
    }

    private static String formatUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public EncryptionResponseObject clientAuth(String hash, String username) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String format = String.format("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s", username, hash);
            URL mojang = new URL(format);

            HttpURLConnection connection = (HttpURLConnection) mojang.openConnection();
            connection.setRequestMethod("GET");

            var bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), EncryptionResponseObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}