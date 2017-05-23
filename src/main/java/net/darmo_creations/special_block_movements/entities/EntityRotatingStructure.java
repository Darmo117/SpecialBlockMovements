package net.darmo_creations.special_block_movements.entities;

import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.special_block_movements.network.SyncMovingStructureMessage;
import net.darmo_creations.special_block_movements.network.SyncRotatingStructureMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRotatingStructure extends EntityMovingStructure {
  private float angle;
  private EnumFacing facing;

  public EntityRotatingStructure(World world) {
    this(world, new HashMap<>(), EnumFacing.UP, 0, 0, 0);
  }

  public EntityRotatingStructure(World world, Map<BlockPos, IBlockState> blocks, EnumFacing facing, double x, double y, double z) {
    super(world, blocks, x, y, z);
    this.angle = 0;
    this.facing = facing;
  }

  public float getAngle() {
    return this.angle;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public EnumFacing getFacing() {
    return this.facing;
  }

  public void setFacing(EnumFacing facing) {
    this.facing = facing;
  }

  @Override
  public SyncMovingStructureMessage getSyncPacket() {
    return new SyncRotatingStructureMessage(getBlocks(), getEntityId(), getAngle(), getFacing());
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.angle = compound.getFloat("Angle");
    this.facing = EnumFacing.getFront(compound.getInteger("Facing"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setFloat("Angle", this.angle);
    compound.setInteger("Facing", this.facing.getIndex());
  }
}
