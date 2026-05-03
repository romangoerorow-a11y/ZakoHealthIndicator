package hw.zako.zakohealthindicator.util;

import net.minecraft.util.DyeColor;

public class ColorUtil {
    public static int getColor(float health) {
        if (health > 15.0f) {
            return 0xFF00AA00; // зелёный
        } else if (health > 10.0f) {
            return 0xFFFFFF55; // жёлтый
        } else if (health > 5.0f) {
            return 0xFFFF7F00; // оранжевый
        } else {
            return 0xFFFF5555; // красный
        }
    }
}
