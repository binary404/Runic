package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.research.ResearchCategories;
import binary404.mystica.api.research.ResearchCategory;
import binary404.mystica.api.research.ResearchEntry;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.research.PacketSyncKnowledge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nonnull;
import java.util.*;

public class PlayerKnowledge implements IPlayerKnowledge {
    private final HashSet<String> research = new HashSet();
    private final Map<String, Integer> stages = new HashMap<String, Integer>();
    private final Map<String, HashSet<IPlayerKnowledge.EnumResearchFlag>> flags = new HashMap<String, HashSet<IPlayerKnowledge.EnumResearchFlag>>();
    private final Map<String, Integer> researchPoints = new HashMap<String, Integer>();

    public PlayerKnowledge() {
    }

    @Override
    public void clear() {
        this.research.clear();
        this.flags.clear();
        this.stages.clear();
        this.researchPoints.clear();
    }

    @Override
    public IPlayerKnowledge.EnumResearchStatus getResearchStatus(@Nonnull String res) {
        if (!this.isResearchKnown(res)) {
            return IPlayerKnowledge.EnumResearchStatus.UNKNOWN;
        }
        ResearchEntry entry = ResearchCategories.getResearch(res);
        if (entry == null || entry.getStages() == null || this.getResearchStage(res) > entry.getStages().length) {
            return IPlayerKnowledge.EnumResearchStatus.COMPLETE;
        }
        return IPlayerKnowledge.EnumResearchStatus.IN_PROGRESS;
    }

    @Override
    public boolean isResearchKnown(String res) {
        if (res == null) {
            return false;
        }
        if (res.equals("")) {
            return true;
        }
        String[] ss = res.split("@");
        if (ss.length > 1 && this.getResearchStage(ss[0]) < NumberUtils.toInt((String) ss[1], (int) 0)) {
            return false;
        }
        return this.research.contains(ss[0]);
    }

    @Override
    public boolean isResearchComplete(String res) {
        return this.getResearchStatus(res) == IPlayerKnowledge.EnumResearchStatus.COMPLETE;
    }

    @Override
    public int getResearchStage(String res) {
        if (res == null || !this.research.contains(res)) {
            return -1;
        }
        Integer stage = this.stages.get(res);
        return stage == null ? 0 : stage;
    }

    @Override
    public boolean setResearchStage(String res, int stage) {
        if (res == null || !this.research.contains(res) || stage <= 0) {
            return false;
        }
        this.stages.put(res, stage);
        return true;
    }

    @Override
    public boolean addResearch(@Nonnull String res) {
        if (!this.isResearchKnown(res)) {
            this.research.add(res);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeResearch(@Nonnull String res) {
        if (this.isResearchKnown(res)) {
            this.research.remove(res);
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public Set<String> getResearchList() {
        return Collections.unmodifiableSet(this.research);
    }

    @Override
    public boolean setResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
        HashSet<IPlayerKnowledge.EnumResearchFlag> list = this.flags.get(res);
        if (list == null) {
            list = new HashSet();
            this.flags.put(res, list);
        }
        if (list.contains((Object) flag)) {
            return false;
        }
        list.add(flag);
        return true;
    }

    @Override
    public boolean clearResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
        HashSet<IPlayerKnowledge.EnumResearchFlag> list = this.flags.get(res);
        if (list != null) {
            boolean b = list.remove((Object) flag);
            if (list.isEmpty()) {
                this.flags.remove(this.research);
            }
            return b;
        }
        return false;
    }

    @Override
    public boolean hasResearchFlag(@Nonnull String res, @Nonnull IPlayerKnowledge.EnumResearchFlag flag) {
        if (this.flags.get(res) != null) {
            return this.flags.get(res).contains((Object) flag);
        }
        return false;
    }

    private String getKey(ResearchCategory category) {
        return (category == null ? "" : category.key + "_POINTS");
    }

    @Override
    public boolean addResearchPoints(@Nonnull ResearchCategory category, int amount) {
        String key = getKey(category);
        int c = getResearchPointsRaw(category);
        if (c + amount < 0)
            return false;
        c += amount;
        this.researchPoints.put(key, c);
        System.out.println(this.researchPoints);
        return true;
    }

    @Override
    public int getResearchPoints(@Nonnull ResearchCategory category) {
        String key = getKey(category);
        int c = this.researchPoints.getOrDefault(key, 0);
        return (int) Math.floor(c / 16F);
    }

    @Override
    public int getResearchPointsRaw(@Nonnull ResearchCategory category) {
        String key = getKey(category);
        return this.researchPoints.getOrDefault(key, 0);
    }

    @Override
    public void sync(@Nonnull ServerPlayerEntity player) {
        PacketHandler.HANDLER.sendTo(new PacketSyncKnowledge(player), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT rootTag = new CompoundNBT();
        ListNBT researchList = new ListNBT();
        for (String resKey : this.research) {
            HashSet<IPlayerKnowledge.EnumResearchFlag> list;
            CompoundNBT tag = new CompoundNBT();
            tag.putString("key", resKey);
            if (this.stages.containsKey(resKey)) {
                tag.putInt("stage", this.stages.get(resKey).intValue());
            }
            if (this.flags.containsKey(resKey) && (list = this.flags.get(resKey)) != null) {
                String fs = "";
                for (IPlayerKnowledge.EnumResearchFlag flag : list) {
                    if (fs.length() > 0) {
                        fs = fs + ",";
                    }
                    fs = fs + flag.name();
                }
                tag.putString("flags", fs);
            }
            researchList.add(tag);
        }
        rootTag.put("research", researchList);

        ListNBT research_points = new ListNBT();
        for (String key : this.researchPoints.keySet()) {
            Integer c = this.researchPoints.get(key);
            if (c != null && c > 0 && key != null && !key.isEmpty()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putString("key", key);
                tag.putInt("amount", c);
                research_points.add(tag);
            }
        }
        rootTag.put("points", research_points);
        return rootTag;
    }


    public void deserializeNBT(CompoundNBT rootTag) {
        if (rootTag == null) {
            return;
        }
        this.clear();
        ListNBT researchList = rootTag.getList("research", 10);
        for (int i = 0; i < researchList.size(); i++) {
            String fs;
            String[] ss;
            CompoundNBT tag = researchList.getCompound(i);
            String know = tag.getString("key");
            if (know == null || this.isResearchKnown(know))
                continue;
            this.research.add(know);
            int stage = tag.getInt("stage");
            if (stage > 0) {
                this.stages.put(know, stage);
            }
            if ((fs = tag.getString("flags")).length() <= 0)
                continue;
            for (String s : ss = fs.split(",")) {
                IPlayerKnowledge.EnumResearchFlag flag = null;
                try {
                    flag = IPlayerKnowledge.EnumResearchFlag.valueOf(s);
                } catch (Exception exception) {
                    // empty catch block
                }
                if (flag != null)
                    this.setResearchFlag(know, flag);
            }
        }

        ListNBT research_points = rootTag.getList("points", 10);
        for (int i = 0; i < research_points.size(); i++) {
            CompoundNBT tag = research_points.getCompound(i);
            String key = tag.getString("key");
            int amount = tag.getInt("amount");
            this.researchPoints.put(key, amount);
        }
        this.addAutoUnlockResearch();
    }

    private void addAutoUnlockResearch() {
        for (ResearchCategory cat : ResearchCategories.researchCategories.values()) {
            for (ResearchEntry ri : cat.research.values()) {
                if (!ri.hasMeta(ResearchEntry.EnumResearchMeta.AUTOUNLOCK)) continue;
                this.addResearch(ri.getKey());
            }
        }
    }

}
