package org.spacehq.mc.protocol.packet.ingame.client.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;
import org.spacehq.mc.protocol.data.game.entity.player.Hand;
import org.spacehq.mc.protocol.data.game.world.block.BlockFace;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import org.spacehq.mc.protocol.util.NetUtil;

public class ClientPlayerPlaceBlockPacket extends MinecraftPacket {

    private Position position;
    private BlockFace face;
    private Hand hand;
    private float cursorX;
    private float cursorY;
    private float cursorZ;
    private boolean insideBlock;

    @SuppressWarnings("unused")
    private ClientPlayerPlaceBlockPacket() {
    }

    public ClientPlayerPlaceBlockPacket(Position position, BlockFace face, Hand hand, float cursorX, float cursorY, float cursorZ, boolean insideBlock) {
        this.position = position;
        this.face = face;
        this.hand = hand;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
        this.insideBlock = insideBlock;
    }

    public Position getPosition() {
        return this.position;
    }

    public BlockFace getFace() {
        return this.face;
    }

    public Hand getHand() {
        return this.hand;
    }

    public float getCursorX() {
        return this.cursorX;
    }

    public float getCursorY() {
        return this.cursorY;
    }

    public float getCursorZ() {
        return this.cursorZ;
    }

    public boolean isInsideBlock() {
        return insideBlock;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.hand = MagicValues.key(Hand.class, in.readVarInt());
        this.position = NetUtil.readPosition(in);
        this.face = MagicValues.key(BlockFace.class, in.readVarInt());
        this.cursorX = in.readFloat();
        this.cursorY = in.readFloat();
        this.cursorZ = in.readFloat();
        this.insideBlock = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.hand));
        NetUtil.writePosition(out, this.position);
        out.writeVarInt(MagicValues.value(Integer.class, this.face));
        out.writeFloat(this.cursorX);
        out.writeFloat(this.cursorY);
        out.writeFloat(this.cursorZ);
        out.writeBoolean(this.insideBlock);
    }
}
