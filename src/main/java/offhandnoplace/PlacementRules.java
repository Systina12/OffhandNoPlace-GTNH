package offhandnoplace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import xonin.backhand.api.core.IBackhandPlayer;

public final class PlacementRules {
    private static final ClassValue<Boolean> EXTRA_BLOCK_ITEMS = new ClassValue<Boolean>() {
        @Override
        protected Boolean computeValue(Class<?> type) {
            for (Class<?> base = type; base != null && base != Item.class; base = base.getSuperclass()) {
                switch (base.getName()) {
                    case "buildcraft.transport.ItemPipe":
                    case "codechicken.microblock.ItemMicroPart":
                    case "com.creativemd.littletiles.common.items.ItemMultiTiles":
                    case "com.gtnewhorizon.cropsnh.items.ItemGenericSeed":
                    case "crazypants.enderio.conduit.AbstractItemConduit":
                    case "crazypants.enderio.conduit.facade.ItemConduitFacade":
                    case "ganymedes01.etfuturum.items.ItemArmorStand":
                        return true;
                    default:
                        break;
                }
                if (isMultipartItem(base)) {
                    return true;
                }
            }
            return false;
        }
    };

    private PlacementRules() {}

    private static boolean isMultipartItem(Class<?> type) {
        for (Class<?> contract : type.getInterfaces()) {
            if (contract.getName().equals("codechicken.multipart.TItemMultiPart") || isMultipartItem(contract)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOffhand(EntityPlayer player) {
        return player instanceof IBackhandPlayer && ((IBackhandPlayer) player).isUsingOffhand();
    }

    public static boolean placesOnBlock(ItemStack stack) {
        Item item = stack.func_77973_b();
        return item instanceof ItemBlock || item instanceof IPlantable
            || item instanceof ItemBed || item instanceof ItemDoor || item instanceof ItemHangingEntity
            || item instanceof ItemMinecart || item instanceof ItemRedstone || item instanceof ItemReed
            || item instanceof ItemSign || item instanceof ItemSkull || item instanceof ItemEnderEye
            || (item instanceof ItemDye && stack.func_77960_j() == 3) // Cocoa beans
            || item != null && EXTRA_BLOCK_ITEMS.get(item.getClass());
    }

    public static boolean placesFromRightClick(Item item) {
        // Empty buckets still pick up fluids. Other IPlantable items can still be eaten.
        return item instanceof ItemBoat || item instanceof ItemLilyPad
            || item == Items.field_151131_as || item == Items.field_151129_at
            || (item != null && item.getClass().getName().equals("tconstruct.smeltery.items.FilledBucket"));
    }
}
