package net.darmo_creations.special_block_movements.utils.math;

/**
 * This class represents a 3-D point.
 *
 * @author Damien Vergnet
 */
public final class Point3d {
  private final double x, y, z;

  /**
   * Copies a point.
   * 
   * @param point the point to copy
   */
  public Point3d(Point3d point) {
    this(point.getX(), point.getY(), point.getZ());
  }

  public Point3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  /**
   * Adds a value to x.
   * 
   * @param tx the value
   * @return the new point
   */
  public Point3d addX(double tx) {
    return new Point3d(this.x + tx, this.y, this.z);
  }

  /**
   * Adds a value to y.
   * 
   * @param ty the value
   * @return the new point
   */
  public Point3d addY(double ty) {
    return new Point3d(this.x, this.y + ty, this.z);
  }

  /**
   * Adds a value to z.
   * 
   * @param tz the value
   * @return the new point
   */
  public Point3d addZ(double tz) {
    return new Point3d(this.x, this.y, this.z + tz);
  }
}
