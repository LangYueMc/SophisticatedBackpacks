package net.p3pp3rf1y.sophisticatedbackpacks.init;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.common.components.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackSettingsScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.SBPButtonDefinitions;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackSettingsContainerMenu;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.BackpackDyeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.BackpackUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.BasicBackpackRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.SmithingBackpackUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.deposit.DepositUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.everlasting.EverlastingBackpackItemEntity;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.everlasting.EverlastingUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.inception.InceptionUpgradeContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.inception.InceptionUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.inception.InceptionUpgradeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.inception.InceptionUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.refill.RefillUpgradeContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.refill.RefillUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.refill.RefillUpgradeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.refill.RefillUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.restock.RestockUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.restock.RestockUpgradeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.restock.RestockUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.toolswapper.ToolSwapperUpgradeContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.toolswapper.ToolSwapperUpgradeItem;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.toolswapper.ToolSwapperUpgradeTab;
import net.p3pp3rf1y.sophisticatedbackpacks.upgrades.toolswapper.ToolSwapperUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ContentsFilteredUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryInventoryPart;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.battery.BatteryUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.compacting.CompactingUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.compacting.CompactingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.compacting.CompactingUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.compacting.CompactingUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoBlastingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoCookingUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoCookingUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoCookingUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoSmeltingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.AutoSmokingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.BlastingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.SmeltingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.SmokingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.crafting.CraftingUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.crafting.CraftingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.crafting.CraftingUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.crafting.CraftingUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.filter.FilterUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.filter.FilterUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.filter.FilterUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.pickup.PickupUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.pickup.PickupUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.pickup.PickupUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stonecutter.StonecutterUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stonecutter.StonecutterUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stonecutter.StonecutterUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stonecutter.StonecutterUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeContainer;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeTab;
import net.p3pp3rf1y.sophisticatedcore.upgrades.voiding.VoidUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.ColorHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ModItems {
	private ModItems() {}

	private static final LazyRegistrar<Item> ITEMS = LazyRegistrar.create(BuiltInRegistries.ITEM, SophisticatedBackpacks.ID);
	private static final LazyRegistrar<MenuType<?>> MENU_TYPES = LazyRegistrar.create(BuiltInRegistries.MENU, SophisticatedBackpacks.ID);
	private static final LazyRegistrar<EntityType<?>> ENTITY_TYPES = LazyRegistrar.create(BuiltInRegistries.ENTITY_TYPE, SophisticatedBackpacks.ID);

	public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack",
			() -> new BackpackItem(Config.SERVER.leatherBackpack.inventorySlotCount::get, Config.SERVER.leatherBackpack.upgradeSlotCount::get, ModBlocks.BACKPACK));
	public static final RegistryObject<BackpackItem> IRON_BACKPACK = ITEMS.register("iron_backpack",
			() -> new BackpackItem(Config.SERVER.ironBackpack.inventorySlotCount::get, Config.SERVER.ironBackpack.upgradeSlotCount::get, ModBlocks.IRON_BACKPACK));
	public static final RegistryObject<BackpackItem> GOLD_BACKPACK = ITEMS.register("gold_backpack",
			() -> new BackpackItem(Config.SERVER.goldBackpack.inventorySlotCount::get, Config.SERVER.goldBackpack.upgradeSlotCount::get, ModBlocks.GOLD_BACKPACK));
	public static final RegistryObject<BackpackItem> DIAMOND_BACKPACK = ITEMS.register("diamond_backpack",
			() -> new BackpackItem(Config.SERVER.diamondBackpack.inventorySlotCount::get, Config.SERVER.diamondBackpack.upgradeSlotCount::get, ModBlocks.DIAMOND_BACKPACK));
	public static final RegistryObject<BackpackItem> NETHERITE_BACKPACK = ITEMS.register("netherite_backpack",
			() -> new BackpackItem(Config.SERVER.netheriteBackpack.inventorySlotCount::get, Config.SERVER.netheriteBackpack.upgradeSlotCount::get, ModBlocks.NETHERITE_BACKPACK, Item.Properties::fireResistant));

	public static final ResourceLocation BACKPACK_UPGRADE_TAG_NAME = new ResourceLocation(SophisticatedBackpacks.ID, "upgrade");

	public static final TagKey<Item> BACKPACK_UPGRADE_TAG = TagKey.create(Registries.ITEM, BACKPACK_UPGRADE_TAG_NAME);

	public static final RegistryObject<PickupUpgradeItem> PICKUP_UPGRADE = ITEMS.register("pickup_upgrade", () -> new PickupUpgradeItem(Config.SERVER.pickupUpgrade.filterSlots::get));
	public static final RegistryObject<PickupUpgradeItem> ADVANCED_PICKUP_UPGRADE = ITEMS.register("advanced_pickup_upgrade", () -> new PickupUpgradeItem(Config.SERVER.advancedPickupUpgrade.filterSlots::get));
	public static final RegistryObject<FilterUpgradeItem> FILTER_UPGRADE = ITEMS.register("filter_upgrade", () -> new FilterUpgradeItem(Config.SERVER.filterUpgrade.filterSlots::get));
	public static final RegistryObject<FilterUpgradeItem> ADVANCED_FILTER_UPGRADE = ITEMS.register("advanced_filter_upgrade", () -> new FilterUpgradeItem(Config.SERVER.advancedFilterUpgrade.filterSlots::get));
	public static final RegistryObject<MagnetUpgradeItem> MAGNET_UPGRADE = ITEMS.register("magnet_upgrade", () -> new MagnetUpgradeItem(Config.SERVER.magnetUpgrade.magnetRange::get, Config.SERVER.magnetUpgrade.filterSlots::get));
	public static final RegistryObject<MagnetUpgradeItem> ADVANCED_MAGNET_UPGRADE = ITEMS.register("advanced_magnet_upgrade", () -> new MagnetUpgradeItem(Config.SERVER.advancedMagnetUpgrade.magnetRange::get, Config.SERVER.advancedMagnetUpgrade.filterSlots::get));
	public static final RegistryObject<FeedingUpgradeItem> FEEDING_UPGRADE = ITEMS.register("feeding_upgrade", () -> new FeedingUpgradeItem(Config.SERVER.feedingUpgrade.filterSlots::get));
	public static final RegistryObject<FeedingUpgradeItem> ADVANCED_FEEDING_UPGRADE = ITEMS.register("advanced_feeding_upgrade", () -> new FeedingUpgradeItem(Config.SERVER.advancedFeedingUpgrade.filterSlots::get));
	public static final RegistryObject<CompactingUpgradeItem> COMPACTING_UPGRADE = ITEMS.register("compacting_upgrade", () -> new CompactingUpgradeItem(false, Config.SERVER.compactingUpgrade.filterSlots::get));
	public static final RegistryObject<CompactingUpgradeItem> ADVANCED_COMPACTING_UPGRADE = ITEMS.register("advanced_compacting_upgrade", () -> new CompactingUpgradeItem(true, Config.SERVER.advancedCompactingUpgrade.filterSlots::get));
	public static final RegistryObject<VoidUpgradeItem> VOID_UPGRADE = ITEMS.register("void_upgrade", () -> new VoidUpgradeItem(Config.SERVER.voidUpgrade));
	public static final RegistryObject<VoidUpgradeItem> ADVANCED_VOID_UPGRADE = ITEMS.register("advanced_void_upgrade", () -> new VoidUpgradeItem(Config.SERVER.advancedVoidUpgrade));
	public static final RegistryObject<RestockUpgradeItem> RESTOCK_UPGRADE = ITEMS.register("restock_upgrade", () -> new RestockUpgradeItem(Config.SERVER.restockUpgrade.filterSlots::get));
	public static final RegistryObject<RestockUpgradeItem> ADVANCED_RESTOCK_UPGRADE = ITEMS.register("advanced_restock_upgrade", () -> new RestockUpgradeItem(Config.SERVER.advancedRestockUpgrade.filterSlots::get));
	public static final RegistryObject<DepositUpgradeItem> DEPOSIT_UPGRADE = ITEMS.register("deposit_upgrade", () -> new DepositUpgradeItem(Config.SERVER.depositUpgrade.filterSlots::get));
	public static final RegistryObject<DepositUpgradeItem> ADVANCED_DEPOSIT_UPGRADE = ITEMS.register("advanced_deposit_upgrade", () -> new DepositUpgradeItem(Config.SERVER.advancedDepositUpgrade.filterSlots::get));
	public static final RegistryObject<RefillUpgradeItem> REFILL_UPGRADE = ITEMS.register("refill_upgrade", () -> new RefillUpgradeItem(Config.SERVER.refillUpgrade.filterSlots::get, false, false));
	public static final RegistryObject<RefillUpgradeItem> ADVANCED_REFILL_UPGRADE = ITEMS.register("advanced_refill_upgrade", () -> new RefillUpgradeItem(Config.SERVER.advancedRefillUpgrade.filterSlots::get, true, true));
	public static final RegistryObject<InceptionUpgradeItem> INCEPTION_UPGRADE = ITEMS.register("inception_upgrade", InceptionUpgradeItem::new);
	public static final RegistryObject<EverlastingUpgradeItem> EVERLASTING_UPGRADE = ITEMS.register("everlasting_upgrade", EverlastingUpgradeItem::new);
	public static final RegistryObject<SmeltingUpgradeItem> SMELTING_UPGRADE = ITEMS.register("smelting_upgrade", () -> new SmeltingUpgradeItem(Config.SERVER.smeltingUpgrade));
	public static final RegistryObject<AutoSmeltingUpgradeItem> AUTO_SMELTING_UPGRADE = ITEMS.register("auto_smelting_upgrade", () -> new AutoSmeltingUpgradeItem(Config.SERVER.autoSmeltingUpgrade));
	public static final RegistryObject<SmokingUpgradeItem> SMOKING_UPGRADE = ITEMS.register("smoking_upgrade", () -> new SmokingUpgradeItem(Config.SERVER.smokingUpgrade));
	public static final RegistryObject<AutoSmokingUpgradeItem> AUTO_SMOKING_UPGRADE = ITEMS.register("auto_smoking_upgrade", () -> new AutoSmokingUpgradeItem(Config.SERVER.autoSmokingUpgrade));
	public static final RegistryObject<BlastingUpgradeItem> BLASTING_UPGRADE = ITEMS.register("blasting_upgrade", () -> new BlastingUpgradeItem(Config.SERVER.blastingUpgrade));
	public static final RegistryObject<AutoBlastingUpgradeItem> AUTO_BLASTING_UPGRADE = ITEMS.register("auto_blasting_upgrade", () -> new AutoBlastingUpgradeItem(Config.SERVER.autoBlastingUpgrade));
	public static final RegistryObject<CraftingUpgradeItem> CRAFTING_UPGRADE = ITEMS.register("crafting_upgrade", CraftingUpgradeItem::new);
	public static final RegistryObject<StonecutterUpgradeItem> STONECUTTER_UPGRADE = ITEMS.register("stonecutter_upgrade", StonecutterUpgradeItem::new);
	public static final RegistryObject<StackUpgradeItem> STACK_UPGRADE_TIER_1 = ITEMS.register("stack_upgrade_tier_1", () -> new StackUpgradeItem(2));
	public static final RegistryObject<StackUpgradeItem> STACK_UPGRADE_TIER_2 = ITEMS.register("stack_upgrade_tier_2", () -> new StackUpgradeItem(4));
	public static final RegistryObject<StackUpgradeItem> STACK_UPGRADE_TIER_3 = ITEMS.register("stack_upgrade_tier_3", () -> new StackUpgradeItem(8));
	public static final RegistryObject<StackUpgradeItem> STACK_UPGRADE_TIER_4 = ITEMS.register("stack_upgrade_tier_4", () ->	new StackUpgradeItem(16));
	public static final RegistryObject<JukeboxUpgradeItem> JUKEBOX_UPGRADE = ITEMS.register("jukebox_upgrade", JukeboxUpgradeItem::new);
	public static final RegistryObject<ToolSwapperUpgradeItem> TOOL_SWAPPER_UPGRADE = ITEMS.register("tool_swapper_upgrade",
			() -> new ToolSwapperUpgradeItem(false, false));
	public static final RegistryObject<ToolSwapperUpgradeItem> ADVANCED_TOOL_SWAPPER_UPGRADE = ITEMS.register("advanced_tool_swapper_upgrade",
			() -> new ToolSwapperUpgradeItem(true, true));
	//public static final RegistryObject<TankUpgradeItem> TANK_UPGRADE = ITEMS.register("tank_upgrade", () -> new TankUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.tankUpgrade));
	public static final RegistryObject<BatteryUpgradeItem> BATTERY_UPGRADE = ITEMS.register("battery_upgrade", () -> new BatteryUpgradeItem(Config.SERVER.batteryUpgrade));
/*	public static final RegistryObject<PumpUpgradeItem> PUMP_UPGRADE = ITEMS.register("pump_upgrade", () -> new PumpUpgradeItem(false, false, SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.pumpUpgrade));
	public static final RegistryObject<PumpUpgradeItem> ADVANCED_PUMP_UPGRADE = ITEMS.register("advanced_pump_upgrade", () -> new PumpUpgradeItem(true, true, SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.pumpUpgrade));
	public static final RegistryObject<XpPumpUpgradeItem> XP_PUMP_UPGRADE = ITEMS.register("xp_pump_upgrade", () -> new XpPumpUpgradeItem(SophisticatedBackpacks.ITEM_GROUP, Config.SERVER.xpPumpUpgrade));*/

	public static final RegistryObject<Item> UPGRADE_BASE = ITEMS.register("upgrade_base", () -> new Item(new Item.Properties().stacksTo(16)));

	public static final RegistryObject<MenuType<BackpackContainer>> BACKPACK_CONTAINER_TYPE = MENU_TYPES.register("backpack", () -> new ExtendedScreenHandlerType<>(BackpackContainer::fromBuffer));

	public static final RegistryObject<MenuType<BackpackSettingsContainerMenu>> SETTINGS_CONTAINER_TYPE = MENU_TYPES.register("settings", () -> new ExtendedScreenHandlerType<>(BackpackSettingsContainerMenu::fromBuffer));

	public static final RegistryObject<EntityType<EverlastingBackpackItemEntity>> EVERLASTING_BACKPACK_ITEM_ENTITY = ENTITY_TYPES.register(
			"everlasting_backpack_item", () -> EntityType.Builder.of(EverlastingBackpackItemEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build("")
	);

	private static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = LazyRegistrar.create(BuiltInRegistries.RECIPE_SERIALIZER, SophisticatedBackpacks.ID);

	public static final RegistryObject<SimpleCraftingRecipeSerializer<?>> BACKPACK_DYE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("backpack_dye", () -> new SimpleCraftingRecipeSerializer<>(BackpackDyeRecipe::new));
	public static final RegistryObject<RecipeSerializer<?>> BACKPACK_UPGRADE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("backpack_upgrade", BackpackUpgradeRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<?>> SMITHING_BACKPACK_UPGRADE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("smithing_backpack_upgrade", SmithingBackpackUpgradeRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<?>> BASIC_BACKPACK_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("basic_backpack", BasicBackpackRecipe.Serializer::new);

	public static void registerItemGroup() {
		ItemGroupEvents.modifyEntriesEvent(SophisticatedBackpacks.ITEM_GROUP).register(entries -> {
			ITEMS.getEntries().stream().filter((item) -> !(item.get() instanceof BackpackItem)).forEach((entry) -> entries.prepend(entry.get()));

			entries.acceptAll(coloredItems(ModItems.BACKPACK.get()));
			entries.acceptAll(coloredItems(ModItems.IRON_BACKPACK.get()));
			entries.acceptAll(coloredItems(ModItems.GOLD_BACKPACK.get()));
			entries.acceptAll(coloredItems(ModItems.DIAMOND_BACKPACK.get()));
			entries.acceptAll(coloredItems(ModItems.NETHERITE_BACKPACK.get()));
		});
	}

	private static Collection<ItemStack> coloredItems(Item item) {
		if (!net.p3pp3rf1y.sophisticatedcore.Config.SERVER.enabledItems.isItemEnabled(item)) {
			return Collections.emptyList();
		}

		List<ItemStack> items = new ArrayList<>();
		ItemStack stack = new ItemStack(item);
		items.add(stack);

		for (DyeColor color : DyeColor.values()) {
			stack = new ItemStack(item);
			new BackpackWrapper(stack).setColors(ColorHelper.getColor(color.getTextureDiffuseColors()), ColorHelper.getColor(color.getTextureDiffuseColors()));
			items.add(stack);
		}

		int clothColor = ColorHelper.calculateColor(BackpackWrapper.DEFAULT_CLOTH_COLOR, BackpackWrapper.DEFAULT_CLOTH_COLOR, List.of(DyeColor.BLUE, DyeColor.YELLOW, DyeColor.LIME));
		int trimColor = ColorHelper.calculateColor(BackpackWrapper.DEFAULT_BORDER_COLOR, BackpackWrapper.DEFAULT_BORDER_COLOR, List.of(DyeColor.BLUE, DyeColor.BLACK));

		stack = new ItemStack(item);
		new BackpackWrapper(stack).setColors(clothColor, trimColor);
		items.add(stack);

		return items;
	}

	public static void registerHandlers() {
		ITEMS.register();
		MENU_TYPES.register();
		ENTITY_TYPES.register();
		RECIPE_SERIALIZERS.register();

		registerContainers();
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(ModItems::onResourceReload);
	}


	private static void onResourceReload(MinecraftServer server, CloseableResourceManager resourceManager, boolean success) {
		BackpackUpgradeRecipe.REGISTERED_RECIPES.clear();
		SmithingBackpackUpgradeRecipe.REGISTERED_RECIPES.clear();
	}

	private static final UpgradeContainerType<PickupUpgradeWrapper, ContentsFilteredUpgradeContainer<PickupUpgradeWrapper>> PICKUP_BASIC_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
	private static final UpgradeContainerType<PickupUpgradeWrapper, ContentsFilteredUpgradeContainer<PickupUpgradeWrapper>> PICKUP_ADVANCED_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
	private static final UpgradeContainerType<MagnetUpgradeWrapper, MagnetUpgradeContainer> MAGNET_BASIC_TYPE = new UpgradeContainerType<>(MagnetUpgradeContainer::new);
	private static final UpgradeContainerType<MagnetUpgradeWrapper, MagnetUpgradeContainer> MAGNET_ADVANCED_TYPE = new UpgradeContainerType<>(MagnetUpgradeContainer::new);
	private static final UpgradeContainerType<FeedingUpgradeWrapper, FeedingUpgradeContainer> FEEDING_TYPE = new UpgradeContainerType<>(FeedingUpgradeContainer::new);
	private static final UpgradeContainerType<FeedingUpgradeWrapper, FeedingUpgradeContainer> ADVANCED_FEEDING_TYPE = new UpgradeContainerType<>(FeedingUpgradeContainer::new);
	private static final UpgradeContainerType<CompactingUpgradeWrapper, CompactingUpgradeContainer> COMPACTING_TYPE = new UpgradeContainerType<>(CompactingUpgradeContainer::new);
	private static final UpgradeContainerType<CompactingUpgradeWrapper, CompactingUpgradeContainer> ADVANCED_COMPACTING_TYPE = new UpgradeContainerType<>(CompactingUpgradeContainer::new);
	private static final UpgradeContainerType<VoidUpgradeWrapper, VoidUpgradeContainer> VOID_TYPE = new UpgradeContainerType<>(VoidUpgradeContainer::new);
	private static final UpgradeContainerType<VoidUpgradeWrapper, VoidUpgradeContainer> ADVANCED_VOID_TYPE = new UpgradeContainerType<>(VoidUpgradeContainer::new);
	private static final UpgradeContainerType<RestockUpgradeWrapper, ContentsFilteredUpgradeContainer<RestockUpgradeWrapper>> RESTOCK_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
	private static final UpgradeContainerType<RestockUpgradeWrapper, ContentsFilteredUpgradeContainer<RestockUpgradeWrapper>> ADVANCED_RESTOCK_TYPE = new UpgradeContainerType<>(ContentsFilteredUpgradeContainer::new);
	private static final UpgradeContainerType<DepositUpgradeWrapper, DepositUpgradeContainer> DEPOSIT_TYPE = new UpgradeContainerType<>(DepositUpgradeContainer::new);
	private static final UpgradeContainerType<DepositUpgradeWrapper, DepositUpgradeContainer> ADVANCED_DEPOSIT_TYPE = new UpgradeContainerType<>(DepositUpgradeContainer::new);
	private static final UpgradeContainerType<RefillUpgradeWrapper, RefillUpgradeContainer> REFILL_TYPE = new UpgradeContainerType<>(RefillUpgradeContainer::new);
	private static final UpgradeContainerType<RefillUpgradeWrapper, RefillUpgradeContainer> ADVANCED_REFILL_TYPE = new UpgradeContainerType<>(RefillUpgradeContainer::new);
	private static final UpgradeContainerType<CookingUpgradeWrapper.SmeltingUpgradeWrapper, CookingUpgradeContainer<SmeltingRecipe, CookingUpgradeWrapper.SmeltingUpgradeWrapper>> SMELTING_TYPE = new UpgradeContainerType<>(CookingUpgradeContainer::new);
	private static final UpgradeContainerType<AutoCookingUpgradeWrapper.AutoSmeltingUpgradeWrapper, AutoCookingUpgradeContainer<SmeltingRecipe, AutoCookingUpgradeWrapper.AutoSmeltingUpgradeWrapper>> AUTO_SMELTING_TYPE = new UpgradeContainerType<>(AutoCookingUpgradeContainer::new);
	private static final UpgradeContainerType<CookingUpgradeWrapper.SmokingUpgradeWrapper, CookingUpgradeContainer<SmokingRecipe, CookingUpgradeWrapper.SmokingUpgradeWrapper>> SMOKING_TYPE = new UpgradeContainerType<>(CookingUpgradeContainer::new);
	private static final UpgradeContainerType<AutoCookingUpgradeWrapper.AutoSmokingUpgradeWrapper, AutoCookingUpgradeContainer<SmokingRecipe, AutoCookingUpgradeWrapper.AutoSmokingUpgradeWrapper>> AUTO_SMOKING_TYPE = new UpgradeContainerType<>(AutoCookingUpgradeContainer::new);
	private static final UpgradeContainerType<CookingUpgradeWrapper.BlastingUpgradeWrapper, CookingUpgradeContainer<BlastingRecipe, CookingUpgradeWrapper.BlastingUpgradeWrapper>> BLASTING_TYPE = new UpgradeContainerType<>(CookingUpgradeContainer::new);
	private static final UpgradeContainerType<AutoCookingUpgradeWrapper.AutoBlastingUpgradeWrapper, AutoCookingUpgradeContainer<BlastingRecipe, AutoCookingUpgradeWrapper.AutoBlastingUpgradeWrapper>> AUTO_BLASTING_TYPE = new UpgradeContainerType<>(AutoCookingUpgradeContainer::new);
	private static final UpgradeContainerType<CraftingUpgradeWrapper, CraftingUpgradeContainer> CRAFTING_TYPE = new UpgradeContainerType<>(CraftingUpgradeContainer::new);
	private static final UpgradeContainerType<InceptionUpgradeWrapper, InceptionUpgradeContainer> INCEPTION_TYPE = new UpgradeContainerType<>(InceptionUpgradeContainer::new);
	private static final UpgradeContainerType<StonecutterUpgradeWrapper, StonecutterUpgradeContainer> STONECUTTER_TYPE = new UpgradeContainerType<>(StonecutterUpgradeContainer::new);
	private static final UpgradeContainerType<JukeboxUpgradeItem.Wrapper, JukeboxUpgradeContainer> JUKEBOX_TYPE = new UpgradeContainerType<>(JukeboxUpgradeContainer::new);
	private static final UpgradeContainerType<ToolSwapperUpgradeWrapper, ToolSwapperUpgradeContainer> TOOL_SWAPPER_TYPE = new UpgradeContainerType<>(ToolSwapperUpgradeContainer::new);
	//private static final UpgradeContainerType<TankUpgradeWrapper, TankUpgradeContainer> TANK_TYPE = new UpgradeContainerType<>(TankUpgradeContainer::new);
	private static final UpgradeContainerType<BatteryUpgradeWrapper, BatteryUpgradeContainer> BATTERY_TYPE = new UpgradeContainerType<>(BatteryUpgradeContainer::new);
/*	private static final UpgradeContainerType<PumpUpgradeWrapper, PumpUpgradeContainer> PUMP_TYPE = new UpgradeContainerType<>(PumpUpgradeContainer::new);
	private static final UpgradeContainerType<PumpUpgradeWrapper, PumpUpgradeContainer> ADVANCED_PUMP_TYPE = new UpgradeContainerType<>(PumpUpgradeContainer::new);
	private static final UpgradeContainerType<XpPumpUpgradeWrapper, XpPumpUpgradeContainer> XP_PUMP_TYPE = new UpgradeContainerType<>(XpPumpUpgradeContainer::new);*/

	public static void registerContainers() {
		UpgradeContainerRegistry.register(PICKUP_UPGRADE.getId(), PICKUP_BASIC_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_PICKUP_UPGRADE.getId(), PICKUP_ADVANCED_TYPE);
		UpgradeContainerRegistry.register(FILTER_UPGRADE.getId(), FilterUpgradeContainer.BASIC_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_FILTER_UPGRADE.getId(), FilterUpgradeContainer.ADVANCED_TYPE);
		UpgradeContainerRegistry.register(MAGNET_UPGRADE.getId(), MAGNET_BASIC_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_MAGNET_UPGRADE.getId(), MAGNET_ADVANCED_TYPE);
		UpgradeContainerRegistry.register(FEEDING_UPGRADE.getId(), FEEDING_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_FEEDING_UPGRADE.getId(), ADVANCED_FEEDING_TYPE);
		UpgradeContainerRegistry.register(COMPACTING_UPGRADE.getId(), COMPACTING_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_COMPACTING_UPGRADE.getId(), ADVANCED_COMPACTING_TYPE);
		UpgradeContainerRegistry.register(VOID_UPGRADE.getId(), VOID_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_VOID_UPGRADE.getId(), ADVANCED_VOID_TYPE);
		UpgradeContainerRegistry.register(RESTOCK_UPGRADE.getId(), RESTOCK_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_RESTOCK_UPGRADE.getId(), ADVANCED_RESTOCK_TYPE);
		UpgradeContainerRegistry.register(DEPOSIT_UPGRADE.getId(), DEPOSIT_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_DEPOSIT_UPGRADE.getId(), ADVANCED_DEPOSIT_TYPE);
		UpgradeContainerRegistry.register(REFILL_UPGRADE.getId(), REFILL_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_REFILL_UPGRADE.getId(), ADVANCED_REFILL_TYPE);
		UpgradeContainerRegistry.register(SMELTING_UPGRADE.getId(), SMELTING_TYPE);
		UpgradeContainerRegistry.register(AUTO_SMELTING_UPGRADE.getId(), AUTO_SMELTING_TYPE);
		UpgradeContainerRegistry.register(SMOKING_UPGRADE.getId(), SMOKING_TYPE);
		UpgradeContainerRegistry.register(AUTO_SMOKING_UPGRADE.getId(), AUTO_SMOKING_TYPE);
		UpgradeContainerRegistry.register(BLASTING_UPGRADE.getId(), BLASTING_TYPE);
		UpgradeContainerRegistry.register(AUTO_BLASTING_UPGRADE.getId(), AUTO_BLASTING_TYPE);
		UpgradeContainerRegistry.register(CRAFTING_UPGRADE.getId(), CRAFTING_TYPE);
		UpgradeContainerRegistry.register(INCEPTION_UPGRADE.getId(), INCEPTION_TYPE);
		UpgradeContainerRegistry.register(STONECUTTER_UPGRADE.getId(), STONECUTTER_TYPE);
		UpgradeContainerRegistry.register(JUKEBOX_UPGRADE.getId(), JUKEBOX_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_TOOL_SWAPPER_UPGRADE.getId(), TOOL_SWAPPER_TYPE);
		//UpgradeContainerRegistry.register(TANK_UPGRADE.getId(), TANK_TYPE);
		UpgradeContainerRegistry.register(BATTERY_UPGRADE.getId(), BATTERY_TYPE);
/*		UpgradeContainerRegistry.register(PUMP_UPGRADE.getId(), PUMP_TYPE);
		UpgradeContainerRegistry.register(ADVANCED_PUMP_UPGRADE.getId(), ADVANCED_PUMP_TYPE);
		UpgradeContainerRegistry.register(XP_PUMP_UPGRADE.getId(), XP_PUMP_TYPE);*/


		EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> {
			MenuScreens.register(BACKPACK_CONTAINER_TYPE.get(), BackpackScreen::constructScreen);
			MenuScreens.register(SETTINGS_CONTAINER_TYPE.get(), BackpackSettingsScreen::constructScreen);

			UpgradeGuiManager.registerTab(PICKUP_BASIC_TYPE, (ContentsFilteredUpgradeContainer<PickupUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new PickupUpgradeTab.Basic(uc, p, s, Config.SERVER.pickupUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(PICKUP_ADVANCED_TYPE, (ContentsFilteredUpgradeContainer<PickupUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new PickupUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedPickupUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(FilterUpgradeContainer.BASIC_TYPE, (FilterUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new FilterUpgradeTab.Basic(uc, p, s, Config.SERVER.filterUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(FilterUpgradeContainer.ADVANCED_TYPE, (FilterUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new FilterUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedFilterUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(MAGNET_BASIC_TYPE, (MagnetUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new MagnetUpgradeTab.Basic(uc, p, s, Config.SERVER.magnetUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(MAGNET_ADVANCED_TYPE, (MagnetUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new MagnetUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedMagnetUpgrade.slotsInRow.get(), SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(FEEDING_TYPE, (FeedingUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new FeedingUpgradeTab.Basic(uc, p, s, Config.SERVER.feedingUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(ADVANCED_FEEDING_TYPE, (FeedingUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new FeedingUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedFeedingUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(COMPACTING_TYPE, (CompactingUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new CompactingUpgradeTab.Basic(uc, p, s, Config.SERVER.compactingUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(ADVANCED_COMPACTING_TYPE, (CompactingUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new CompactingUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedCompactingUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(VOID_TYPE, (VoidUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new VoidUpgradeTab.Basic(uc, p, s, Config.SERVER.voidUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(ADVANCED_VOID_TYPE, (VoidUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new VoidUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedVoidUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(RESTOCK_TYPE, (ContentsFilteredUpgradeContainer<RestockUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new RestockUpgradeTab.Basic(uc, p, s, SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(ADVANCED_RESTOCK_TYPE, (ContentsFilteredUpgradeContainer<RestockUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new RestockUpgradeTab.Advanced(uc, p, s, SBPButtonDefinitions.BACKPACK_CONTENTS_FILTER_TYPE));
			UpgradeGuiManager.registerTab(DEPOSIT_TYPE, DepositUpgradeTab.Basic::new);
			UpgradeGuiManager.registerTab(ADVANCED_DEPOSIT_TYPE, DepositUpgradeTab.Advanced::new);
			UpgradeGuiManager.registerTab(REFILL_TYPE, (RefillUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new RefillUpgradeTab.Basic(uc, p, s,
					Config.SERVER.refillUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(ADVANCED_REFILL_TYPE, (RefillUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new RefillUpgradeTab.Advanced(uc, p, s,
					Config.SERVER.advancedRefillUpgrade.slotsInRow.get()));
			UpgradeGuiManager.registerTab(SMELTING_TYPE, CookingUpgradeTab.SmeltingUpgradeTab::new);
			UpgradeGuiManager.registerTab(AUTO_SMELTING_TYPE, (AutoCookingUpgradeContainer<SmeltingRecipe, AutoCookingUpgradeWrapper.AutoSmeltingUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new AutoCookingUpgradeTab.AutoSmeltingUpgradeTab(uc, p, s, Config.SERVER.autoSmeltingUpgrade.inputFilterSlotsInRow.get(), Config.SERVER.autoSmeltingUpgrade.fuelFilterSlotsInRow.get()));
			UpgradeGuiManager.registerTab(SMOKING_TYPE, CookingUpgradeTab.SmokingUpgradeTab::new);
			UpgradeGuiManager.registerTab(AUTO_SMOKING_TYPE, (AutoCookingUpgradeContainer<SmokingRecipe, AutoCookingUpgradeWrapper.AutoSmokingUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new AutoCookingUpgradeTab.AutoSmokingUpgradeTab(uc, p, s, Config.SERVER.autoSmokingUpgrade.inputFilterSlotsInRow.get(), Config.SERVER.autoSmokingUpgrade.fuelFilterSlotsInRow.get()));
			UpgradeGuiManager.registerTab(BLASTING_TYPE, CookingUpgradeTab.BlastingUpgradeTab::new);
			UpgradeGuiManager.registerTab(AUTO_BLASTING_TYPE, (AutoCookingUpgradeContainer<BlastingRecipe, AutoCookingUpgradeWrapper.AutoBlastingUpgradeWrapper> uc, Position p, StorageScreenBase<?> s) ->
					new AutoCookingUpgradeTab.AutoBlastingUpgradeTab(uc, p, s, Config.SERVER.autoBlastingUpgrade.inputFilterSlotsInRow.get(), Config.SERVER.autoBlastingUpgrade.fuelFilterSlotsInRow.get()));
			UpgradeGuiManager.registerTab(CRAFTING_TYPE, (CraftingUpgradeContainer uc, Position p, StorageScreenBase<?> s) ->
					new CraftingUpgradeTab(uc, p, s, SBPButtonDefinitions.SHIFT_CLICK_TARGET));
			UpgradeGuiManager.registerTab(INCEPTION_TYPE, InceptionUpgradeTab::new);
			UpgradeGuiManager.registerTab(STONECUTTER_TYPE, (StonecutterUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) ->
					new StonecutterUpgradeTab(upgradeContainer, position, screen, SBPButtonDefinitions.SHIFT_CLICK_TARGET));
			UpgradeGuiManager.registerTab(JUKEBOX_TYPE, JukeboxUpgradeTab::new);
			UpgradeGuiManager.registerTab(TOOL_SWAPPER_TYPE, ToolSwapperUpgradeTab::new);
			//UpgradeGuiManager.registerTab(TANK_TYPE, TankUpgradeTab::new);
			UpgradeGuiManager.registerTab(BATTERY_TYPE, BatteryUpgradeTab::new);
			//UpgradeGuiManager.registerInventoryPart(TANK_TYPE, TankInventoryPart::new);
			UpgradeGuiManager.registerInventoryPart(BATTERY_TYPE, BatteryInventoryPart::new);
/*			UpgradeGuiManager.registerTab(PUMP_TYPE, PumpUpgradeTab.Basic::new);
			UpgradeGuiManager.registerTab(ADVANCED_PUMP_TYPE, PumpUpgradeTab.Advanced::new);
			UpgradeGuiManager.registerTab(XP_PUMP_TYPE, (XpPumpUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) ->
					new XpPumpUpgradeTab(upgradeContainer, position, screen, Config.SERVER.xpPumpUpgrade.mendingOn.get()));*/
		});
	}

	public static void registerDispenseBehavior() {
		DispenserBlock.registerBehavior(BACKPACK.get(), new BackpackDispenseBehavior());
		DispenserBlock.registerBehavior(IRON_BACKPACK.get(), new BackpackDispenseBehavior());
		DispenserBlock.registerBehavior(GOLD_BACKPACK.get(), new BackpackDispenseBehavior());
		DispenserBlock.registerBehavior(DIAMOND_BACKPACK.get(), new BackpackDispenseBehavior());
		DispenserBlock.registerBehavior(NETHERITE_BACKPACK.get(), new BackpackDispenseBehavior());
	}

	public static void registerCauldronInteractions() {
		CauldronInteraction.WATER.put(BACKPACK.get(), new BackpackCauldronInteraction());
		CauldronInteraction.WATER.put(IRON_BACKPACK.get(), new BackpackCauldronInteraction());
		CauldronInteraction.WATER.put(GOLD_BACKPACK.get(), new BackpackCauldronInteraction());
		CauldronInteraction.WATER.put(DIAMOND_BACKPACK.get(), new BackpackCauldronInteraction());
		CauldronInteraction.WATER.put(NETHERITE_BACKPACK.get(), new BackpackCauldronInteraction());
	}

	private static boolean hasDefaultColor(IStorageWrapper wrapper) {
		return wrapper.getAccentColor() == BackpackWrapper.DEFAULT_BORDER_COLOR && wrapper.getMainColor() == BackpackWrapper.DEFAULT_CLOTH_COLOR;
	}

	private static class BackpackCauldronInteraction implements CauldronInteraction {
		@Override
		public InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
			LazyOptional<IBackpackWrapper> backpackWrapperCapability = IBackpackWrapper.maybeGet(stack);
			if (backpackWrapperCapability.map(ModItems::hasDefaultColor).orElse(true)) {
				return InteractionResult.PASS;
			}

			if (!level.isClientSide) {
				backpackWrapperCapability.ifPresent(w -> w.setColors(BackpackWrapper.DEFAULT_CLOTH_COLOR, BackpackWrapper.DEFAULT_BORDER_COLOR));
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	private static class BackpackDispenseBehavior extends OptionalDispenseItemBehavior {
		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack) {
			setSuccess(false);
			Item item = stack.getItem();
			if (item instanceof BackpackItem backpackItem) {
				Direction dispenserDirection = source.getBlockState().getValue(DispenserBlock.FACING);
				BlockPos blockpos = source.getPos().relative(dispenserDirection);
				Direction against = source.getLevel().isEmptyBlock(blockpos.below()) ? dispenserDirection.getOpposite() : Direction.UP;

				setSuccess(backpackItem.tryPlace(null, dispenserDirection.getAxis() == Direction.Axis.Y ? Direction.NORTH : dispenserDirection.getOpposite(), new DirectionalPlaceContext(source.getLevel(), blockpos, dispenserDirection, stack, against)).consumesAction());
			}

			return stack;
		}
	}
}
