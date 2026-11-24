package offhandnoplace.mixin.early;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import offhandnoplace.PlacementRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin {
    // ItemStack.tryPlaceItemIntoWorld runs after the clicked block's normal activation.
    @Inject(method = "func_77943_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$blockPlacement(EntityPlayer player, World world, int x, int y, int z,
            int face, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> callback) {
        if (PlacementRules.isOffhand(player) && PlacementRules.placesOnBlock((ItemStack) (Object) this)) {
            callback.setReturnValue(false);
        }
    }

    // ItemStack.useItemRightClick covers the ray-traced placement items.
    @Inject(method = "func_77957_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void offhandnoplace$rayTracedPlacement(World world, EntityPlayer player,
            CallbackInfoReturnable<ItemStack> callback) {
        ItemStack stack = (ItemStack) (Object) this;
        if (PlacementRules.isOffhand(player) && PlacementRules.placesFromRightClick(stack.func_77973_b())) {
            callback.setReturnValue(stack);
        }
    }
}
