package net.darmo_creations.special_block_movements.tile_entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.darmo_creations.special_block_movements.ModBlocks;
import net.darmo_creations.special_block_movements.blocks.BlockPivot;
import net.darmo_creations.special_block_movements.blocks.BlockSlider;
import net.darmo_creations.special_block_movements.entities.EntitySlidingStructure;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntitySlider extends TileEntityStructureController<EntitySlidingStructure> {
  private float offset;
  private BlockPos plusPos, minusPos;

  public TileEntitySlider() {
    super();
    this.offset = 0;
  }

  @Override
  public void powerChanged() {
    if (!isPowered() && this.offset > 1 && !isAdjusting()) {
      inverseDirection();
      setAdjusting(true);
    }
    if (isPowered() && isAdjusting()) {
      inverseDirection();
      setAdjusting(false);
    }
  }

  public float getOffset() {
    return this.offset;
  }

  @Override
  public void update() {
    @SuppressWarnings("deprecation")
    EnumFacing facing = getBlockType().getStateFromMeta(getBlockMetadata()).getValue(BlockPivot.FACING);

    if (isPowered() || isAdjusting()) {
      if (!getStructure().isPresent()) {
        Map<BlockPos, IBlockState> blocks = new HashMap<>();
        BlockPos pos = getPos().offset(facing);

        if (!getWorld().isAirBlock(pos)) {
          resetBlocksCount();
          this.plusPos = null;
          this.minusPos = null;
          if (exploreBlocks(pos, pos, blocks)) {
            setStructure(new EntitySlidingStructure(getWorld(), blocks, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
            getWorld().spawnEntity(getStructure().get());
            blocks.forEach((p, __) -> getWorld().setBlockToAir(pos.add(p)));
          }
        }
      }

      if (getStructure() != null) {
        float actualSpeed = getSpeed() * getDirectionOffset();

        if (isAdjusting() && this.offset < 1 || this.offset >= getStructure().get().getLength()) {
          this.offset = this.offset < 1 ? 0 : getStructure().get().getLength();
          inverseDirection();
          setAdjusting(false);
        }
        else
          this.offset += actualSpeed;
        getStructure().get().setOffset(this.offset);
      }
    }
    else if (getStructure() != null) {
      // TODO place blocks in the new configuration if different
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
      }
      getStructure().get().setDead();
      setStructure(null);
    }
  }

  // TODO check a + block and a - block are there (in front of each other)
  private boolean exploreBlocks(BlockPos startPos, BlockPos currentPos, Map<BlockPos, IBlockState> blocks) {
    boolean ok = getBlocksCount() <= MAX_BLOCKS_NB;

    if (!ok)
      return false;

    IBlockState state = getWorld().getBlockState(currentPos);
    blocks.put(currentPos.subtract(startPos), state);
    if (state.getBlock() == ModBlocks.SLIDER_END_PLUS) {
      this.plusPos = currentPos;
    }
    else if (state.getBlock() == ModBlocks.SLIDER_END_MINUS) {
      this.minusPos = currentPos;
    }

    if (this.plusPos != null && this.minusPos != null) {
      boolean diffX = this.plusPos.getX() != this.minusPos.getX();
      boolean diffY = this.plusPos.getY() != this.minusPos.getY();
      boolean diffZ = this.plusPos.getZ() != this.minusPos.getZ();
      if (diffX && diffY || diffX && diffZ || diffY && diffZ)
        return false;
    }

    for (EnumFacing facing : EnumFacing.values()) {
      BlockPos nextPos = currentPos.offset(facing);
      BlockPos relPos = nextPos.subtract(startPos);
      IBlockState state1 = getWorld().getBlockState(nextPos);

      if (!blocks.containsKey(relPos) && !getWorld().isAirBlock(nextPos) && !(state1.getBlock() instanceof ITileEntityProvider)
          && !(state1.getBlock() instanceof BlockSlider)) {
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
    compound.setFloat("Offset", this.offset);

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.offset = compound.getFloat("Offset");
  }
}
