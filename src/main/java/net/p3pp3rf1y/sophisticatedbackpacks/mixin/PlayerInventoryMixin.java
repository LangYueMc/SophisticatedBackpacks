package net.p3pp3rf1y.sophisticatedbackpacks.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


@Mixin(Inventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public NonNullList<ItemStack> armor;

    @Shadow @Final public Player player;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void sophisticatedbackpacks$tick(CallbackInfo ci) {
        armor.forEach(a -> a.onArmorTick(player.level(), player));
    }
}
