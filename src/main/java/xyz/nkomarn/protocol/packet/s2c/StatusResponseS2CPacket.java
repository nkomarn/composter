package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class StatusResponseS2CPacket extends Packet<StatusResponseS2CPacket> {

    public StatusResponseS2CPacket() {
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    @NotNull
    public ByteBuf encode() {
        String response = "{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"1.8.7\",\n" +
                "        \"protocol\": 47\n" +
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
                "        \"text\": \"Hello world\"\n" +
                "    },\n" +
                "    \"favicon\": \"data:image/png;base64,<data>\"\n" +
                "}";
        var buffer = Unpooled.buffer();
        ByteBufUtil.writeString(response, buffer);
        return buffer;
    }
}
