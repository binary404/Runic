package binary404.mystica.common.items;

import binary404.mystica.Mystica;
import binary404.mystica.api.item.WandCap;
import binary404.mystica.api.item.WandRod;
import binary404.mystica.common.core.MysticaTab;
import binary404.mystica.data.recipe.WandRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item guide;

    public static Item mysticometer;

    public static Item goggles;

    public static Item iron_cap;
    public static Item gold_cap;

    public static Item wand;

    public static WandRod WOOD_ROD;

    public static WandCap IRON_CAP;
    public static WandCap GOLD_CAP;

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(MysticaTab.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        guide = register(r, new ItemGuide(), "guide");
        mysticometer = register(r, new ItemMysticometer(), "mysticometer");
        goggles = register(r, new ItemGoggles(), "goggles");
        iron_cap = register(r, new Item(defaultBuilder().maxStackSize(1)), "iron_cap");
        gold_cap = register(r, new Item(defaultBuilder().maxStackSize(1)), "gold_cap");

        wand = register(r, new ItemWand(defaultBuilder().maxStackSize(1)), "wand");

        WOOD_ROD = new WandRod("wood", 25, new ItemStack(Items.STICK), 1, 0x80530f);
        IRON_CAP = new WandCap("iron", 1.5F, 0, new ItemStack(ModItems.iron_cap), 1, 0x969593);
        GOLD_CAP = new WandCap("gold", 1.3F, 0, new ItemStack(ModItems.gold_cap), 2, 0xfefdb9);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> r = event.getRegistry();

        register(r, WandRecipe.SERIALIZER, "wand_recipe");
    }

}
