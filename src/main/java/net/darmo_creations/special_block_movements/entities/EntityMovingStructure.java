package net.darmo_creations.special_block_movements.entities;

import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.special_block_movements.network.ModNetworkWrapper;
import net.darmo_creations.special_block_movements.network.SyncMovingStructureMessage;
import net.darmo_creations.special_block_movements.utils.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityMovingStructure extends Entity {
  private boolean synced;
  private int saveId;
  protected Map<BlockPos, IBlockState> blocks;

  public EntityMovingStructure(World world) {
    this(world, new HashMap<>(), 0, 0, 0);
  }

  public EntityMovingStructure(World world, Map<BlockPos, IBlockState> blocks, double x, double y, double z) {
    super(world);
    this.synced = false;
    this.blocks = blocks;
    setPosition(x, y, z);
    setSize(1, 1);
    setSaveId(getEntityId());
  }

  public int getSaveId() {
    return this.saveId;
  }

  public void setSaveId(int saveId) {
    this.saveId = saveId;
  }

  public Map<BlockPos, IBlockState> getBlocks() {
    return this.blocks;
  }

  public void setBlocks(Map<BlockPos, IBlockState> blocks) {
    this.blocks = blocks;
  }

  public void setSynced(boolean synced) {
    this.synced = synced;
  }

  @Override
  protected void entityInit() {}

  @Override
  public void onEntityUpdate() {
    super.onEntityUpdate();
    if (!getEntityWorld().isRemote && !this.synced) {
      ModNetworkWrapper.getModWapper().sendToAll(getSyncPacket());
      this.synced = true;
    }
  }

  public abstract SyncMovingStructureMessage getSyncPacket();

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    NBTTagList blocks = compound.getTagList("Blocks", NBTUtils.TAG_COMPOUND);

    this.blocks = new HashMap<>();
    for (int i = 0; i < blocks.tagCount(); i++) {
      NBTTagCompound block = blocks.getCompoundTagAt(i);
      int meta = block.getByte("Data") & 255;
      String resource = block.getString("Block");
      @SuppressWarnings("deprecation")
      IBlockState state = Block.getBlockFromName(resource).getStateFromMeta(meta);
      int x = block.getInteger("X");
      int y = block.getInteger("Y");
      int z = block.getInteger("Z");
      BlockPos pos = new BlockPos(x, y, z);

      this.blocks.put(pos, state);
    }
    setSaveId(compound.getInteger("SaveId"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    NBTTagList blocks = new NBTTagList();

    for (Map.Entry<BlockPos, IBlockState> entry : this.blocks.entrySet()) {
      IBlockState state = entry.getValue();
      BlockPos pos = entry.getKey();
      NBTTagCompound block = new NBTTagCompound();
      ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(state.getBlock());
      block.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
      block.setByte("Data", (byte) state.getBlock().getMetaFromState(state));
      block.setInteger("X", pos.getX());
      block.setInteger("Y", pos.getY());
      block.setInteger("Z", pos.getZ());
      blocks.appendTag(block);
    }
    compound.setTag("Blocks", blocks);
    compound.setInteger("SaveId", getSaveId());
  }
}
