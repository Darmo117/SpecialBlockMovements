package net.darmo_creations.special_block_movements.insulation;

import org.lwjgl.opengl.GL11;

import net.darmo_creations.special_block_movements.SpecialBlockMovements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderListener {
  @SubscribeEvent
  public void onRender(RenderWorldLastEvent e) {
    InsulationHandler handler = SpecialBlockMovements.getInsulationHandler();

    for (BlockPos pos : handler.getAllPositions()) {
      EntityPlayerSP player = Minecraft.getMinecraft().player;

      GlStateManager.pushMatrix();
      GlStateManager.translate(pos.getX() - player.posX, pos.getY() - player.posY, pos.getZ() - player.posZ);
      GlStateManager.glLineWidth(2);
      GlStateManager.color(1, 1, 1);

      for (EnumFacing side : EnumFacing.VALUES) {
        if (!handler.getInsulatedSides(pos).get().hasSide(side))
          continue;

        GlStateManager.pushMatrix();

        switch (side) {
          case DOWN:
            // No-op
            break;
          case UP:
            GlStateManager.translate(0, 1, 1);
            GlStateManager.rotate(180, 1, 0, 0);
            break;
          case NORTH:
            GlStateManager.translate(0, 1, 0);
            GlStateManager.rotate(90, 1, 0, 0);
            break;
          case SOUTH:
            GlStateManager.translate(0, 0, 1);
            GlStateManager.rotate(-90, 1, 0, 0);
            break;
          case WEST:
            GlStateManager.translate(0, 1, 0);
            GlStateManager.rotate(-90, 0, 0, 1);
            break;
          case EAST:
            GlStateManager.translate(1, 0, 0);
            GlStateManager.rotate(90, 0, 0, 1);
            break;
        }

        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        GlStateManager.glVertex3f(0, 0, 0);
        GlStateManager.glVertex3f(0, 0, 1);
        GlStateManager.glVertex3f(1, 0, 1);
        GlStateManager.glVertex3f(1, 0, 0);
        GlStateManager.glVertex3f(0, 0, 0);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_LINES);
        GlStateManager.glVertex3f(0, 0, 0);
        GlStateManager.glVertex3f(1, 0, 1);
        GlStateManager.glVertex3f(0, 0, 1);
        GlStateManager.glVertex3f(1, 0, 0);
        // TEMP
        GlStateManager.glVertex3f(0.5f, 0, 0.5f);
        GlStateManager.glVertex3f(0.5f, -0.5f, 0.5f);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();
      }

      GlStateManager.popMatrix();
    }
  }
}
