package hw.zako.zakohealthindicator.client.mixin;

import hw.zako.zakohealthindicator.client.ui.HealthBarGUI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class ImGameGudMixin {

    private HealthBarGUI hud;

    /**
     * In 1.21.4 the method signature for renderHealthBar changed.
     * We inject at the HEAD of renderHealthBar.
     *
     * 1.20.2 signature: renderHealthBar(DrawContext, PlayerEntity, int, int, int, int, float, int, int, int, boolean)
     * 1.21.4 signature: renderHealthBar(DrawContext, PlayerEntity, int, int, int, int, float, int, int, int, boolean)
     * The signature is the same but we must match the exact method name in 1.21.4 mappings.
     */
    @Inject(
        method = "renderHealthBar",
        at = @At("HEAD")
    )
    private void render(
            DrawContext context,
            PlayerEntity player,
            int x, int y, int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci
    ) {
        if (hud == null) {
            hud = new HealthBarGUI(MinecraftClient.getInstance());
        }
        hud.render(context);
    }
}
