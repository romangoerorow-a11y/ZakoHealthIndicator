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

        // Берём хп цели (противника), а не игрока
        float health = target.getHealth();
        int color = ColorUtil.getColor(health);
        String text = String.format("%.1f", health);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int textWidth = client.textRenderer.getWidth(text);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight / 2 + 10;

        context.getMatrices().push();
        context.getMatrices().scale(1.5f, 1.5f, 1.0f);
        context.drawText(client.textRenderer, text, (int)(x / 1.5f), (int)(y / 1.5f), color, true);
        context.getMatrices().pop();
    }
}
