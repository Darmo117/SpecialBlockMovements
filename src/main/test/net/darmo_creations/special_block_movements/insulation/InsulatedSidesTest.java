package net.darmo_creations.special_block_movements.insulation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.special_block_movements.insulation.InsulationHandler.InsulatedSides;
import net.minecraft.util.EnumFacing;

public class InsulatedSidesTest {
  private InsulatedSides sidesEmpty, sides;

  @Before
  public void setUp() {
    this.sidesEmpty = new InsulatedSides();
    this.sides = new InsulatedSides((byte) 0b00000001);
  }

  @Test
  public void testAddSideNew() {
    this.sidesEmpty.addSide(EnumFacing.DOWN);
    assertEquals(0b00000001, this.sidesEmpty.getMask());
    this.sides.addSide(EnumFacing.UP);
    assertEquals(0b00000011, this.sides.getMask());
  }

  @Test
  public void testAddSideAlreadyPresent() {
    this.sides.addSide(EnumFacing.DOWN);
    assertEquals(0b0000001, this.sides.getMask());
  }

  @Test
  public void testRemoveSideAbsent() {
    this.sidesEmpty.removeSide(EnumFacing.DOWN);
    assertEquals(0b00000000, this.sidesEmpty.getMask());
    this.sides.removeSide(EnumFacing.UP);
    assertEquals(0b00000001, this.sides.getMask());
  }

  @Test
  public void testRemoveSidePresent() {
    this.sides.removeSide(EnumFacing.DOWN);
    assertEquals(0b0000000, this.sides.getMask());
  }

  @Test
  public void testHasNotSide() {
    assertFalse(this.sidesEmpty.hasSide(EnumFacing.DOWN));
    assertFalse(this.sides.hasSide(EnumFacing.UP));
  }

  @Test
  public void testHasSide() {
    assertTrue(this.sides.hasSide(EnumFacing.DOWN));
  }
}
