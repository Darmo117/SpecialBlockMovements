package net.darmo_creations.special_block_movements.commons;

import net.darmo_creations.special_block_movements.utils.NameUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlockSpecial;

/**
 * This class facilitates the declaration of {@link ItemBlockSpecial}.
 * 
 * @author Damien Vergnet
 * @see ItemBlockSpecial
 */
public class ModItemBlockSpecial extends ItemBlockSpecial {
  /**
   * Creates a new item.
   */
  public ModItemBlockSpecial(String registryName, Block block, CreativeTabs creativeTabs) {
    super(block);
    setRegistryName(registryName);
    setUnlocalizedName(NameUtils.getUnlocalizedName(registryName));
    setCreativeTab(creativeTabs);
  }
}
