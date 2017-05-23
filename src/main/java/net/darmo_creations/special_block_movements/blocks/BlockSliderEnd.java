package net.darmo_creations.special_block_movements.blocks;

import net.darmo_creations.special_block_movements.commons.ModBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockSliderEnd extends ModBlock {
  private final boolean isPlus;

  public BlockSliderEnd(boolean isPlus) {
    super("translator_end_" + (isPlus ? "plus" : "minus"), Material.IRON, SoundType.METAL, 2, 5, null, -1, CreativeTabs.REDSTONE);
    this.isPlus = isPlus;
  }

  public boolean isPlus() {
    return this.isPlus;
  }
}
