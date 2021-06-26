package xyz.nkomarn.composter.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

public class EncryptionRequestS2CPacket extends Packet<EncryptionRequestS2CPacket>{

    private String serverID;
    private int publicKeyLength;
    private int tokenLength;
    private byte[] publicKey;
    private byte[] token;

    public EncryptionRequestS2CPacket() {
    }

    public EncryptionRequestS2CPacket(@NotNull String serverID, int publicKeyLength, byte[] publicKey, int tokenLength, byte[] token) {
        this.serverID = serverID;
        this.publicKeyLength = publicKeyLength;
        this.publicKey = publicKey;
        this.tokenLength = tokenLength;
        this.token = token;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    @NotNull
    public ByteBuf encode() {
        var buffer = Unpooled.buffer();
        DataTypeUtils.writeString(buffer, serverID);
        DataTypeUtils.writeVarInt(buffer, publicKeyLength);
        buffer.writeBytes(publicKey);
        DataTypeUtils.writeVarInt(buffer, tokenLength);
        buffer.writeBytes(token);
        return buffer;
    }

}
