package xyz.nkomarn.composter.protocol.packet.c2s;

import com.google.gson.internal.$Gson$Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

public class EncryptionResponseC2SPacket extends Packet<EncryptionResponseC2SPacket>{

    private int secretLen;
    private int tokenLen;
    private byte[] secret;
    private byte[] token;

    public EncryptionResponseC2SPacket() {
    }

    public EncryptionResponseC2SPacket(int secretLen, byte[] secret, int tokenLen, byte[] token) {
        this.secretLen = secretLen;
        this.secret = secret;
        this.tokenLen = tokenLen;
        this.token = token;
    }

    public int getSecretLen() {
        return secretLen;
    }

    public int getTokenLen() {
        return tokenLen;
    }

    public byte[] getSecret() {
        return secret;
    }

    public byte[] token() {
        return token;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    public EncryptionResponseC2SPacket decode(@NotNull ByteBuf buffer) {
        var secretLen = DataTypeUtils.readVarInt(buffer);
        byte[] secret = new byte[secretLen];
        buffer.readBytes(secret);

        var tokenLen = DataTypeUtils.readVarInt(buffer);
        byte[] token = new byte[tokenLen];
        buffer.readBytes(token);

        return new EncryptionResponseC2SPacket(secretLen, secret, tokenLen, token);
    }

}
