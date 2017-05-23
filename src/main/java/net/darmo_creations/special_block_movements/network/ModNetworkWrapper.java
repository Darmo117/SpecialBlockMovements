package net.darmo_creations.special_block_movements.network;

import net.darmo_creations.special_block_movements.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This class is this mod's packet handler.
 *
 * @author Damien Vergnet
 */
public final class ModNetworkWrapper {
  private static SimpleNetworkWrapper theWrapper;
  /** next's packet ID */
  private static int id = 0;

  public static SimpleNetworkWrapper getModWapper() {
    if (theWrapper == null)
      theWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MOD_ID);
    return theWrapper;
  }

  /**
   * Registers a packet type.
   * 
   * @param handlerClass the handler
   * @param packetClass the packet's class
   * @param side the concerned side
   */
  public static <T extends IMessage, U extends IMessage> void registerPacket(Class<? extends IMessageHandler<T, U>> handlerClass,
      Class<T> packetClass, Side side) {
    getModWapper().registerMessage(handlerClass, packetClass, id++, side);
  }

  private ModNetworkWrapper() {}
}
