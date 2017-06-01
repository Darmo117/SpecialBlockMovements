package net.darmo_creations.special_block_movements.insulation;

import java.util.Optional;

import net.darmo_creations.special_block_movements.SpecialBlockMovements;
import net.darmo_creations.special_block_movements.commons.ModItem;
import net.darmo_creations.special_block_movements.insulation.InsulationHandler.InsulatedSides;
import net.darmo_creations.special_block_movements.network.ModNetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemInsulator extends ModItem {
  public ItemInsulator() {
    super("insulator", 1, CreativeTabs.REDSTONE);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
      float hitY, float hitZ) {
    if (world.isRemote) {
      Optional<InsulatedSides> sides = SpecialBlockMovements.getInsulationHandler().getInsulatedSides(pos);

      ModNetworkWrapper.getModWapper().sendToServer(
          new InsulationPlateActionMessage(pos, facing, !sides.isPresent() || !sides.get().hasSide(facing)));
    }

    return EnumActionResult.SUCCESS;
  }
}
