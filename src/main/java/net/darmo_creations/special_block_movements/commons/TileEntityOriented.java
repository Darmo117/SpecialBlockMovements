package net.darmo_creations.special_block_movements.commons;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * This tile entity knows the orientation of its associated block.
 * 
 * @author Damien Vergnet
 */
public abstract class TileEntityOriented extends TileEntity {
  /** The orientation */
  private EnumFacing facing;

  /**
   * Creates a new tile entity pointing towards north.
   */
  public TileEntityOriented() {
    this(EnumFacing.NORTH);
  }

  /**
   * Creates a new tile entity with the given orientation.
   */
  public TileEntityOriented(EnumFacing facing) {
    this.facing = facing;
  }

  public EnumFacing getFacing() {
    return this.facing;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.facing = EnumFacing.getHorizontal(compound.getInteger("Facing"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger("Facing", getFacing().getHorizontalIndex());
    return compound;
  }

  @Override
  public final SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public final NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }
}
