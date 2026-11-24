package offhandnoplace.mixin.late;

import crazypants.enderio.conduit.IConduitBundle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import offhandnoplace.PlacementRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "crazypants.enderio.conduit.BlockConduitBundle", remap = false)
public abstract class EnderConduitBundleMixin {
    @Inject(method = "handleConduitClick", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$addConduit(World world, int x, int y, int z, EntityPlayer player,
            IConduitBundle bundle, ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player)) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "handleFacadeClick", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$applyFacade(World world, int x, int y, int z, EntityPlayer player,
            int side, IConduitBundle bundle, ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player)) {
            callback.setReturnValue(false);
        }
    }
}
