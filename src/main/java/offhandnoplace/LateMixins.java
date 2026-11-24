package offhandnoplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public final class LateMixins implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.offhandnoplace.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if (loadedMods.contains("appliedenergistics2")) {
            mixins.add("AePartPlacementMixin");
        }
        if (loadedMods.contains("EnderIO")) {
            mixins.add("EnderConduitItemMixin");
            mixins.add("EnderConduitBundleMixin");
        }
        return mixins;
    }
}
