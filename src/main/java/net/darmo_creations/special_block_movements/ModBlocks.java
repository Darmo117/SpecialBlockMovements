package net.darmo_creations.special_block_movements;

import net.darmo_creations.special_block_movements.blocks.BlockPivot;
import net.darmo_creations.special_block_movements.blocks.BlockSlider;
import net.darmo_creations.special_block_movements.blocks.BlockSliderEnd;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class declares all blocks.
 *
 * @author Damien Vergnet
 */
public final class ModBlocks {
  public static final BlockPivot PIVOT = new BlockPivot();
  public static final BlockSlider SLIDER = new BlockSlider();
  public static final BlockSliderEnd SLIDER_END_PLUS = new BlockSliderEnd(true);
  public static final BlockSliderEnd SLIDER_END_MINUS = new BlockSliderEnd(false);

  /**
   * Initializes all blocks.
   */
  public static void init() {
    register(PIVOT);
    register(SLIDER);
    register(SLIDER_END_PLUS);
    register(SLIDER_END_MINUS);
  }

  /**
   * Registers a block.
   *
   * @param block the block
   */
  private static void register(Block block) {
    GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    GameRegistry.register(block);
  }

  private ModBlocks() {}
}
