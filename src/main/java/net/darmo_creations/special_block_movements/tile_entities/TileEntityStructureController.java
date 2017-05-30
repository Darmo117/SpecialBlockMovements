package net.darmo_creations.special_block_movements.tile_entities;

import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.special_block_movements.entities.EntityMovingStructure;
import net.darmo_creations.special_block_movements.utils.TileEntityUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ITickable;

public abstract class TileEntityStructureController<T extends EntityMovingStructure> extends TileEntity implements ITickable {
  public static final int MAX_BLOCKS_NB = 300;

  private AxisDirection direction;
  private boolean powered, adjusting;
  private float speed;
  private int blocksCount;
  private T structure;
  private int structureId;

  public TileEntityStructureController() {
    this.powered = false;
    this.adjusting = false;
    this.speed = 1;
    this.structure = null;
    this.structureId = -1;
    this.direction = AxisDirection.POSITIVE;
  }

  public boolean isPowered() {
    return this.powered;
  }

  public boolean isAdjusting() {
    return this.adjusting;
  }

  protected void setAdjusting(boolean adjusting) {
    this.adjusting = adjusting;
  }

  /**
   * @return the speed of the structure (always positive)
   */
  public float getSpeed() {
    return this.speed;
  }

  /**
   * Sets structure's speed.
   * 
   * @param speed the new speed
   */
  public void setSpeed(float speed) {
    this.speed = Math.abs(speed);
  }

  /**
   * @return movement's direction
   */
  public AxisDirection getDirection() {
    return this.direction;
  }

  /**
   * Sets the direction.
   * 
   * @param direction the new direction
   */
  public void setDirection(AxisDirection direction) {
    this.direction = Objects.requireNonNull(direction);
  }

  /**
   * Inverts movement's direction.
   */
  public void inverseDirection() {
    this.direction = this.direction == AxisDirection.POSITIVE ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE;
  }

  /**
   * @return movement's direction offset (-1 or 1)
   */
  public int getDirectionOffset() {
    return this.direction.getOffset();
  }

  public void setPowered(boolean powered) {
    this.powered = powered;
    powerChanged();
    if (hasWorld()) {
      markDirty();
      TileEntityUtils.sendTileEntityUpdate(getWorld(), this);
    }
  }

  public abstract void powerChanged();

  protected int getBlocksCount() {
    return this.blocksCount;
  }

  protected void addBlocksCount() {
    this.blocksCount++;
  }

  protected void resetBlocksCount() {
    this.blocksCount = 0;
  }

  public Optional<T> getStructure() {
    return Optional.ofNullable(this.structure);
  }

  public void setStructure(T structure) {
    this.structure = structure;
  }

  protected int getStructureId() {
    return this.structureId;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setBoolean("Powered", this.powered);
    compound.setFloat("Speed", this.speed);
    compound.setInteger("Direction", this.direction.getOffset());
    compound.setBoolean("Adjusting", this.adjusting);
    compound.setInteger("StructureId", this.structure != null ? this.structure.getEntityId() : -1);

    return compound;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.powered = compound.getBoolean("Powered");
    this.speed = compound.getFloat("Speed");
    this.direction = compound.getInteger("Direction") == 1 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
    this.adjusting = compound.getBoolean("Adjusting");
    this.structureId = compound.getInteger("StructureId");

    if (hasWorld() && this.structureId != -1) {
      try {
        getWorld().getEntities(EntityMovingStructure.class, e -> e.getSaveId() == this.structureId + 1).stream().findFirst().ifPresent(
            e -> this.structure = (T) e);
      }
      catch (ClassCastException __) {}
      this.structureId = -1;
    }
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
