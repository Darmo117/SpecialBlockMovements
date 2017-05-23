package net.darmo_creations.special_block_movements.network;

import net.darmo_creations.special_block_movements.utils.CustomGuiScreens;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * This class handles custom GUIs.
 * 
 * @author Damien Vergnet
 */
public class GuiHandler implements IGuiHandler {
  @SuppressWarnings("unused")
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity tileEntity = world.getTileEntity(pos);

    switch (CustomGuiScreens.values()[id]) {
      // empty for now
    }

    return null;
  }

  @SuppressWarnings("unused")
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity tileEntity = world.getTileEntity(pos);

    switch (CustomGuiScreens.values()[id]) {
      // empty for now
    }

    return null;
  }
}