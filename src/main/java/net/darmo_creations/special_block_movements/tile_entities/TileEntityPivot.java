package net.darmo_creations.special_block_movements.tile_entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.darmo_creations.special_block_movements.blocks.BlockInsulated;
import net.darmo_creations.special_block_movements.blocks.BlockPivot;
import net.darmo_creations.special_block_movements.entities.EntityRotatingStructure;
import net.darmo_creations.special_block_movements.utils.TileEntityUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/*
 * #f:0
 * Planned options (in a GUI):
 * - change speed
 * - change rotation direcion
 * - change turning-off behavior (finish turn or go back)
 * #f:1
 */
public class TileEntityPivot extends TileEntityStructureController<EntityRotatingStructure> {
  private float angle;
  private boolean isClockwise;
  /** Turning-off behavior */
  private boolean endRotation;
  private boolean turning;

  public TileEntityPivot() {
    super();
    this.angle = 0;
    this.isClockwise = true;
    this.endRotation = false;
    this.turning = false;
  }

  public float getAngle() {
    return this.angle;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public boolean isClockwise() {
    return this.isClockwise;
  }

  public void setClockwise(boolean isClockwise) {
    this.isClockwise = isClockwise;
  }

  public boolean endsRotation() {
    return this.endRotation;
  }

  public void setEndRotation(boolean endRotation) {
    this.endRotation = endRotation;
  }

  public boolean isTurning() {
    return this.turning;
  }

  @Override
  public void powerChanged() {
    if (!isPowered() && (!this.isClockwise && this.angle % 360 > 1 || this.isClockwise && this.angle % 360 < -1) && !isAdjusting()) {
      if (!this.endRotation)
        inverseDirection();
      setAdjusting(true);
    }
    if (isPowered() && isAdjusting()) {
      if (!this.endRotation)
        inverseDirection();
      setAdjusting(false);
    }
  }

  @Override
  public void update() {
    @SuppressWarnings("deprecation")
    EnumFacing facing = getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockPivot.FACING);

    if (isPowered() || isAdjusting()) {
      if (!getStructure().isPresent() && getStructureId() == -1) {
        Map<BlockPos, IBlockState> blocks = new HashMap<>();
        BlockPos pos = getPos().offset(facing);

        if (!getWorld().isAirBlock(pos)) {
          resetBlocksCount();
          if (exploreBlocks(pos, pos, blocks)) {
            if (!getWorld().isRemote) {
              setStructure(new EntityRotatingStructure(getWorld(), blocks, facing, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
              getWorld().spawnEntity(getStructure().get());
              blocks.forEach((p, __) -> getWorld().setBlockState(pos.add(p), Blocks.AIR.getDefaultState(), 2));
            }
            this.turning = true;
            if (this.isClockwise && getDirectionOffset() == 1 || !this.isClockwise && getDirectionOffset() == -1)
              inverseDirection();
            markDirty();
            TileEntityUtils.sendTileEntityUpdate(getWorld(), this);
          }
        }
      }

      if (this.turning) {
        float actualSpeed = getSpeed() * getDirectionOffset();

        if (isAdjusting() && !this.endRotation && (!this.isClockwise && this.angle < 1 || this.isClockwise && this.angle > -1)
            || this.endRotation
                && (!this.isClockwise && this.angle + actualSpeed >= 360 || this.isClockwise && this.angle + actualSpeed <= -360)) {
          this.angle = 0;
          if (!this.endRotation)
            inverseDirection();
          setAdjusting(false);
        }
        else
          this.angle += actualSpeed;
        getStructure().ifPresent(s -> s.setAngle(this.angle));
      }
    }
    else if (getStructure().isPresent()) {
      // FIXME beware "double-blocks"
      while (!getStructure().get().getBlocks().isEmpty()) {
        for (Iterator<Map.Entry<BlockPos, IBlockState>> it = getStructure().get().getBlocks().entrySet().iterator(); it.hasNext();) {
          Map.Entry<BlockPos, IBlockState> entry = it.next();
          BlockPos pos = getPos().add(entry.getKey()).offset(facing);
          IBlockState state = entry.getValue();

          if (state.getBlock().canPlaceBlockAt(getWorld(), pos)) {
            getWorld().setBlockState(pos, state);
            it.remove();
          }
        }
        break; // TEMP
      }
      getStructure().get().setDead();
      setStructure(null);
      this.turning = false;
    }
  }

  private boolean exploreBlocks(BlockPos startPos, BlockPos currentPos, Map<BlockPos, IBlockState> blocks) {
    boolean ok = getBlocksCount() <= MAX_BLOCKS_NB;

    if (!ok)
      return false;

    blocks.put(currentPos.subtract(startPos), getWorld().getBlockState(currentPos));

    for (EnumFacing facing : EnumFacing.values()) {
      BlockPos nextPos = currentPos.offset(facing);
      BlockPos relPos = nextPos.subtract(startPos);
      IBlockState state = getWorld().getBlockState(nextPos);

      if (!blocks.containsKey(relPos) && !getWorld().isAirBlock(nextPos) && !(state.getBlock() instanceof ITileEntityProvider)
          && !(state.getBlock() instanceof BlockPivot) && !(state.getBlock() instanceof BlockInsulated)) {
        addBlocksCount();
        ok &= exploreBlocks(startPos, nextPos, blocks);
        if (!ok)
          break;
      }
    }

    return ok;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setFloat("Angle", this.angle);
    compound.setBoolean("Clockwise", this.isClockwise);
    compound.setBoolean("EndRotation", this.endRotation);
    compound.setBoolean("Turning", this.turning);

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    setAngle(compound.getFloat("Angle"));
    setClockwise(compound.getBoolean("Clockwise"));
    setEndRotation(compound.getBoolean("EndRotation"));
    this.turning = compound.getBoolean("Turning");
  }
}
