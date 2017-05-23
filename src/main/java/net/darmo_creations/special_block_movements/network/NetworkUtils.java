package net.darmo_creations.special_block_movements.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class provides methods to handle packets.
 *
 * @author Damien Vergnet
 */
public final class NetworkUtils {
  @SideOnly(Side.CLIENT)
  public static EntityPlayer getLocalPlayer() {
    return Minecraft.getMinecraft().player;
  }

  @SideOnly(Side.CLIENT)
  public static World getLocalWorld() {
    return Minecraft.getMinecraft().player.getEntityWorld();
  }

  private NetworkUtils() {}
}
