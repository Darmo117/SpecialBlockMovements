package net.darmo_creations.special_block_movements.commons;

import net.darmo_creations.special_block_movements.utils.NameUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * This class serves as the basis for all items from this mod.
 * 
 * @author Damien Vergnet
 */
public class ModItem extends Item {
  /**
   * Creates a new item with a max stack size of 64.
   */
  public ModItem(String registryName, CreativeTabs tab) {
    this(registryName, 64, tab);
  }

  /**
   * Instancie un nouvel item.
   */
  public ModItem(String registryName, int maxStackSize, CreativeTabs tab) {
    super();
    setMaxStackSize(maxStackSize);
    setCreativeTab(tab);
    setRegistryName(registryName);
    setUnlocalizedName(NameUtils.getUnlocalizedName(registryName));
  }
}