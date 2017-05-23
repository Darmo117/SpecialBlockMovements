package net.darmo_creations.special_block_movements.tile_entities;

import java.util.Optional;

import net.darmo_creations.special_block_movements.entities.EntityMovingStructure;
import net.darmo_creations.special_block_movements.utils.TileEntityUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityStructureController<T extends EntityMovingStructure> extends TileEntity implements ITickable {
  public static final int MAX_BLOCKS_NB = 100;

  protected boolean powered, adjusting;
  protected float speed;
  protected int blocksNb;
  protected T structure;
  protected int structureId;

  public TileEntityStructureController() {
    this.powered = false;
    this.adjusting = false;
    this.speed = 1;
    this.structure = null;
    this.structureId = -1;
  }

  public boolean isPowered() {
    return this.powered;
  }

  public void setPowered(boolean powered) {
    this.powered = powered;
    powerChanged();
    if (getWorld() != null) {
      TileEntityUtils.sendTileEntityUpdate(getWorld(), this);
      markDirty();
    }
  }

  public abstract void powerChanged();

  @SuppressWarnings("unchecked")
  @Override
  public void update() {
    if (this.structure == null && this.structureId != -1) {
      try {
        System.out.println(getWorld().getEntities(EntityMovingStructure.class, e -> {
          System.out.println(":" + e.getSaveId() + " " + this.structureId);
          return true;
        }));
        getWorld().getEntities(EntityMovingStructure.class, e -> e.getSaveId() == this.structureId).stream().findFirst().ifPresent(e -> {
          this.structure = (T) e;
          this.structure.setSaveId(this.structure.getEntityId());
        });
      }
      catch (ClassCastException __) {}
      this.structureId = -1;
    }
  }

  public Optional<T> getStructure() {
    return Optional.ofNullable(this.structure);
  }

  public void setStructure(T structure) {
    this.structure = structure;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setBoolean("Powered", isPowered());
    compound.setFloat("Speed", this.speed);
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
    this.adjusting = compound.getBoolean("Adjusting");
    this.structureId = compound.getInteger("StructureId");
    if (hasWorld() && this.structureId != -1) {
      try {
        // System.out.println(":" + this.structureId);
        System.out.println(getWorld().getEntities(EntityMovingStructure.class, e -> {
          // System.out.println(":" + e.getSaveId());
          return true;
        }));
        getWorld().getEntities(EntityMovingStructure.class, e -> e.getSaveId() == this.structureId + 1).stream().findFirst().ifPresent(
            e -> this.structure = (T) e);
        // System.out.println(this.structure);
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
