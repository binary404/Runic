package binary404.mystica.common.items;

import binary404.mystica.api.item.IReaveler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import javax.annotation.Nullable;

public class ItemGoggles extends ArmorItem implements IReaveler {

    public ItemGoggles() {
        super(ArmorMaterial.CHAIN, EquipmentSlotType.HEAD, ModItems.defaultBuilder().maxStackSize(1).maxDamage(400).rarity(Rarity.RARE));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "mystica:textures/entity/armor/goggles.png";
    }

    @Override
    public boolean showNodes(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public double viewDistance() {
        return 8000;
    }
}
