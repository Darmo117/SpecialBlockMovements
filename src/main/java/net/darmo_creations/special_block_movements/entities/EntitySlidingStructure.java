package net.darmo_creations.special_block_movements.entities;

import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.special_block_movements.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySlidingStructure extends EntityMovingStructure {
  private int length;
  private EnumFacing.Axis axis;
  private EnumFacing.AxisDirection direction;
  private BlockPos startPos;

  public EntitySlidingStructure(World world) {
    this(world, new HashMap<>(), 0, 0, 0);
  }

  public EntitySlidingStructure(World world, Map<BlockPos, IBlockState> blocks, double x, double y, double z) {
    super(world, blocks, x, y, z);
    setLengthAndAxis(blocks);
    this.startPos = new BlockPos(x, y, z);
  }

  /**
   * 
   * @param blocks les blocs, la structure est supposée cohérente (présence d'un seul bloc + et d'un
   *          seul bloc - et sur une même ligne)
   */
  private void setLengthAndAxis(Map<BlockPos, IBlockState> blocks) {
    BlockPos plusPos = null;
    BlockPos minusPos = null;

    for (Map.Entry<BlockPos, IBlockState> entry : blocks.entrySet()) {
      if (entry.getValue().getBlock() == ModBlocks.SLIDER_END_PLUS) {
        plusPos = entry.getKey();
      }
      if (entry.getValue().getBlock() == ModBlocks.SLIDER_END_MINUS) {
        minusPos = entry.getKey();
      }
      if (plusPos != null && minusPos != null)
        break;
    }

    boolean sameX = plusPos.getX() != minusPos.getX();
    boolean sameY = plusPos.getY() != minusPos.getY();
    boolean sameZ = plusPos.getZ() != minusPos.getZ();
    int l = 0;

    // FIXME revoir le calcul de la direction
    if (sameX) {
      l = plusPos.getX() - minusPos.getX();
      this.length = Math.abs(l);
      this.axis = EnumFacing.Axis.X;
    }
    else if (sameY) {
      l = plusPos.getY() - minusPos.getY();
      this.length = Math.abs(l);
      this.axis = EnumFacing.Axis.Y;
    }
    else if (sameZ) {
      l = plusPos.getZ() - minusPos.getZ();
      this.length = Math.abs(l);
      this.axis = EnumFacing.Axis.Z;
    }
    this.direction = l < 0 ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
  }

  public void setOffset(float offset) {
    switch (this.axis) {
      case X:
        setPosition(this.startPos.getX() + offset * this.direction.getOffset(), this.startPos.getY(), this.startPos.getZ());
        break;
      case Y:
        setPosition(this.startPos.getX(), this.startPos.getY() + offset * this.direction.getOffset(), this.startPos.getZ());
        break;
      case Z:
        setPosition(this.startPos.getX(), this.startPos.getY(), this.startPos.getZ() + offset * this.direction.getOffset());
        break;
    }
  }

  public int getLength() {
    return this.length;
  }

  public EnumFacing.Axis getAxis() {
    return this.axis;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.length = compound.getInteger("Length");
    this.axis = EnumFacing.Axis.values()[compound.getInteger("Axis")];
    int x = compound.getInteger("startX");
    int y = compound.getInteger("startY");
    int z = compound.getInteger("startZ");
    this.startPos = new BlockPos(x, y, z);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("Length", this.length);
    compound.setInteger("Axis", this.axis.ordinal());
    compound.setInteger("startX", this.startPos.getX());
    compound.setInteger("startY", this.startPos.getY());
    compound.setInteger("startZ", this.startPos.getZ());
  }
}
