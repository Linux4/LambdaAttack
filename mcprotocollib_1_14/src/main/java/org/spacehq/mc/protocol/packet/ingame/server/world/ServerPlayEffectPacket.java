package org.spacehq.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;
import org.spacehq.mc.protocol.data.game.world.block.BlockState;
import org.spacehq.mc.protocol.data.game.world.effect.BonemealGrowEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.BreakBlockEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.BreakPotionEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.ComposterEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.ParticleEffect;
import org.spacehq.mc.protocol.data.game.world.effect.RecordEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.SmokeEffectData;
import org.spacehq.mc.protocol.data.game.world.effect.SoundEffect;
import org.spacehq.mc.protocol.data.game.world.effect.WorldEffect;
import org.spacehq.mc.protocol.data.game.world.effect.WorldEffectData;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import org.spacehq.mc.protocol.util.NetUtil;

public class ServerPlayEffectPacket extends MinecraftPacket {
    private WorldEffect effect;
    private Position position;
    private WorldEffectData data;
    private boolean broadcast;

    @SuppressWarnings("unused")
    private ServerPlayEffectPacket() {
    }

    public ServerPlayEffectPacket(WorldEffect effect, Position position, WorldEffectData data) {
        this(effect, position, data, false);
    }

    public ServerPlayEffectPacket(WorldEffect effect, Position position, WorldEffectData data, boolean broadcast) {
        this.effect = effect;
        this.position = position;
        this.data = data;
        this.broadcast = broadcast;
    }

    public WorldEffect getEffect() {
        return this.effect;
    }

    public Position getPosition() {
        return this.position;
    }

    public WorldEffectData getData() {
        return this.data;
    }

    public boolean getBroadcast() {
        return this.broadcast;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.effect = MagicValues.key(WorldEffect.class, in.readInt());
        this.position = NetUtil.readPosition(in);
        int value = in.readInt();
        if(this.effect == SoundEffect.RECORD) {
            this.data = new RecordEffectData(value);
        } else if(this.effect == ParticleEffect.SMOKE) {
            this.data = MagicValues.key(SmokeEffectData.class, value % 9);
        } else if(this.effect == ParticleEffect.BREAK_BLOCK) {
            this.data = new BreakBlockEffectData(new BlockState(value));
        } else if(this.effect == ParticleEffect.BREAK_SPLASH_POTION) {
            this.data = new BreakPotionEffectData(value);
        } else if(this.effect == ParticleEffect.BONEMEAL_GROW) {
            this.data = new BonemealGrowEffectData(value);
        } else if(this.effect == ParticleEffect.COMPOSTER) {
            this.data = value > 0 ? ComposterEffectData.FILL_SUCCESS : ComposterEffectData.FILL;
        }

        this.broadcast = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(MagicValues.value(Integer.class, this.effect));
        NetUtil.writePosition(out, this.position);
        int value = 0;
        if(this.data instanceof RecordEffectData) {
            value = ((RecordEffectData) this.data).getRecordId();
        } else if(this.data instanceof SmokeEffectData) {
            value = MagicValues.value(Integer.class, (SmokeEffectData) this.data);
        } else if(this.data instanceof BreakBlockEffectData) {
            value = ((BreakBlockEffectData) this.data).getBlockState().getId();
        } else if(this.data instanceof BreakPotionEffectData) {
            value = ((BreakPotionEffectData) this.data).getPotionId();
        } else if(this.data instanceof BonemealGrowEffectData) {
            value = ((BonemealGrowEffectData) this.data).getParticleCount();
        } else if(this.data instanceof ComposterEffectData) {
            value = MagicValues.value(Integer.class, this.data);
        }

        out.writeInt(value);
        out.writeBoolean(this.broadcast);
    }
}
