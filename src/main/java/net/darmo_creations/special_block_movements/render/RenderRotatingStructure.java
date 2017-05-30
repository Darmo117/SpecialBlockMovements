package net.darmo_creations.special_block_movements.render;

import java.util.Map;

import net.darmo_creations.special_block_movements.entities.EntityRotatingStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RenderRotatingStructure extends Render<EntityRotatingStructure> {
  public RenderRotatingStructure(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  public void doRender(EntityRotatingStructure entity, double x, double y, double z, float entityYaw, float partialTicks) {
    Map<BlockPos, IBlockState> blocks = entity.getBlocks();

    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();
    BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ).offset(entity.getFacing().getOpposite());
    int rotateX = entity.getFacing().getAxis() == EnumFacing.Axis.X ? 1 : 0;
    int rotateY = entity.getFacing().getAxis() == EnumFacing.Axis.Y ? 1 : 0;
    int rotateZ = entity.getFacing().getAxis() == EnumFacing.Axis.Z ? 1 : 0;
    GlStateManager.translate(x, y, z);
    if (rotateY == 0)
      GlStateManager.translate(0, 0.5, 0);
    GlStateManager.rotate(entity.getAngle() * entity.getFacing().getAxisDirection().getOffset(), rotateX, rotateY, rotateZ);
    if (rotateY == 0)
      GlStateManager.translate(0, -0.5, 0);
    GlStateManager.translate(-blockpos.getX() - 0.5, -blockpos.getY(), -blockpos.getZ() - 0.5);

    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    }

    for (Map.Entry<BlockPos, IBlockState> entry : blocks.entrySet()) {
      IBlockState state = entry.getValue();

      if (state.getRenderType() == EnumBlockRenderType.MODEL) {
        World world = entity.getEntityWorld();

        if (state != world.getBlockState(new BlockPos(entity)) && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
          GlStateManager.pushMatrix();
          bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
          Tessellator tessellator = Tessellator.getInstance();
          VertexBuffer buffer = tessellator.getBuffer();
          BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
          BlockPos pos = entry.getKey();

          GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
          buffer.begin(7, DefaultVertexFormats.BLOCK);
          dispatcher.getBlockModelRenderer().renderModel(world, dispatcher.getModelForState(state), state, blockpos, buffer, false,
              MathHelper.getPositionRandom(entity.getPosition()));
          tessellator.draw();

          GlStateManager.popMatrix();
        }
      }
    }

    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }

    GlStateManager.enableLighting();
    GlStateManager.popMatrix();

    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityRotatingStructure entity) {
    return null;
  }
}
