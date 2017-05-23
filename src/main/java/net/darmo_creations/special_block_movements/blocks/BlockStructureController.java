package net.darmo_creations.special_block_movements.blocks;

import net.darmo_creations.special_block_movements.commons.ModBlock;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityStructureController;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStructureController<T extends TileEntityStructureController<?>> extends ModBlock implements ITileEntityProvider {
  public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

  private Class<T> tileEntityClass;

  public BlockStructureController(String name, Class<T> tileEntityClass) {
    super(name, Material.IRON, SoundType.METAL, 2, 5, null, -1, CreativeTabs.REDSTONE);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    this.tileEntityClass = tileEntityClass;
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
      EntityLivingBase placer, EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing);
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
    if (!world.isRemote) {
      TileEntity te = world.getTileEntity(pos);

      if (te != null && te.getClass() == this.tileEntityClass) {
        this.tileEntityClass.cast(te).setPowered(world.isBlockPowered(pos));
      }
    }
  }

  @Override
  public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    TileEntity te = world.getTileEntity(pos);
    if (te != null && te.getClass() == this.tileEntityClass) { // TODO place back blocks
      this.tileEntityClass.cast(te).getStructure().ifPresent(s -> s.setDead());
    }
    super.breakBlock(world, pos, state);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    try {
      return this.tileEntityClass.newInstance();
    }
    catch (InstantiationException | IllegalAccessException e) {
      return null;
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getIndex();
  }
}
