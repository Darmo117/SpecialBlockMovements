package net.darmo_creations.special_block_movements.blocks;

import net.darmo_creations.special_block_movements.tile_entities.TileEntityPivot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPivot extends BlockStructureController<TileEntityPivot> {
  public BlockPivot() {
    super("pivot", TileEntityPivot.class);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
      float hitX, float hitY, float hitZ) {
    TileEntity te = world.getTileEntity(pos);

    if (te instanceof TileEntityPivot) {
      TileEntityPivot pivot = (TileEntityPivot) te;

      System.out.println("Speed: " + pivot.getSpeed());
      System.out.println("Angle: " + pivot.getAngle());
      System.out.println("Ends rotation: " + pivot.endsRotation());

      return true;
    }

    return false;
  }
}
