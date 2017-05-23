package net.darmo_creations.special_block_movements.utils;

import org.lwjgl.opengl.GL11;

import net.darmo_creations.special_block_movements.utils.math.Point2d;
import net.darmo_creations.special_block_movements.utils.math.Point3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class provides methods to draw with OpenGL.
 *
 * @author Damien Vergnet
 */
public final class RenderUtils {
  /**
   * "Fixes" the lighting.
   * 
   * @param world the world
   * @param pos the position
   */
  public static void fixLighting(World world, BlockPos pos) {
    int worldLight = world.getCombinedLight(pos, 15728640);
    int wBrightness = worldLight % 65536;
    int yBrightness = worldLight / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) wBrightness, (float) yBrightness);
  }

  public static VertexBuffer getRenderer() {
    return Tessellator.getInstance().getBuffer();
  }

  /**
   * Begins GL_QUADS mode.
   */
  public static void beginQuads() {
    getRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
  }

  /**
   * Loads a texture.
   *
   * @param texturePath the path to the texture
   */
  public static void bindTex(String texturePath) {
    beginQuads();
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(texturePath));
  }

  /**
   * Declares a vertex.
   *
   * @param texX texture x
   * @param texY texture y
   */
  public static void vertex(double x, double y, double z, double texX, double texY) {
    getRenderer().pos(x, y, z).tex(texX, texY).endVertex();
  }

  /**
   * Declares a vertex.
   *
   * @param tex texture's coordinates
   */
  public static void vertex(double x, double y, double z, Point2d tex) {
    vertex(x, y, z, tex.getX(), tex.getY());
  }

  /**
   * Declares a vertex.
   *
   * @param ver vertex' coordinates
   * @param tex texture's coordinates
   */
  public static void vertex(Point3d ver, Point2d tex) {
    vertex(ver.getX(), ver.getY(), ver.getZ(), tex);
  }

  /**
   * Draws a quad.
   */
  public static void drawQuad(Point3d p1, Point3d p2, Point3d p3, Point3d p4, Point2d tex1, Point2d tex2, Point2d tex3, Point2d tex4) {
    vertex(p1, tex1);
    vertex(p2, tex2);
    vertex(p3, tex3);
    vertex(p4, tex4);
  }

  /**
   * Renders an item stack.
   *
   * @param stack the stack
   */
  public static void renderItem(ItemStack stack) {
    Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
  }

  /**
   * Draws the buffer.
   */
  public static void draw() {
    Tessellator.getInstance().draw();
  }

  private RenderUtils() {}
}
