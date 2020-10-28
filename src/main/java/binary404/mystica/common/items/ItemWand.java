package binary404.mystica.common.items;

import binary404.mystica.api.aspect.Aspect;
import binary404.mystica.api.aspect.AspectList;
import binary404.mystica.api.aspect.AspectRegistry;
import binary404.mystica.api.item.IWand;
import binary404.mystica.api.item.WandCap;
import binary404.mystica.api.item.WandRod;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemWand extends Item implements IWand {

    public ItemWand(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getMaxMystic(ItemStack stack) {
        return getRod(stack).getCapacity();
    }

    private int getMaxMysticInternal(ItemStack stack) {
        return getMaxMystic(stack) * 100;
    }

    @Override
    public int getMystic(ItemStack stack) {
        return getMysticInternal(stack) / 100;
    }

    private int getMysticInternal(ItemStack is) {
        int out = 0;
        if (is != null && is.hasTag() && is.getTag().contains("mystic"))
            out = is.getTag().getInt("mystic");
        return out;
    }

    private void storeMysticInternal(ItemStack is, int amount) {
        is.setTagInfo("mystic", IntNBT.valueOf(amount));
    }

    @Override
    public boolean consumeMystic(ItemStack stack, PlayerEntity player, int amount, boolean doIt) {
        amount *= 100;
        amount *= getConsumptionRate(stack);
        if (getMysticInternal(stack) >= amount) {
            if (doIt)
                storeMysticInternal(stack, getMysticInternal(stack) - amount);
            return true;
        }
        return false;
    }

    @Override
    public int addMystic(ItemStack stack, int amount) {
        int storeAmount = getMysticInternal(stack) + amount * 100;
        int leftOver = Math.max(storeAmount - getMaxMysticInternal(stack), 0);
        storeMysticInternal(stack, Math.min(storeAmount, getMaxMysticInternal(stack)));
        return (int) Math.floor(leftOver / 100.0D);
    }

    public ItemStack getFocusStack(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("focus")) {
            CompoundNBT nbt = stack.getTag().getCompound("focus");
            return ItemStack.read(nbt);
        }
        return null;
    }

    public void setFocus(ItemStack stack, ItemStack focus) {
        if (focus == null) {
            stack.getTag().remove("focus");
        } else {
            stack.setTagInfo("focus", focus.write(new CompoundNBT()));
        }
    }

    public WandRod getRod(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("rod"))
            return (WandRod) WandRod.rods.get(stack.getTag().getString("rod"));
        return ModItems.WOOD_ROD;
    }

    public float getConsumptionRate(ItemStack is) {
        return getCap(is).getBaseCostModifier();
    }

    public static DecimalFormat formatter = new DecimalFormat("#######.#");

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, list, flagIn);
        int pos = list.size();
        String tt2 = "";
        if (stack.hasTag()) {
            String tt = "";
            int tot = 0;
            int num = 0;
            if (stack.getTag().contains("mystic")) {
                String amount = "" + getMystic(stack);
                String consumption = formatter.format(getConsumptionRate(stack) * 100);
                num++;
                String text = "";
                ItemStack focus = getFocusStack(stack);
                if (focus != null) {
                        /*
                        float amt = ((ItemFocusBasic) focus.getItem()).getVisCost(focus).getAmount(aspect) * mod;
                        if (amt > 0.0F) {
                            text = "§r, " + this.myFormatter.format(amt) + " " + StatCollector.translateToLocal(((ItemFocusBasic) focus.getItem()).isVisCostPerTick(focus) ? "item.Focus.cost2" : "item.Focus.cost1");
                        }
                         */
                }

                if (Screen.hasShiftDown()) {
                    list.add(new StringTextComponent(TextFormatting.LIGHT_PURPLE + " Mystic" + "\u00a7r x " + amount + ", \u00a7o(" + consumption + "% " + I18n.format("mystica.mystic.cost") + ")" + text));
                } else if (tt.length() > 0) tt = tt + " | ";
                tt = tt + amount;
            }
        }
        list.add(pos, new StringTextComponent(TextFormatting.GOLD + I18n.format("item.capacity.text") + " " + getMaxMystic(stack) + "\u00a7r" + tt2));
        if (getCap(stack).getChargeBonus() > 0) {
            list.add(pos + 1, new StringTextComponent(TextFormatting.AQUA + I18n.format("item.chargebonus.text") + " +" + getCap(stack).getChargeBonus() + "\u00a7r"));
        }

        /*
        if (getFocus(stack) != null) {
            list.add(EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GREEN + getFocus(stack).getItemStackDisplayName(getFocusStack(stack)));

            if (Thaumcraft.proxy.isShiftKeyDown())
                getFocus(stack).addFocusInformation(getFocusStack(stack), player, list, par4);
        }
         */
    }

    public void setRod(ItemStack stack, WandRod rod) {
        stack.setTagInfo("rod", StringNBT.valueOf(rod.getTag()));
    }

    public WandCap getCap(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("cap"))
            return (WandCap) WandCap.caps.get(stack.getTag().getString("cap"));
        return ModItems.IRON_CAP;
    }

    public void setCap(ItemStack stack, WandCap cap) {
        stack.setTagInfo("cap", StringNBT.valueOf(cap.getTag()));
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Integer.MAX_VALUE;
    }


    @Override
    public void inventoryTick(ItemStack stack, World worldIn, net.minecraft.entity.Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn.ticksExisted % 80 == 0)
            handleRecharge(worldIn, stack, entityIn.getPosition(), (PlayerEntity) entityIn, 1);
    }

    public void handleRecharge(World world, ItemStack is, BlockPos pos, PlayerEntity player, int amount) {
        if (this.getMystic(is) < this.getMaxMystic(is)) {
            int amt = Math.min(amount, this.getMaxMystic(is) - this.getMystic(is));
            float drained = WeaveHelper.drainMystic(world, pos, amt);
            if (drained > 0) {
                addMystic(is, (int) drained);
                amount -= drained;
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        ItemStack w1 = new ItemStack(this);
        ItemStack w2 = new ItemStack(this);
        ((IWand) w1.getItem()).setCap(w1, ModItems.IRON_CAP);
        ((IWand) w2.getItem()).setCap(w2, ModItems.GOLD_CAP);
        ((IWand) w1.getItem()).addMystic(w1, ((IWand) w1.getItem()).getMaxMystic(w1));
        ((IWand) w2.getItem()).addMystic(w2, ((IWand) w2.getItem()).getMaxMystic(w2));
        items.add(w1);
        items.add(w2);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        String name = I18n.format("item.mystica.wand.name");
        name = name.replace("CAP", I18n.format("item.mystica.wand." + getCap(stack).getTag() + ".cap"));
        String rod = getRod(stack).getTag();
        name = name.replace("ROD", I18n.format("item.mystica.wand." + rod + ".rod"));
        return ITextComponent.getTextComponentOrEmpty(name);
    }
}
