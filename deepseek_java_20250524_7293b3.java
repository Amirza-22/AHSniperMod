package com.example.ahsniper;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class SniperConfigGUI extends GuiScreen {
    private final AHSniperMod mod;
    private GuiTextField minProfitField;

    public SniperConfigGUI(AHSniperMod mod) {
        this.mod = mod;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 50, 200, 20, "Save"));
        minProfitField = new GuiTextField(1, this.fontRendererObj, width / 2 - 100, height / 2, 200, 20);
        minProfitField.setText(String.valueOf(mod.getMinProfitMargin() * 100));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(fontRendererObj, "AH Sniper Settings", width / 2, height / 2 - 30, 0xFFFFFF);
        this.drawString(fontRendererObj, "Min Profit Margin (%):", width / 2 - 100, height / 2 - 10, 0xAAAAAA);
        minProfitField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            try {
                mod.setMinProfitMargin(Double.parseDouble(minProfitField.getText()) / 100);
                Minecraft.getMinecraft().displayGuiScreen(null);
            } catch (NumberFormatException e) {
                minProfitField.setText("Invalid");
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        minProfitField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        minProfitField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}