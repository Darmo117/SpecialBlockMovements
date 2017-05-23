package net.darmo_creations.special_block_movements.commons;

import net.darmo_creations.special_block_movements.utils.NameUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * This class serves as the basis for all blocks from this mod.
 * 
 * @author Damien Vergnet
 */
public class ModBlock extends Block {
  /**
   * Creates a new block.
   */
  public ModBlock(String registryName, Material material, SoundType sound, float hardness, float resistance, String tool, int harvestLevel,
      CreativeTabs tab) {
    this(registryName, material, material.getMaterialMapColor(), sound, hardness, resistance, tool, harvestLevel, tab);
  }

  /**
   * Creates a new block.
   */
  public ModBlock(String registryName, Material material, MapColor mapColor, SoundType sound, float hardness, float resistance, String tool,
      int harvestLevel, CreativeTabs tab) {
    super(material, mapColor);
    setResistance(resistance);
    setHardness(hardness);
    if (tool != null)
      setHarvestLevel(tool, harvestLevel);
    setSoundType(sound);
    setCreativeTab(tab);
    setRegistryName(registryName);
    setUnlocalizedName(NameUtils.getUnlocalizedName(registryName));
  }
}