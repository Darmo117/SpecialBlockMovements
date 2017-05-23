package net.darmo_creations.special_block_movements.render;

import net.darmo_creations.special_block_movements.tile_entities.TileEntityInsulated;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class TileEntityInsulatedRenderer extends TileEntitySpecialRenderer<TileEntityInsulated> {
  // FIXME
  @Override
  public void renderTileEntityAt(TileEntityInsulated te, double x, double y, double z, float partialTicks, int destroyStage) {
    IBlockState state = te.getDisplayedBlock();

    if (state.getRenderType() == EnumBlockRenderType.MODEL) {
      World world = te.getWorld();

      if (state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(x, y, z);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

        buffer.begin(7, DefaultVertexFormats.BLOCK);
        dispatcher.renderBlock(state, te.getPos(), world, buffer);
        tessellator.draw();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
      }
    }

    super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
  }
}
