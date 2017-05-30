package net.darmo_creations.special_block_movements.network;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityPivot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncPivotMessage implements IMessage {
  private BlockPos pos;
  private float speed;
  private boolean isClockwise;
  private boolean endRotation;

  public SyncPivotMessage() {
    this(null, 0, true, true);
  }

  public SyncPivotMessage(BlockPos pos, float speed, boolean isClockwise, boolean endRotation) {
    this.pos = pos;
    this.speed = speed;
    this.isClockwise = isClockwise;
    this.endRotation = endRotation;
  }

  public BlockPos getPos() {
    return this.pos;
  }

  public float getSpeed() {
    return this.speed;
  }

  public boolean isClockwise() {
    return this.isClockwise;
  }

  public boolean endsRotation() {
    return this.endRotation;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    this.speed = buf.readFloat();
    this.isClockwise = buf.readBoolean();
    this.endRotation = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(getPos().getX());
    buf.writeInt(getPos().getY());
    buf.writeInt(getPos().getZ());
    buf.writeFloat(getSpeed());
    buf.writeBoolean(isClockwise());
    buf.writeBoolean(endsRotation());
  }

  public static class ServerHandler implements IMessageHandler<SyncPivotMessage, IMessage> {
    @Override
    public IMessage onMessage(SyncPivotMessage message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().playerEntity;

      player.getServerWorld().addScheduledTask(() -> {
        TileEntity te = player.getServerWorld().getTileEntity(message.getPos());

        if (te instanceof TileEntityPivot) {
          TileEntityPivot pivot = (TileEntityPivot) te;

          pivot.setSpeed(message.getSpeed());
          pivot.setClockwise(message.isClockwise());
          pivot.setEndRotation(message.endsRotation());

          ModNetworkWrapper.getModWapper().sendTo(message, player);
        }
      });

      return null;
    }
  }

  public static class ClientHandler implements IMessageHandler<SyncPivotMessage, IMessage> {
    @Override
    public IMessage onMessage(SyncPivotMessage message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        TileEntity te = NetworkUtils.getLocalWorld().getTileEntity(message.getPos());

        if (te instanceof TileEntityPivot) {
          TileEntityPivot pivot = (TileEntityPivot) te;

          pivot.setSpeed(message.getSpeed());
          pivot.setClockwise(message.isClockwise());
          pivot.setEndRotation(message.endsRotation());
        }
      });

      return null;
    }
  }
}
