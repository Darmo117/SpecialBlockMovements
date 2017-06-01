package net.darmo_creations.special_block_movements.insulation;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.darmo_creations.special_block_movements.SpecialBlockMovements;
import net.darmo_creations.special_block_movements.insulation.InsulationHandler.InsulatedSides;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncInsulationPlatesMessage implements IMessage {
  private Map<BlockPos, InsulatedSides> plates;

  public SyncInsulationPlatesMessage() {
    this(null);
  }

  public SyncInsulationPlatesMessage(Map<BlockPos, InsulatedSides> plates) {
    this.plates = plates;
  }

  public Map<BlockPos, InsulatedSides> getPlates() {
    return this.plates;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.plates = new HashMap<>();
    int size = buf.readInt();

    for (int i = 0; i < size; i++) {
      BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.plates.put(pos, new InsulatedSides(buf.readByte()));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.plates.size());
    for (Map.Entry<BlockPos, InsulatedSides> entry : this.plates.entrySet()) {
      BlockPos pos = entry.getKey();

      buf.writeInt(pos.getX());
      buf.writeInt(pos.getY());
      buf.writeInt(pos.getZ());
      buf.writeByte(entry.getValue().getMask());
    }
  }

  public static class ClientHandler implements IMessageHandler<SyncInsulationPlatesMessage, IMessage> {
    @Override
    public IMessage onMessage(SyncInsulationPlatesMessage message, MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        InsulationHandler handler = SpecialBlockMovements.getInsulationHandler();

        handler.reset();
        for (Map.Entry<BlockPos, InsulatedSides> entry : message.getPlates().entrySet()) {
          for (EnumFacing side : EnumFacing.VALUES) {
            if (entry.getValue().hasSide(side))
              handler.addPlate(entry.getKey(), side);
          }
        }
      });

      return null;
    }
  }
}
