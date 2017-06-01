package net.darmo_creations.special_block_movements.insulation;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.darmo_creations.special_block_movements.network.ModNetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public final class InsulationHandler {
  private final Map<BlockPos, InsulatedSides> plates;

  public InsulationHandler() {
    this.plates = new HashMap<>();
  }

  public void reset() {
    this.plates.clear();
  }

  public void addPlate(BlockPos pos, EnumFacing side) {
    if (!this.plates.containsKey(pos))
      this.plates.put(new BlockPos(pos), new InsulatedSides());
    this.plates.get(pos).addSide(side);
  }

  public void removePlate(BlockPos pos, EnumFacing side) {
    if (this.plates.containsKey(pos)) {
      this.plates.get(pos).removeSide(side);
      if (this.plates.get(pos).getMask() == 0)
        this.plates.remove(pos);
    }
  }

  public Set<BlockPos> getAllPositions() {
    Set<BlockPos> set = new HashSet<>();

    for (BlockPos p : this.plates.keySet()) {
      set.add(new BlockPos(p));
    }

    return set;
  }

  // 3: x, y, z from BlockPos
  // 4: size of an int
  // 1: insulate sides mask
  /** The size of one block for the save file. */
  private static final int BLOCK_SIZE = 3 * 4 + 1;

  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load e) {
    World world = e.getWorld();

    if (!world.isRemote) {
      this.plates.clear();
      try {
        File f = getFile(world);

        if (f.exists()) {
          ByteBuffer b = ByteBuffer.wrap(Files.readAllBytes(f.toPath()));

          while (b.remaining() >= BLOCK_SIZE) {
            BlockPos pos = new BlockPos(b.getInt(), b.getInt(), b.getInt());
            InsulatedSides sides = new InsulatedSides(b.get());

            this.plates.put(pos, sides);
          }
        }
      }
      catch (IOException __) {}
    }
  }

  @SubscribeEvent
  public void onWorldSave(WorldEvent.Save e) {
    World world = e.getWorld();

    if (!world.isRemote) {
      try {
        ByteBuffer b = ByteBuffer.allocate(this.plates.size() * BLOCK_SIZE);

        for (Map.Entry<BlockPos, InsulatedSides> entry : this.plates.entrySet()) {
          BlockPos pos = entry.getKey();

          b.putInt(pos.getX());
          b.putInt(pos.getY());
          b.putInt(pos.getZ());
          b.put(entry.getValue().getMask());
        }

        Files.write(getFile(e.getWorld()).toPath(), b.array());
      }
      catch (IOException __) {}
    }
  }

  @SubscribeEvent
  public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent e) {
    ModNetworkWrapper.getModWapper().sendTo(new SyncInsulationPlatesMessage(this.plates), (EntityPlayerMP) e.player);
  }

  private File getFile(World world) {
    String name = world.getMinecraftServer().getFolderName();
    return new File("saves/" + name + "/sbm.dat");
  }

  public Optional<InsulatedSides> getInsulatedSides(BlockPos pos) {
    if (this.plates.containsKey(pos))
      return Optional.of(this.plates.get(pos).clone());
    return Optional.empty();
  }

  public static final class InsulatedSides implements Cloneable {
    private byte mask;

    public InsulatedSides() {
      this((byte) 0);
    }

    public InsulatedSides(byte mask) {
      this.mask = mask;
    }

    public void addSide(EnumFacing side) {
      this.mask |= 1 << side.getIndex();
    }

    public void removeSide(EnumFacing side) {
      this.mask &= ~(1 << side.getIndex());
    }

    public boolean hasSide(EnumFacing side) {
      return (this.mask & (1 << side.getIndex())) != 0;
    }

    public byte getMask() {
      return this.mask;
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
