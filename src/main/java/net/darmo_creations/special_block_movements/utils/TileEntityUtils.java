package net.darmo_creations.special_block_movements.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * This class provides a method to sync tile entities.
 *
 * @author Damien Vergnet
 */
public final class TileEntityUtils {
  /**
   * Sends an update packet to the players.
   * 
   * @param world the world
   * @param te the tile entity
   */
  public static void sendTileEntityUpdate(World world, TileEntity te) {
    if (!world.isRemote) {
      for (EntityPlayer p : world.playerEntities) {
        if (p instanceof EntityPlayerMP)
          ((EntityPlayerMP) p).connection.sendPacket(te.getUpdatePacket());
      }
    }
  }

  private TileEntityUtils() {}
}
