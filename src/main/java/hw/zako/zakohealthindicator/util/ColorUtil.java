package hw.zako.zakohealthindicator.util;

import net.minecraft.item.DyeItem;
import net.minecraft.util.DyeColor;

public class ColorUtil {
    /**
     * Returns a DyeColor based on current health.
     * Green > 15hp, Yellow > 10hp, Orange > 5hp, Red otherwise.
     * Mapped from original class_124 fields: field_1061=GREEN, field_1065=YELLOW,
     * field_1054=ORANGE, field_1060=RED, field_1077=WHITE (fallback)
     */
    public static DyeColor getColor(float health) {
        if (health > 15.0f) {
            return DyeColor.GREEN;
        } else if (health > 10.0f) {
            return DyeColor.YELLOW;
        } else if (health > 5.0f) {
            return DyeColor.ORANGE;
        } else {
            return DyeColor.RED;
        }
    }
}
