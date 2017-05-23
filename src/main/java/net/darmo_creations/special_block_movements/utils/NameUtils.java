package net.darmo_creations.special_block_movements.utils;

import net.darmo_creations.special_block_movements.Constants;

/**
 * This class provides methods to handle blocks/items' names.
 *
 * @author Damien Vergnet
 */
public final class NameUtils {
  /**
   * Returns the unlocalized name from the internal name.
   *
   * @param name internal name
   * @return the unlocalized name
   */
  public static String getUnlocalizedName(String name) {
    return Constants.MOD_ID + "_" + name;
  }

  private NameUtils() {}
}
