package net.p3pp3rf1y.sophisticatedbackpacks.compat.rei;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.common.BackpackWrapperLookup;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.compat.rei.ReiGridMenuInfo;

import java.util.function.Function;

public class REICompat implements REIServerPlugin {
    @Override
    public double getPriority() {
        return 0D;
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        EntryComparator<Tag> nbt = EntryComparator.nbt();
        Function<ItemStack, CompoundTag> colorTag = stack -> {
            CompoundTag tag = new CompoundTag();
            BackpackWrapperLookup.get(stack)
                    .ifPresent(wrapper -> {
                        tag.putInt("clothColor", wrapper.getMainColor());
                        tag.putInt("borderColor", wrapper.getAccentColor());
                    });
            return tag;
        };

        registry.register((context, stack) -> nbt.hash(context, colorTag.apply(stack)), ModItems.BACKPACKS);
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(BuiltinPlugin.CRAFTING, BackpackContainer.class, SimpleMenuInfoProvider.of(ReiGridMenuInfo::new));
    }
}
