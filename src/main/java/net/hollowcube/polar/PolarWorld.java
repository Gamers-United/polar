package net.hollowcube.polar;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minestom.server.utils.chunk.ChunkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A Java type representing the latest version of the world format.
 */
@SuppressWarnings("UnstableApiUsage")
public class PolarWorld {
    public static final int MAGIC_NUMBER = 0x506F6C72;
    public static final byte VERSION_MAJOR = 1;
    public static final byte VERSION_MINOR = 0;

    // Polar metadata
    private byte major;
    private byte minor;
    private CompressionType compression;

    // World metadata
    private final int minSection;
    private final int maxSection;

    // Chunk data
    private final Long2ObjectMap<PolarChunk> chunks = new Long2ObjectOpenHashMap<>();

    public PolarWorld(
            byte major, byte minor,
            @NotNull CompressionType compression,
            int minSection, int maxSection,
            @NotNull List<PolarChunk> chunks
    ) {
        this.major = major;
        this.minor = minor;
        this.compression = compression;

        this.minSection = minSection;
        this.maxSection = maxSection;

        for (var chunk : chunks) {
            var index = ChunkUtils.getChunkIndex(chunk.x(), chunk.z());
            this.chunks.put(index, chunk);
        }
    }

    public @Nullable PolarChunk chunkAt(int x, int z) {
        return chunks.getOrDefault(ChunkUtils.getChunkIndex(x, z), null);
    }

    public enum CompressionType {
        NONE,
        ZSTD;

        private static final CompressionType[] VALUES = values();

        public static @Nullable CompressionType fromId(int id) {
            if (id < 0 || id >= VALUES.length) return null;
            return VALUES[id];
        }
    }
}