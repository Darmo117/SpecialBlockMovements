package net.darmo_creations.special_block_movements.insulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public final class InsulationHandler {
  private final Map<BlockPos, InsulatedSides> plates;

  public InsulationHandler() {
    this.plates = new HashMap<>();
  }

  public void addPlate(BlockPos pos, EnumFacing side) {
    if (!this.plates.containsKey(pos))
      this.plates.put(new BlockPos(pos), new InsulatedSides());
    this.plates.get(pos).addSide(side);
  }

  public void removePlate(BlockPos pos, EnumFacing side) {
    if (this.plates.containsKey(pos))
      this.plates.get(pos).removeSide(side);
  }

  public Set<BlockPos> getAllPositions() {
    Set<BlockPos> set = new HashSet<>();

    for (BlockPos p : this.plates.keySet()) {
      set.add(new BlockPos(p));
    }

    return set;
  }

  public Optional<InsulatedSides> getInsulatedSides(BlockPos pos) {
    if (this.plates.containsKey(pos))
      return Optional.of(this.plates.get(pos).clone());
    return Optional.empty();
  }

  public static final class InsulatedSides implements Cloneable {
    private byte sides;

    public InsulatedSides() {
      this.sides = 0;
    }

    public void addSide(EnumFacing side) {
      this.sides |= 1 << side.getIndex();
    }

    public void removeSide(EnumFacing side) {
      this.sides &= ~side.getIndex();
    }

    public boolean hasSide(EnumFacing side) {
      return (this.sides & (1 << side.getIndex())) != 0;
    }

    @Override
    public InsulatedSides clone() {
      try {
        return (InsulatedSides) super.clone();
      }
      catch (CloneNotSupportedException e) {
        throw new Error(e);
      }
    }
  }
}
