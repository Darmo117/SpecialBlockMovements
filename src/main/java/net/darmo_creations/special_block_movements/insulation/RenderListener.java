package net.darmo_creations.special_block_movements.insulation;

import net.darmo_creations.special_block_movements.SpecialBlockMovements;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderListener {
  @SubscribeEvent
  public void onRender(RenderWorldLastEvent e) {
    InsulationHandler handler = SpecialBlockMovements.getInsulationHandler();

    for (BlockPos pos : handler.getAllPositions()) {
      System.out.println(pos + " " + Integer.toBinaryString(handler.getInsulatedSides(pos).get().getMask()));
      // GlStateManager.pushMatrix();
      // GlStateManager.glBegin(GL11.GL_LINE);
      //
      // GlStateManager.color(1, 1, 1);
      // GlStateManager.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
      // GlStateManager.glVertex3f(pos.getX(), pos.getY() + 1, pos.getZ());
      //
      // GlStateManager.glEnd();
      // GlStateManager.popMatrix();
    }
  }
}
