package offhandnoplace.mixin.early;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import offhandnoplace.PlacementRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.multiplayer.PlayerControllerMP", remap = false)
public abstract class PlayerControllerMixin {
    // onPlayerRightClick sends C08 before calling ItemStack.tryPlaceItemIntoWorld.
    @Inject(method = "func_78760_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$beforeBlockClick(EntityPlayer player, World world, ItemStack stack,
            int x, int y, int z, int side, Vec3 hit, CallbackInfoReturnable<Boolean> callback) {
        if (stack != null && PlacementRules.isOffhand(player) && PlacementRules.placesOnBlock(stack)) {
            callback.setReturnValue(false);
        }
    }

    // sendUseItem also sends C08 before calling ItemStack.useItemRightClick.
    @Inject(method = "func_78769_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$beforeItemUse(EntityPlayer player, World world, ItemStack stack,
            CallbackInfoReturnable<Boolean> callback) {
        if (stack != null && PlacementRules.isOffhand(player) && PlacementRules.placesFromRightClick(stack.func_77973_b())) {
            callback.setReturnValue(false);
        }
    }
}
