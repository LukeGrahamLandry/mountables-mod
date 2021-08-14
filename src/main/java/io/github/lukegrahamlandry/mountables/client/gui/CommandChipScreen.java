package io.github.lukegrahamlandry.mountables.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandChipScreen extends Screen {
    private MountEntity mount;
    private float xMouse;
    private float yMouse;
    protected int imageWidth = 100;
    protected int imageHeight = 125;

    private static final ResourceLocation TEXTURE = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/chip.png");
    private int guiLeft;
    private int guiTop;

    public CommandChipScreen(MountEntity mount) {
        super(new StringTextComponent(""));
        this.mount = mount;
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.imageWidth) / 2;
        this.guiTop = (this.height - this.imageHeight) / 2;

        this.addButton(new Button(this.guiLeft + 5, this.guiTop + 100, 40, 20, new StringTextComponent("Back"), (p_214318_1_) -> {
            int index = this.mount.getTextureType() - 1;
            this.mount.setTextureType(index);
            MountablesMain.LOGGER.debug(index);
        }));

        this.addButton(new Button(this.guiLeft + 5 + 10 + 40, this.guiTop + 100, 40, 20, new StringTextComponent("Next"), (p_214318_1_) -> {
            int index = this.mount.getTextureType() + 1;
            this.mount.setTextureType(index);
            MountablesMain.LOGGER.debug(index);
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.xMouse = (float)p_230430_2_;
        this.yMouse = (float)p_230430_3_;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);

        InventoryScreen.renderEntityInInventory(this.guiLeft + 45, this.guiTop + 45, 90, (float) (this.guiLeft + 45) - this.xMouse, (float) (this.guiTop + 45) - this.yMouse, this.mount);

    }


    // for ? help button
    private static void openLink(String uri) {
        Util.getPlatform().openUri(uri);
    }
}
