package net.darmo_creations.special_block_movements.network;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.special_block_movements.entities.EntityRotatingStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncRotatingStructureMessage extends SyncMovingStructureMessage {
  private float angle; // TODO remove
  private EnumFacing facing;

  public SyncRotatingStructureMessage() {
    this(new HashMap<>(), 0, 0, EnumFacing.NORTH);
  }

  public SyncRotatingStructureMessage(Map<BlockPos, IBlockState> blocks, int entityId, float angle, EnumFacing facing) {
    super(blocks, entityId);
    this.angle = angle;
    this.facing = facing;
  }

  public float getAngle() {
    return this.angle;
  }

  public EnumFacing getFacing() {
    return this.facing;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.facing = EnumFacing.getFront(buf.readInt());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(getFacing().getIndex());
  }

  public static class ClientHandler extends AbstractClientHandler<SyncRotatingStructureMessage, EntityRotatingStructure> {
    @Override
    protected void onMessage(SyncRotatingStructureMessage message, MessageContext ctx, EntityRotatingStructure structure) {
      structure.setFacing(message.getFacing());
    }

    @Override
    protected Class<EntityRotatingStructure> getEntityClass() {
      return EntityRotatingStructure.class;
    }
  }
}
