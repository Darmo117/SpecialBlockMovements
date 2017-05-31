package net.darmo_creations.special_block_movements;

import net.darmo_creations.special_block_movements.insulation.ItemInsulator;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class declares all items.
 * 
 * @author Damien Vergnet
 */
public final class ModItems {
  public static final ItemInsulator INSULATOR = new ItemInsulator();

  /**
   * Initializes all items.
   */
  public static void init() {
    register(INSULATOR);
  }

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
