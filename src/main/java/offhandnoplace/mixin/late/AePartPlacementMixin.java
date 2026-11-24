package offhandnoplace.mixin.late;

import appeng.api.parts.IPartHost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import offhandnoplace.PlacementRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.parts.PartPlacement", remap = false)
public abstract class AePartPlacementMixin {
    // AE2's event and packet handlers share these entry points.
    @Inject(method = "tryPlace", at = @At("HEAD"), cancellable = true, remap = false)
    private static void offhandnoplace$partPlacement(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, ForgeDirection face, IPartHost host, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player)) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "facadeLogic", at = @At("HEAD"), cancellable = true, remap = false)
    private static void offhandnoplace$facadePlacement(ItemStack stack, EntityPlayer player, ForgeDirection face,
            IPartHost host, boolean client, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player)) {
            callback.setReturnValue(false);
        }
    }
}
