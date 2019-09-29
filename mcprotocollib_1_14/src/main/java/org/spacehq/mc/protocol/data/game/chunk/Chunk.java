package org.spacehq.mc.protocol.data.game.chunk;

import java.util.Objects;

import org.spacehq.mc.protocol.util.ObjectUtil;

public class Chunk {
    private BlockStorage blocks;

    public Chunk(boolean skylight) {
        this(new BlockStorage());
    }

    public Chunk(BlockStorage blocks) {
        this.blocks = blocks;
    }

    public BlockStorage getBlocks() {
        return this.blocks;
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Chunk)) return false;

        Chunk that = (Chunk) o;
        return Objects.equals(this.blocks, that.blocks);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.blocks);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
