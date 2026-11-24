package offhandnoplace.mixin.late;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import offhandnoplace.PlacementRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "crazypants.enderio.conduit.AbstractItemConduit", remap = false)
public abstract class EnderConduitItemMixin {
    // Replacing an existing conduit uses the Forge hook before block activation.
    @Inject(method = "onItemUseFirst", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$replaceConduit(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player)) {
            callback.setReturnValue(false);
        }
    }
}
