package io.github.lukegrahamlandry.mountables.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import io.github.lukegrahamlandry.mountables.network.NetworkHandler;
import io.github.lukegrahamlandry.mountables.network.UpdateMountSettingsPacket;
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
    protected int imageHeight = 155;
    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation BACKGROUND = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/chip.png");
    private static final ResourceLocation RIGHT = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/right.png");
    private static final ResourceLocation RIGHT_SELECT = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/right_select.png");
    private static final ResourceLocation RIGHT_DISABLED = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/right_disabled.png");
    private static final ResourceLocation LEFT = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/left.png");
    private static final ResourceLocation LEFT_SELECT = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/left_select.png");
    private static final ResourceLocation LEFT_DISABLED = new ResourceLocation(MountablesMain.MOD_ID, "textures/gui/left_disabled.png");
    // disabled tint color: #333333 (https://pinetools.com/colorize-image)
    private static final ITextComponent THE_WORD_TEXTURE = new StringTextComponent("TEXTURE");

    private static final String[] movementTypes = new String[]{
      "Following", "Sitting", "Wandering"
    };


    int textureType;
    int moveType;

    public CommandChipScreen(MountEntity mount) {
        super(new StringTextComponent(""));
        this.mount = mount;
        this.textureType = mount.getTextureType();
        this.moveType = mount.getMovementMode();
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.imageWidth) / 2;
        this.guiTop = (this.height - this.imageHeight) / 2;

        int textureCount = MountTypes.get(mount.getVanillaType()).textureCount;

        Button back = this.addButton(new ModImageButton(this.guiLeft + 10, this.guiTop + 100, 16, 16, new StringTextComponent("Back"), (p_214318_1_) -> {
            this.textureType--;
            this.update();
        }, LEFT, LEFT_SELECT, LEFT_DISABLED));
        back.active = this.textureType > 0;

        Button next = this.addButton(new ModImageButton(this.guiLeft + this.imageWidth - 10 - 16, this.guiTop + 100, 16, 16, new StringTextComponent("Next"), (p_214318_1_) -> {
           this.textureType++;
           this.update();
        }, RIGHT, RIGHT_SELECT, RIGHT_DISABLED));
        next.active = textureCount > (this.textureType + 1);

        // todo: translateable
        ITextComponent moveTypeText = new StringTextComponent(movementTypes[this.moveType]);
        this.addButton(new Button(this.guiLeft + 10, this.guiTop + 100 + 16 + 10, 80, 20, moveTypeText, (p_214318_1_) -> {
            this.moveType = (this.moveType + 1) % movementTypes.length;
            this.update();
        }));
    }

    private void update(){
        NetworkHandler.INSTANCE.sendToServer(new UpdateMountSettingsPacket(this.mount.getId(), this.textureType, this.moveType));
        this.buttons.clear();
        this.init();
        this.mount.setTextureType(this.textureType);  // for render in gui
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.xMouse = (float)p_230430_2_;
        this.yMouse = (float)p_230430_3_;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(BACKGROUND);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);

        InventoryScreen.renderEntityInInventory(this.guiLeft + 50, this.guiTop + 70, (int) (90.0D / (mount.getDimensions(mount.getPose()).height + 1)), (float) (this.guiLeft + 50) - this.xMouse, (float) (this.guiTop + 70) - this.yMouse, this.mount);

        int openWidthForText = this.imageWidth - 20 - 32;
        // int textX = (openWidthForText - this.font.width(THE_WORD_TEXTURE)) / 2;
        // this.font.draw(matrixStack, THE_WORD_TEXTURE, textX, this.guiTop + this.imageWidth + 3, 0x000000);
        drawCenteredString(matrixStack, this.font, THE_WORD_TEXTURE, this.guiLeft + 10 + 16 + (openWidthForText / 2), this.guiTop + this.imageWidth + 3, 0xFFFFFF);
    }

    // for ? help button
    private static void openLink(String uri) {
        Util.getPlatform().openUri(uri);
    }

    class ModImageButton extends Button {
        private final ResourceLocation baseTexture;
        private final ResourceLocation selectedTexture;
        private final ResourceLocation disabledTexture;

        public ModImageButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_, ResourceLocation baseTexture, ResourceLocation selectedTexture, ResourceLocation disabledTexture) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
            this.baseTexture = baseTexture;
            this.selectedTexture = selectedTexture;
            this.disabledTexture = disabledTexture;
        }

        @Override
        public void renderButton(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
            ResourceLocation texture;
            if (this.active){
                if (this.isHovered){
                    texture = this.selectedTexture;
                } else {
                    texture = this.baseTexture;
                }
            } else {
                texture = this.disabledTexture;
            }

            Drawable draw = new Drawable(texture, this.width, this.height);
            draw.draw(this.x, this.y);
        }
    }
}
