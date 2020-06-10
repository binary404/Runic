package binary404.runic.api.research;

import binary404.runic.api.capability.IPlayerKnowledge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class ResearchEvent extends Event {

    private final PlayerEntity player;

    public ResearchEvent(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public boolean isCancelable() {
        return true;
    }

    public static class Research extends ResearchEvent {
        private final String researchKey;

        public Research(PlayerEntity player, String researchKey) {
            super(player);
            this.researchKey = researchKey;
        }

        public String getResearchKey() {
            return this.researchKey;
        }
    }

}
