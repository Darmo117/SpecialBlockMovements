package net.darmo_creations.special_block_movements.guis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.darmo_creations.special_block_movements.Constants;
import net.darmo_creations.special_block_movements.network.ModNetworkWrapper;
import net.darmo_creations.special_block_movements.network.SyncPivotMessage;
import net.darmo_creations.special_block_movements.tile_entities.TileEntityPivot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiPivot extends GuiScreen {
  private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/pivot.png");

  private static final int VALIDATE_BTN_ID = 0;
  private static final int CANCEL_BTN_ID = 1;
  private static final int DEC_SPEED_BTN_ID = 2;
  private static final int INC_SPEED_BTN_ID = 3;
  private static final int ROTATION_BTN_ID = 4;
  private static final int SWITCH_OFF_MODE_BTN_ID = 5;

  private TileEntityPivot pivot;
  private int xSize, ySize;
  private float speed;
  private boolean clockwise;
  private boolean endRotation;

  private Button validateBtn, cancelBtn, decSpeedBtn, incSpeedBtn;
  private ToggleButton rotationDirBtn, switchOffModeBtn;

  public GuiPivot(TileEntityPivot pivot) {
    this.allowUserInput = false;
    this.pivot = pivot;
    this.xSize = 160;
    this.ySize = 126;
    this.speed = Math.abs(this.pivot.getSpeed());
    this.clockwise = pivot.isClockwise();
    this.endRotation = pivot.endsRotation();
  }

  @Override
  public void initGui() {
    addButton(this.validateBtn = new Button(VALIDATE_BTN_ID, 0, 0));
    addButton(this.cancelBtn = new Button(CANCEL_BTN_ID, 20, 0));
    addButton(this.decSpeedBtn = new Button(DEC_SPEED_BTN_ID, 40, 0));
    addButton(this.incSpeedBtn = new Button(INC_SPEED_BTN_ID, 60, 0));
    addButton(this.rotationDirBtn = new ToggleButton(ROTATION_BTN_ID, 80, 0, 100, 0));
    addButton(this.switchOffModeBtn = new ToggleButton(SWITCH_OFF_MODE_BTN_ID, 120, 0, 140, 0));
    updateButtons();
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    switch (button.id) {
      case VALIDATE_BTN_ID:
        ModNetworkWrapper.getModWapper().sendToServer(
            new SyncPivotMessage(this.pivot.getPos(), this.speed, this.clockwise, this.endRotation));
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null) {
          this.mc.setIngameFocus();
        }
        break;
      case CANCEL_BTN_ID:
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null) {
          this.mc.setIngameFocus();
        }
        break;
      case DEC_SPEED_BTN_ID:
        this.speed--;
        break;
      case INC_SPEED_BTN_ID:
        this.speed++;
        break;
      case ROTATION_BTN_ID:
        this.clockwise = !this.clockwise;
        break;
      case SWITCH_OFF_MODE_BTN_ID:
        this.endRotation = !this.endRotation;
        break;
    }
    updateButtons();
  }

  private void updateButtons() {
    this.decSpeedBtn.enabled = this.speed > 1;
    this.incSpeedBtn.enabled = this.speed < 10;
    this.rotationDirBtn.setSelected(this.clockwise);
    this.switchOffModeBtn.setSelected(!this.endRotation);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    this.mc.getTextureManager().bindTexture(TEXTURE);
    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    String title = I18n.format("gui.pivot.title");
    this.fontRendererObj.drawString(title, (this.width - this.fontRendererObj.getStringWidth(title)) / 2, y + 8, 0xffffff);
    List<String> tooltip = new ArrayList<>();

    int newY = y + 30;

    this.fontRendererObj.drawString(I18n.format("gui.pivot.speed.label"), x, newY, 10526880);
    int newX = x + this.xSize - this.incSpeedBtn.width;
    this.incSpeedBtn.xPosition = newX;
    this.incSpeedBtn.yPosition = newY - 6;
    newX -= this.decSpeedBtn.width + 2;
    this.decSpeedBtn.xPosition = newX;
    this.decSpeedBtn.yPosition = newY - 6;
    String text = (int) this.speed + "°/tick";
    newX -= this.fontRendererObj.getStringWidth(text) + 10;
    this.fontRendererObj.drawString(text, newX, newY, 0xffffff);

    newY += 22;
    this.fontRendererObj.drawString(I18n.format("gui.pivot.direction.label"), x, newY, 10526880);
    this.rotationDirBtn.xPosition = x + this.xSize - this.rotationDirBtn.width;
    this.rotationDirBtn.yPosition = newY - 6;

    newY += 22;
    this.fontRendererObj.drawString(I18n.format("gui.pivot.behavior.label"), x, newY, 10526880);
    this.switchOffModeBtn.xPosition = x + this.xSize - this.switchOffModeBtn.width;
    this.switchOffModeBtn.yPosition = newY - 6;

    newY = y + this.ySize - this.validateBtn.height - 10;
    this.validateBtn.xPosition = x;
    this.validateBtn.yPosition = newY;
    this.cancelBtn.xPosition = x + this.xSize - this.cancelBtn.width;
    this.cancelBtn.yPosition = newY;

    if (this.rotationDirBtn.isMouseOver())
      tooltip.add(I18n.format("gui.pivot.direction.tooltip." + (this.rotationDirBtn.isSelected() ? "clockwise" : "counterclockwise")));
    if (this.switchOffModeBtn.isMouseOver())
      tooltip.add(I18n.format("gui.pivot.behavior.tooltip." + (this.switchOffModeBtn.isSelected() ? "go_back" : "end_turn")));

    super.drawScreen(mouseX, mouseY, partialTicks);
    drawHoveringText(tooltip, mouseX, mouseY);
  }

  private class Button extends GuiButton {
    private int iconX, iconY;

    public Button(int buttonId, int iconX, int iconY) {
      super(buttonId, 0, 0, 20, 20, "");
      this.iconX = iconX;
      this.iconY = iconY;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
        super.drawButton(mc, mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;

        drawTexturedModalRect(this.xPosition, this.yPosition, this.iconX, this.iconY, this.width, this.height);
      }
    }
  }

  private class ToggleButton extends GuiButton {
    private int iconX, iconY, selectedIconX, selectedIconY;
    private boolean selected;

    public ToggleButton(int buttonId, int iconX, int iconY, int selectedIconX, int selectedIconY) {
      super(buttonId, 0, 0, 20, 20, "");
      this.iconX = iconX;
      this.iconY = iconY;
      this.selectedIconX = selectedIconX;
      this.selectedIconY = selectedIconY;
      this.selected = false;
    }

    public boolean isSelected() {
      return this.selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
        super.drawButton(mc, mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(TEXTURE);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
            && mouseY < this.yPosition + this.height;

        if (isSelected())
          drawTexturedModalRect(this.xPosition, this.yPosition, this.selectedIconX, this.selectedIconY, this.width, this.height);
        else
          drawTexturedModalRect(this.xPosition, this.yPosition, this.iconX, this.iconY, this.width, this.height);
      }
    }
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }
}
