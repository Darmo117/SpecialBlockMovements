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
  /** Turning-off behavior */
  private boolean endRotation;
  private boolean turning;

  public TileEntityPivot() {
    super();
    this.angle = 0;
    this.endRotation = false;
    this.turning = false;
  }

  public float getAngle() {
    return this.angle;
  }

  public void setAngle(float angle) {
    this.angle = angle;
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
    if (!this.powered && this.angle > 1 && !this.adjusting) {
      if (!this.endRotation)
        this.speed = -this.speed;
      this.adjusting = true;
    }
    if (this.powered && this.adjusting) {
      if (!this.endRotation)
        this.speed = -this.speed;
      this.adjusting = false;
    }
  }

  @Override
  public void update() {
    @SuppressWarnings("deprecation")
    EnumFacing facing = getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockPivot.FACING);

    if (isPowered() || this.adjusting) {
      if (this.structure == null && this.structureId == -1) {
        Map<BlockPos, IBlockState> blocks = new HashMap<>();
        BlockPos pos = getPos().offset(facing);

        if (!getWorld().isAirBlock(pos)) {
          this.blocksNb = 0;
          if (exploreBlocks(pos, pos, blocks)) {
            if (!getWorld().isRemote) {
              this.structure = new EntityRotatingStructure(getWorld(), blocks, facing, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
              getWorld().spawnEntity(this.structure);
              blocks.forEach((p, __) -> getWorld().setBlockState(pos.add(p), Blocks.AIR.getDefaultState(), 2));
            }
            this.turning = true;
            markDirty();
            TileEntityUtils.sendTileEntityUpdate(getWorld(), this);
          }
        }
      }

      if (this.turning) {
        if (this.adjusting && !this.endRotation && this.angle < 1 || this.endRotation && this.angle + this.speed >= 360) {
          this.angle = 0;
          if (!this.endRotation)
            this.speed = -this.speed;
          this.adjusting = false;
        }
        else
          this.angle = (this.angle + this.speed) % 360;
        if (this.structure != null)
          this.structure.setAngle(this.angle);
      }
    }
    else if (this.structure != null) {
      // FIXME beware "double-blocks"
      while (!this.structure.getBlocks().isEmpty()) {
        for (Iterator<Map.Entry<BlockPos, IBlockState>> it = this.structure.getBlocks().entrySet().iterator(); it.hasNext();) {
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
      this.structure.setDead();
      this.structure = null;
      this.turning = false;
    }
  }

  private boolean exploreBlocks(BlockPos startPos, BlockPos currentPos, Map<BlockPos, IBlockState> blocks) {
    boolean ok = this.blocksNb <= MAX_BLOCKS_NB;

    if (!ok)
      return false;

    blocks.put(currentPos.subtract(startPos), getWorld().getBlockState(currentPos));

    for (EnumFacing facing : EnumFacing.values()) {
      BlockPos nextPos = currentPos.offset(facing);
      BlockPos relPos = nextPos.subtract(startPos);
      IBlockState state = getWorld().getBlockState(nextPos);

      if (!blocks.containsKey(relPos) && !getWorld().isAirBlock(nextPos) && !(state.getBlock() instanceof ITileEntityProvider)
          && !(state.getBlock() instanceof BlockPivot) && !(state.getBlock() instanceof BlockInsulated)) {
        this.blocksNb++;
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
    compound.setBoolean("EndRotation", this.endRotation);
    compound.setBoolean("Turning", this.turning);

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.angle = compound.getFloat("Angle");
    this.endRotation = compound.getBoolean("EndRotation");
    this.turning = compound.getBoolean("Turning");
  }
}
