package net.darmo_creations.special_block_movements;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class declares all items.
 * 
 * @author Damien Vergnet
 */
public final class ModItems {
  /**
   * Initializes all items.
   */
  public static void init() {}

  /**
   * Registers an item.
   * 
   * @param item the item
   */
  private static void register(Item item) {
    GameRegistry.register(item);
  }

  private ModItems() {}
}
