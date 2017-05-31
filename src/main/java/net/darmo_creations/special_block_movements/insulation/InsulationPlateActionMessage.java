package net.darmo_creations.special_block_movements.insulation;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.special_block_movements.SpecialBlockMovements;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InsulationPlateActionMessage implements IMessage {
  private BlockPos pos;
  private EnumFacing side;
  private boolean add;

  public InsulationPlateActionMessage() {
    this(null, null, false);
  }

  public InsulationPlateActionMessage(BlockPos pos, EnumFacing side, boolean add) {
    this.pos = pos;
    this.side = side;
    this.add = add;
  }

  public BlockPos getPosition() {
    return this.pos;
  }

  public EnumFacing getSide() {
    return this.side;
  }

  public boolean isAdd() {
    return this.add;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    this.side = EnumFacing.getFront(buf.readInt());
    this.add = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.pos.getX());
    buf.writeInt(this.pos.getY());
    buf.writeInt(this.pos.getZ());
    buf.writeInt(this.side.getIndex());
    buf.writeBoolean(this.add);
  }

  public static class ServerHandler implements IMessageHandler<InsulationPlateActionMessage, IMessage> {
    @Override
    public IMessage onMessage(InsulationPlateActionMessage message, MessageContext ctx) {
      InsulationHandler handler = SpecialBlockMovements.getInsulationHandler();

      if (message.isAdd()) {
        handler.addPlate(message.getPosition(), message.getSide());
      }
      else {
        handler.removePlate(message.getPosition(), message.getSide());
      }

      return null;
    }
  }
}
