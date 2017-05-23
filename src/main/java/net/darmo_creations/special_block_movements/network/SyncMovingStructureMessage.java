package net.darmo_creations.special_block_movements.network;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.special_block_movements.entities.EntityMovingStructure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class SyncMovingStructureMessage implements IMessage {
  private int entityId;
  private Map<BlockPos, IBlockState> blocks;

  public SyncMovingStructureMessage() {
    this(new HashMap<>(), 0);
  }

  public SyncMovingStructureMessage(Map<BlockPos, IBlockState> blocks, int entityId) {
    this.blocks = blocks;
    this.entityId = entityId;
  }

  public int getEntityId() {
    return this.entityId;
  }

  public Map<BlockPos, IBlockState> getBlocks() {
    return this.blocks;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
    int size = buf.readInt();
    this.blocks = new HashMap<>();

    for (int i = 0; i < size; i++) {
      int idLength = buf.readInt();
      String blockName = new String(buf.readBytes(idLength).array());
      int meta = buf.readInt();
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      BlockPos pos = new BlockPos(x, y, z);
      @SuppressWarnings("deprecation")
      IBlockState state = Block.getBlockFromName(blockName).getStateFromMeta(meta);

      this.blocks.put(pos, state);
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
    buf.writeInt(this.blocks.size());
    for (Map.Entry<BlockPos, IBlockState> entry : this.blocks.entrySet()) {
      BlockPos pos = entry.getKey();
      IBlockState state = entry.getValue();
      ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(state.getBlock());
      String id = resourcelocation == null ? "" : resourcelocation.toString();
      int meta = state.getBlock().getMetaFromState(state);

      buf.writeInt(id.length());
      buf.writeBytes(id.getBytes());
      buf.writeInt(meta);
      buf.writeInt(pos.getX());
      buf.writeInt(pos.getY());
      buf.writeInt(pos.getZ());
    }
  }

  protected static abstract class AbstractClientHandler<T extends SyncMovingStructureMessage, U extends EntityMovingStructure>
      implements IMessageHandler<T, IMessage> {
    @Override
    public final IMessage onMessage(T message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        Entity e = NetworkUtils.getLocalWorld().getEntityByID(message.getEntityId());

        if (e != null && e.getClass() == getEntityClass()) {
          U structure = getEntityClass().cast(e);
          structure.setBlocks(message.getBlocks());
          onMessage(message, ctx, structure);
          structure.setSynced(true);
        }
      });

      return null;
    }

    protected abstract Class<U> getEntityClass();

    protected abstract void onMessage(T message, MessageContext ctx, U structure);
  }
}
