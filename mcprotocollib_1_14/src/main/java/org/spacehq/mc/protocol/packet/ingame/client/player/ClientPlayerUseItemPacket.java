package org.spacehq.mc.protocol.packet.ingame.client.player;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.player.Hand;
import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class ClientPlayerUseItemPacket extends MinecraftPacket {
    private Hand hand;

    @SuppressWarnings("unused")
    private ClientPlayerUseItemPacket() {
    }

    public ClientPlayerUseItemPacket(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return this.hand;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.hand = MagicValues.key(Hand.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.hand));
    }
}
