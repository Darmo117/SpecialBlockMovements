package net.darmo_creations.special_block_movements.blocks;

import net.darmo_creations.special_block_movements.commons.ModBlock;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityInsulated;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockInsulated extends ModBlock implements ITileEntityProvider {
  public BlockInsulated() {
    super("insulated_block", Material.IRON, SoundType.METAL, 2, 5, null, -1, CreativeTabs.REDSTONE);
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityInsulated();
  }
}
