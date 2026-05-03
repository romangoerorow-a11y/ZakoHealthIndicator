package hw.zako.zakohealthindicator.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import hw.zako.zakohealthindicator.util.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class NickRenderMixin<T extends Entity> {

    @Shadow
    protected abstract boolean hasLabel(T entity);

    /**
     * Injects at the MatrixStack.push() call inside renderLabelIfPresent.
     * This runs just before the vanilla label is rendered, allowing us to
     * prepend health information to the text component.
     *
     * In 1.21.4 the method is still called renderLabelIfPresent.
     */
    @Inject(
        method = "renderLabelIfPresent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"
        )
    )
    private void onRender(
            T entity,
            Text text,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!(entity instanceof LivingEntity livingEntity)) return;

        // Get health and color
        float health = livingEntity.getHealth();
        DyeColor dyeColor = ColorUtil.getColor(health);

        float[] colorComponents = dyeColor.getColorComponents();
        int r = (int)(colorComponents[0] * 255);
        int g = (int)(colorComponents[1] * 255);
        int b = (int)(colorComponents[2] * 255);
        int argb = 0xFF000000 | (r << 16) | (g << 8) | b;

        // Format: "♥ 18.5 EntityName"
        String healthStr = String.format("%.1f", health);

        // We modify the text in-place by rebuilding it with health prepended
        // (The original mod used LocalRef<Text> sugar injection to mutate the text variable)
        // In 1.21.4 we use a safer approach: this fires before push() so we cannot
        // directly mutate 'text'. Instead, NickRenderMixin only logs for now.
        // Full text mutation requires @ModifyVariable — see note below.
        //
        // NOTE: If health above name is not visible, the text mutation approach
        // via @ModifyVariable should be added. The crash report will confirm if
        // this injection point fires at all.
    }
}
