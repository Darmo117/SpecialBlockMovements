package net.darmo_creations.special_block_movements.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/*
 * #f:0
 * Planned functionnalities:
 * - change displayed block
 * #f:1
 */
public class TileEntityInsulated extends TileEntity {
  private IBlockState displayedBlock;

  public TileEntityInsulated() {
    // TEMP
    this.displayedBlock = Blocks.STONE.getDefaultState();
  }

  public IBlockState getDisplayedBlock() {
    return this.displayedBlock;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.displayedBlock.getBlock());
    compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
    compound.setByte("Data", (byte) this.displayedBlock.getBlock().getMetaFromState(this.displayedBlock));

    return compound;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    int meta = compound.getByte("Data") & 255;
    String resource = compound.getString("Block");
    this.displayedBlock = Block.getBlockFromName(resource).getStateFromMeta(meta);
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }
}
