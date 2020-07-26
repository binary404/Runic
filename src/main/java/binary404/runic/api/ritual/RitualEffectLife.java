package binary404.runic.api.ritual;

import binary404.runic.api.rune.Rune;
import binary404.runic.api.rune.Runes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.List;

public class RitualEffectLife extends RitualEffect {

    public RitualEffectLife() {
        super(Runes.LIFE);
    }

    @Override
    public void doEffect(World world, BlockPos pos, RitualProperties properties) {
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos).grow(properties.size));
        for (LivingEntity entity : entities) {
            if (entity.isAlive()) {
                if (properties.chaotic) {
                    entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, properties.potency));
                    entity.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 200, properties.potency));
                    entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 200, properties.potency));
                } else {
                    entity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, properties.potency));
                }
            }
        }
    }
}
