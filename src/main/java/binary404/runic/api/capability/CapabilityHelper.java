package binary404.runic.api.capability;

import binary404.runic.common.core.capability.PlayerKnowledge;
import binary404.runic.common.core.capability.PlayerKnowledgeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class CapabilityHelper {

    public static IPlayerKnowledge getKnowledge(@Nonnull PlayerEntity player) {
        LazyOptional<IPlayerKnowledge> knowledge = player.getCapability(PlayerKnowledgeProvider.KNOWLEDGE);
        return knowledge.orElse(new PlayerKnowledge());
    }

    public static boolean knowsResearch(@Nonnull PlayerEntity player, @Nonnull String... research) {
        for (String r : research) {
            String[] rr;
            if (r.contains("&&")) {
                rr = r.split("&&");
                if (CapabilityHelper.knowsResearch(player, rr))
                    continue;
                return false;
            }
            if (r.contains("||")) {
                for (String str : rr = r.split("||")) {
                    if (!CapabilityHelper.knowsResearch(player, str))
                        continue;
                    return true;
                }
                continue;
            }
            if (CapabilityHelper.getKnowledge(player).isResearchKnown(r))
                continue;
            return false;
        }
        return true;
    }

    public static boolean knowsResearchStrict(@Nonnull PlayerEntity player, @Nonnull String... research) {
        for (String r : research) {
            String[] rr;
            if (r.contains("&&")) {
                rr = r.split("&&");
                if (CapabilityHelper.knowsResearchStrict(player, rr)) continue;
                return false;
            }
            if (r.contains("||")) {
                for (String str : rr = r.split("||")) {
                    if (!CapabilityHelper.knowsResearchStrict(player, str)) continue;
                    return true;
                }
                continue;
            }
            if (!(r.contains("@") ? !CapabilityHelper.getKnowledge(player).isResearchKnown(r) : !CapabilityHelper.getKnowledge(player).isResearchComplete(r)))
                continue;
            return false;
        }
        return true;
    }

}
