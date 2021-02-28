package xyz.nkomarn.composter.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

public class StatusResponseS2CPacket extends Packet<StatusResponseS2CPacket> {

    public StatusResponseS2CPacket() {
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    @NotNull
    public ByteBuf encode() { // TODO yes this is dumb, properly encode the correct data later
        String response = "{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"1.16.4\",\n" +
                "        \"protocol\": 754\n" +
                "    },\n" +
                "    \"players\": {\n" +
                "        \"max\": 100,\n" +
                "        \"online\": 5,\n" +
                "        \"sample\": [\n" +
                "            {\n" +
                "                \"name\": \"thinkofdeath\",\n" +
                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"description\": {\n" +
                "        \"text\": \"" + "§a§lComposter - 1.16.4!\n§eNow with modern protocol support." + "\"\n" +
                "    },\n" +
                "    \"favicon\": \"data:image/png;base64,<data>\"\n" +
                "}";
        var buffer = Unpooled.buffer();
        DataTypeUtils.writeString(buffer, response);
        return buffer;
    }
}
