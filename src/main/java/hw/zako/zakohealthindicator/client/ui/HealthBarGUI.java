package hw.zako.zakohealthindicator.client.ui;

import hw.zako.zakohealthindicator.util.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class HealthBarGUI {

    private final MinecraftClient client;
    private long lastAttack = 0;
    private LivingEntity target;

    public HealthBarGUI(MinecraftClient client) {
        this.client = client;
        AttackEntityCallback.EVENT.register((playerIn, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity livingEntity) {
                this.target = livingEntity;
                this.lastAttack = System.currentTimeMillis();
            }
            return ActionResult.PASS;
        });
    }

    public void render(DrawContext context) {
        if (target == null) return;
        if (System.currentTimeMillis() - lastAttack > 5000) return;

        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        int color = ColorUtil.getColor(health);

        // Без десятых — просто целое число
        String text = String.valueOf((int) health);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int textWidth = client.textRenderer.getWidth(text);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight / 2 + 10;

        // Чем меньше хп — тем больше текст (от 1.2 до 2.5)
        float ratio = 1.0f - (health / maxHealth);
        float scale = 1.2f + (ratio * 1.3f);

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawText(client.textRenderer, text, (int)(x / scale), (int)(y / scale), color, true);
        context.getMatrices().pop();
    }
}
