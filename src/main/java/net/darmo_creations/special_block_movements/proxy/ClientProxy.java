package net.darmo_creations.special_block_movements.proxy;

import static net.darmo_creations.special_block_movements.ModBlocks.*;
import static net.darmo_creations.special_block_movements.ModItems.*;

import net.darmo_creations.special_block_movements.entities.EntityRotatingStructure;
import net.darmo_creations.special_block_movements.insulation.RenderListener;
import net.darmo_creations.special_block_movements.render.RenderRotatingStructure;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Proxy used by the client.
 *
 * @author Damien Vergnet
 */
public class ClientProxy extends CommonProxy {
  @SuppressWarnings("deprecation")
  @Override
  public void register() {
    MinecraftForge.EVENT_BUS.register(new RenderListener());

    registerBlock(PIVOT);
    registerBlock(SLIDER);
    registerBlock(SLIDER_END_PLUS);
    registerBlock(SLIDER_END_MINUS);

    registerItem(INSULATOR);

    RenderManager render = Minecraft.getMinecraft().getRenderManager();

    RenderingRegistry.registerEntityRenderingHandler(EntityRotatingStructure.class, new RenderRotatingStructure(render));
  }

  /**
   * Registers an item.
   *
   * @param item the item
   */
  private static void registerItem(Item item) {
    registerItem_Impl(item, 0, item.getRegistryName().getResourcePath());
  }

  /**
   * Registers a block.
   *
   * @param block the block
   */
  private static void registerBlock(Block block) {
    registerBlock(block, 0, block.getRegistryName().getResourcePath());
  }

  /**
   * Registers a block.
   *
   * @param block the block
   * @param metadata the metadata
   * @param name block's internal name
   */
  private static void registerBlock(Block block, int metadata, String name) {
    registerItem_Impl(Item.getItemFromBlock(block), metadata, name);
  }

  /**
   * Registers an item.
   *
   * @param item the item
   * @param metadata the metadata
   * @param name item's internal name
   */
  private static void registerItem_Impl(Item item, int metadata, String name) {
    if (item != null)
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, metadata,
          new ModelResourceLocation(item.getRegistryName().getResourceDomain() + ":" + name, "inventory"));
  }
}
