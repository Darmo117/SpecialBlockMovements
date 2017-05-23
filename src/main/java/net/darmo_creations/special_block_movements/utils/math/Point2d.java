package net.darmo_creations.special_block_movements.utils.math;

/**
 * This class represents a 2-D point.
 *
 * @author Damien Vergnet
 */
public final class Point2d {
  private final double x, y;

  /**
   * Copies a point.
   * 
   * @param point the point to copy
   */
  public Point2d(Point2d point) {
    this(point.getX(), point.getY());
  }

  public Point2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  /**
   * Adds a value to x.
   * 
   * @param tx the value
   * @return the new point
   */
  public Point2d addX(double tx) {
    return new Point2d(this.x + tx, this.y);
  }

  /**
   * Adds a value to y.
   * 
   * @param ty the value
   * @return the new point
   */
  public Point2d addY(double ty) {
    return new Point2d(this.x, this.y + ty);
  }
}
