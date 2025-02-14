package net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DepositFilterLogic extends FilterLogic {
	private Set<ItemStackKey> inventoryFilterStacks = new HashSet<>();

	public DepositFilterLogic(ItemStack upgrade, Consumer<ItemStack> saveHandler, int filterSlotCount) {
		super(upgrade, saveHandler, filterSlotCount);
	}

	public DepositFilterType getDepositFilterType() {
		if (shouldFilterByInventory()) {
			return DepositFilterType.INVENTORY;
		}
		return isAllowList() ? DepositFilterType.ALLOW : DepositFilterType.BLOCK;
	}

	public void setDepositFilterType(DepositFilterType depositFilterType) {
		switch (depositFilterType) {
			case ALLOW:
				setFilterByInventory(false);
				setAllowList(true);
				break;
			case BLOCK:
				setFilterByInventory(false);
				setAllowList(false);
				break;
			case INVENTORY:
			default:
				setFilterByInventory(true);
				save();
		}
	}

	public void setInventory(SlottedStorage<ItemVariant> inventory) {
		inventoryFilterStacks = InventoryHelper.getUniqueStacks(inventory);
	}

	@Override
	public boolean matchesFilter(ItemStack stack) {
		if (!shouldFilterByInventory()) {
			return super.matchesFilter(stack);
		}

		for (ItemStackKey filterStack : inventoryFilterStacks) {
			if (stackMatchesFilter(stack, filterStack.getStack())) {
				return true;
			}
		}
		return false;
	}

	private void setFilterByInventory(boolean filterByInventory) {
		NBTHelper.setBoolean(upgrade, "filterByInventory", filterByInventory);
		save();
	}

	private boolean shouldFilterByInventory() {
		return NBTHelper.getBoolean(upgrade, "filterByInventory").orElse(false);
	}
}
