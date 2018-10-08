package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.Effect;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerEntityRemoveEffectPacket extends MinecraftPacket {
    private int entityId;
    private Effect effect;
    private int effectId;

    @SuppressWarnings("unused")
    private ServerEntityRemoveEffectPacket() {
    }

    public ServerEntityRemoveEffectPacket(int entityId, Effect effect) {
        this.entityId = entityId;
        this.effect = effect;
    }

    public ServerEntityRemoveEffectPacket(int entityId, int effectId) {
        this.entityId = entityId;
        this.effectId = effectId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getEffectId() {
        return this.effect == null ? this.effectId : MagicValues.value(Integer.class, this.effect);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effectId = in.readUnsignedByte();
        try {
            this.effect = MagicValues.key(Effect.class, this.effectId);
        } catch (IllegalArgumentException e) {
            this.effect = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(this.getEffectId());
    }
}
