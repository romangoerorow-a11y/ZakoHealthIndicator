package hw.zako.zakohealthindicator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ZakoHealthIndicatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Client init handled in mixin
    }
}
