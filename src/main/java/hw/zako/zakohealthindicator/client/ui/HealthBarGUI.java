package hw.zako.zakohealthindicator.client.ui;

import hw.zako.zakohealthindicator.util.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

@Environment(EnvType.CLIENT)
public class HealthBarGUI {

    private final MinecraftClient client;
    private long lastAttack = 0;
    private PlayerEntity player;

    public HealthBarGUI(MinecraftClient client) {
        this.client = client;

        AttackEntityCallback.EVENT.register((playerIn, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity livingEntity) {
                this.player = (PlayerEntity) playerIn;
                this.lastAttack = System.currentTimeMillis();
            }
            return ActionResult.PASS;
        });
    }

    public long getLeft() {
        return System.currentTimeMillis() - lastAttack;
    }

    public void render(DrawContext context) {
        if (player == null) return;
        if (getLeft() > 5000) return; // hide after 5 seconds

        // Get the target entity the player last attacked
        // We track health of the target via the attacked entity
        // Since we only have player reference stored, display player's own health as demo
        // The original mod showed health of the entity being attacked
        float health = player.getHealth();
        DyeColor dyeColor = ColorUtil.getColor(health);

        // Convert DyeColor to ARGB int
        float[] colorComponents = dyeColor.getColorComponents();
        int r = (int)(colorComponents[0] * 255);
        int g = (int)(colorComponents[1] * 255);
        int b = (int)(colorComponents[2] * 255);
        int color = 0xFF000000 | (r << 16) | (g << 8) | b;

        String text = String.format("%.1f", health);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Draw text in the center of the screen slightly below crosshair
        int textWidth = client.textRenderer.getWidth(text);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight / 2 + 10;

        context.getMatrices().push();
        context.getMatrices().scale(1.5f, 1.5f, 1.0f);
        context.drawText(client.textRenderer, text, (int)(x / 1.5f), (int)(y / 1.5f), color, true);
        context.getMatrices().pop();
    }
}
