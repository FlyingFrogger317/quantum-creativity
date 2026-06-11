package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuantizerBlockScreen extends AbstractContainerScreen<QuantizerBlockMenu> {
    public QuantizerBlockScreen(QuantizerBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth=176;
        this.imageHeight=240;
        this.titleLabelX+=20;
        this.inventoryLabelY+=74;
    }

    private static final ResourceLocation BG_TEXTURE = ResourceLocation.fromNamespaceAndPath(QuantumCreativity.MODID, "textures/gui/container/quantizer.png");

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Calculate the top-left corner of your GUI
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;

        // Bind and draw your background texture
        guiGraphics.blit(BG_TEXTURE, left, top, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.renderBg(pGuiGraphics,pPartialTick,pMouseX,pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics,pMouseX,pMouseY);
    }
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Draw block title
        guiGraphics.drawString(
                this.font,
                this.title,
                this.titleLabelX,
                this.titleLabelY,
                0xFFFFFF, // ← change color here
                false
        );

        // Draw "Inventory"
        guiGraphics.drawString(
                this.font,
                this.playerInventoryTitle,
                this.inventoryLabelX,
                this.inventoryLabelY,
                0xFFFFFF, // ← change color here
                false
        );
    }
}
