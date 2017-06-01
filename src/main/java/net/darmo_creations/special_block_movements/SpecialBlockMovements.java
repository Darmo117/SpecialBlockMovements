package net.darmo_creations.special_block_movements;

import net.darmo_creations.special_block_movements.entities.EntityRotatingStructure;
import net.darmo_creations.special_block_movements.entities.EntitySlidingStructure;
import net.darmo_creations.special_block_movements.guis.GuiHandler;
import net.darmo_creations.special_block_movements.insulation.InsulationHandler;
import net.darmo_creations.special_block_movements.insulation.InsulationPlateActionMessage;
import net.darmo_creations.special_block_movements.insulation.SyncInsulationPlatesMessage;
import net.darmo_creations.special_block_movements.network.ModNetworkWrapper;
import net.darmo_creations.special_block_movements.network.SyncPivotMessage;
import net.darmo_creations.special_block_movements.network.SyncRotatingStructureMessage;
import net.darmo_creations.special_block_movements.proxy.CommonProxy;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityInsulated;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityPivot;
import net.darmo_creations.special_block_movements.tile_entities.TileEntitySlider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Main mod class.
 * 
 * @author Damien Vergnet
 */
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
public class SpecialBlockMovements {
  /** Mod instance. */
  @Instance
  public static SpecialBlockMovements theMod;

  /** Client and server proxies. */
  @SidedProxy(clientSide = "net.darmo_creations.special_block_movements.proxy.ClientProxy", serverSide = "net.darmo_creations.special_block_movements.proxy.CommonProxy")
  public static CommonProxy proxy;

  private static InsulationHandler insulationHandler;

  /**
   * @return the global insulation handler
   */
  public static InsulationHandler getInsulationHandler() {
    return insulationHandler;
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    ModBlocks.init();
    ModItems.init();
    ModCrafts.registerCrafts();

    registerTileEntities();
    registerEntities();
    insulationHandler = new InsulationHandler();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(theMod, new GuiHandler());
    registerPackets();
    MinecraftForge.EVENT_BUS.register(insulationHandler);
  }

  /**
   * Registers network packets.
   */
  private void registerPackets() {
    ModNetworkWrapper.registerPacket(SyncRotatingStructureMessage.ClientHandler.class, SyncRotatingStructureMessage.class, Side.CLIENT);
    ModNetworkWrapper.registerPacket(SyncPivotMessage.ServerHandler.class, SyncPivotMessage.class, Side.SERVER);
    ModNetworkWrapper.registerPacket(SyncPivotMessage.ClientHandler.class, SyncPivotMessage.class, Side.CLIENT);
    ModNetworkWrapper.registerPacket(InsulationPlateActionMessage.ServerHandler.class, InsulationPlateActionMessage.class, Side.SERVER);
    ModNetworkWrapper.registerPacket(SyncInsulationPlatesMessage.ClientHandler.class, SyncInsulationPlatesMessage.class, Side.CLIENT);
  }

  /**
   * Registers tile entities.
   */
  private void registerTileEntities() {
    GameRegistry.registerTileEntity(TileEntityPivot.class, "pivot");
    GameRegistry.registerTileEntity(TileEntitySlider.class, "slider");
    GameRegistry.registerTileEntity(TileEntityInsulated.class, "insulated_block");
  }

  /**
   * Registers entities.
   */
  private void registerEntities() {
    EntityRegistry.registerModEntity(new ResourceLocation(Constants.MOD_ID, "rotating_structure"), EntityRotatingStructure.class,
        "rotating_structure", 1000, this, 80, 3, true);
    EntityRegistry.registerModEntity(new ResourceLocation(Constants.MOD_ID, "sliding_structure"), EntitySlidingStructure.class,
        "sliding_structure", 1001, this, 80, 3, true);
  }
}