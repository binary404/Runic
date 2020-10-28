package binary404.mystica.common.config;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.internal.CommonInternals;
import binary404.mystica.api.research.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResearchManager {

    public static ConcurrentHashMap<String, Boolean> syncList = new ConcurrentHashMap();
    public static boolean noFlags = false;
    public static LinkedHashSet<Integer> craftingReferences = new LinkedHashSet();

    public static boolean addResearchPoints(PlayerEntity player, ResearchCategory category, int amount) {
        IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(player);
        knowledge.addResearchPoints(category, amount);
        syncList.put(player.getName().getString(), true);
        return true;
    }

    public static boolean completeResearch(PlayerEntity player, String researchkey, boolean sync) {
        boolean b = false;
        while (ResearchManager.progressResearch(player, researchkey, sync)) {
            b = true;
        }
        return b;
    }

    public static boolean completeResearch(PlayerEntity player, String researchkey) {
        boolean b = false;
        while (ResearchManager.progressResearch(player, researchkey, true)) {
            b = true;
        }
        return b;
    }

    public static boolean startResearchWithPopup(PlayerEntity player, String researchkey) {
        boolean b = ResearchManager.progressResearch(player, researchkey, true);
        if (b) {
            IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(player);
            knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.POPUP);
            knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
        }
        return b;
    }

    public static boolean progressResearch(PlayerEntity player, String researchkey) {
        return ResearchManager.progressResearch(player, researchkey, true);
    }

    public static boolean progressResearch(PlayerEntity player, String researchkey, boolean sync) {
        IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(player);
        if (!knowledge.isResearchComplete(researchkey) && ResearchManager.doesPlayerHaveRequisites(player, researchkey)) {
            ResearchEntry re;
            if (MinecraftForge.EVENT_BUS.post((Event) new ResearchEvent.Research(player, researchkey))) {
                return false;
            }
            if (!knowledge.isResearchKnown(researchkey)) {
                knowledge.addResearch(researchkey);
            }
            if ((re = ResearchCategories.getResearch(researchkey)) != null) {
                boolean popups = true;
                if (re.getStages() != null) {
                    int cs = knowledge.getResearchStage(researchkey);
                    ResearchStage currentStage = null;
                    if (cs > 0) {
                        cs = Math.min(cs, re.getStages().length);
                        currentStage = re.getStages()[cs - 1];
                    }
                    if (re.getStages().length == 1 && cs == 0 && re.getStages()[0].getCraft() == null && re.getStages()[0].getObtain() == null && re.getStages()[0].getPoints() == null && re.getStages()[0].getResearch() == null) {
                        ++cs;
                    } else if (re.getStages().length > 1 && re.getStages().length <= cs + 1 && cs < re.getStages().length && re.getStages()[cs].getCraft() == null && re.getStages()[cs].getObtain() == null && re.getStages()[cs].getPoints() == null && re.getStages()[cs].getResearch() == null) {
                        ++cs;
                    }
                    knowledge.setResearchStage(researchkey, Math.min(re.getStages().length + 1, cs + 1));
                    popups = cs >= re.getStages().length;
                    if (popups) {
                        cs = Math.min(cs, re.getStages().length);
                        currentStage = re.getStages()[cs - 1];
                    }
                    int warp = 0;
                    if (currentStage != null) {
                        warp = currentStage.getWarp();
                    }

                    if (currentStage != null) {
                        warp += currentStage.getWarp();
                        //Warp
                    }
                }

                if (popups) {
                    if (sync) {
                        knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.POPUP);
                        if (!noFlags) {
                            knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
                        } else {
                            noFlags = false;
                        }
                        if (re.getRewardItem() != null) {
                            for (ItemStack rs : re.getRewardItem()) {
                                if (!player.inventory.addItemStackToInventory(rs.copy())) {
                                    player.entityDropItem(rs.copy(), 1.0F);
                                }
                            }
                        }
                        if (re.getRewardPoints() != null) {
                            for (ResearchStage.Point rk : re.getRewardPoints()) {
                                addResearchPoints(player, rk.category, 16 * rk.amount);
                            }
                        }
                    }
                    for (String rc : ResearchCategories.researchCategories.keySet()) {
                        block3:
                        for (ResearchEntry ri : ResearchCategories.getResearchCategory((String) rc).research.values()) {
                            if (ri == null || ri.getAddenda() == null || !knowledge.isResearchComplete(ri.getKey()))
                                continue;
                            for (ResearchAddendum addendum : ri.getAddenda()) {
                                if (addendum.getResearch() == null || !Arrays.asList(addendum.getResearch()).contains(researchkey))
                                    continue;
                                TranslationTextComponent text = new TranslationTextComponent("mystica.addaddendum", new Object[]{ri.getLocalizedName()});
                                player.sendMessage((ITextComponent) text, player.getUniqueID());
                                knowledge.setResearchFlag(ri.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE);
                                continue block3;
                            }
                        }
                    }
                }
            }
            if (re != null && re.getSiblings() != null) {
                for (String sibling : re.getSiblings()) {
                    if (knowledge.isResearchComplete(sibling) || !ResearchManager.doesPlayerHaveRequisites(player, sibling))
                        continue;
                    ResearchManager.completeResearch(player, sibling, sync);
                }
            }
            if (sync) {
                syncList.put(player.getName().getString(), true);
                if (re != null) {
                    player.giveExperiencePoints(5);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean doesPlayerHaveRequisites(PlayerEntity player, String key) {
        ResearchEntry ri = ResearchCategories.getResearch(key);
        if (ri == null) {
            return true;
        }
        String[] parents = ri.getParentsStripped();
        return parents != null ? CapabilityHelper.knowsResearchStrict(player, parents) : true;
    }

    public static InputStreamReader getReader(String s) {
        InputStream stream = ResearchManager.class.getClassLoader().getResourceAsStream(s);
        return new InputStreamReader(stream);
    }

    public static void parseAllResearch() {
        Mystica.LOGGER.info("<Mystica-ResearchManager> Started Parsing Research");
        JsonParser parser = new JsonParser();
        for (ResourceLocation loc : CommonInternals.jsonLocs.values()) {
            String s = "/data/" + loc.getNamespace() + "/" + loc.getPath();
            if (!s.endsWith(".json")) {
                s = s + ".json";
            }
            try {
                InputStreamReader reader = getReader(s);
                JsonObject obj = parser.parse((Reader) reader).getAsJsonObject();
                JsonArray entries = obj.get("entries").getAsJsonArray();
                int a = 0;
                for (JsonElement element : entries) {
                    a++;
                    try {
                        JsonObject entry = element.getAsJsonObject();
                        ResearchEntry researchEntry = ResearchManager.parseResearchJson(entry);
                        Mystica.LOGGER.info("Parsed research entry " + researchEntry.getName());
                        ResearchManager.addResearchToCategory(researchEntry);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Mystica.LOGGER.error("Invalid research entry [" + a + "] found in " + loc.toString());
                        a--;
                    }
                }
                Mystica.LOGGER.error("Loaded " + a + " research entries from " + loc.toString());
            } catch (Exception e) {
                Mystica.LOGGER.error("Error parsing all research files");
                e.printStackTrace();
            }
        }
    }

    private static <T> ResearchEntry parseResearchJson(JsonObject obj) throws Exception {
        String[] icons;
        String[] meta;
        Integer[] location;
        ResearchEntry entry = new ResearchEntry();
        entry.setKey(obj.getAsJsonPrimitive("key").getAsString());
        if (entry.getKey() == null) {
            throw new Exception("Invalid key in research JSon");
        }
        entry.setName(obj.getAsJsonPrimitive("name").getAsString());

        entry.setCategory(obj.getAsJsonPrimitive("category").getAsString());
        if (entry.getCategory() == null) {
            throw new Exception("Invalid category in research JSon");
        }

        if (obj.has("icons") && (icons = ResearchManager.arrayJsonToString(obj.get("icons").getAsJsonArray())) != null && icons.length > 0) {
            Object[] ir = new Object[icons.length];
            for (int a = 0; a < icons.length; ++a) {
                Object stack = ResearchManager.parseJSONtoItemStack(icons[a]);
                ir[a] = stack != null && !((ItemStack) stack).isEmpty() ? stack : (icons[a].startsWith("focus") ? icons[a] : new ResourceLocation(icons[a]));
            }
            entry.setIcons(ir);
            System.out.println(ir);
        }

        if (obj.has("parents")) {
            entry.setParents(ResearchManager.arrayJsonToString(obj.get("parents").getAsJsonArray()));
        }

        if (obj.has("siblings")) {
            entry.setSiblings(ResearchManager.arrayJsonToString(obj.get("siblings").getAsJsonArray()));
        }

        if (obj.has("meta") && (meta = ResearchManager.arrayJsonToString(obj.get("meta").getAsJsonArray())) != null && meta.length > 0) {
            ArrayList<ResearchEntry.EnumResearchMeta> metas = new ArrayList<ResearchEntry.EnumResearchMeta>();
            String[] a = meta;
            int stack = a.length;
            for (int i = 0; i < stack; ++i) {
                String s = a[i];
                ResearchEntry.EnumResearchMeta en = ResearchEntry.EnumResearchMeta.valueOf(s.toUpperCase());
                if (en == null) {
                    throw new Exception("Illegal metadata in research JSon");
                }
                metas.add(en);
            }
            entry.setMeta(metas.toArray(new ResearchEntry.EnumResearchMeta[metas.size()]));
        }

        if (obj.has("location") && (location = ResearchManager.arrayJsonToInt(obj.get("location").getAsJsonArray())) != null && location.length == 2) {
            entry.setDisplayColumn(location[0]);
            entry.setDisplayRow(location[1]);
        }

        if (obj.has("reward_item")) {
            entry.setRewardItem(ResearchManager.parseJsonItemList(entry.getKey(), ResearchManager.arrayJsonToString(obj.get("reward_item").getAsJsonArray())));
        }

        if (obj.has("reward_points")) {
            String[] sl = arrayJsonToString(obj.get("reward_points").getAsJsonArray());
            if (sl != null && sl.length > 0) {
                ArrayList<ResearchStage.Point> kl = new ArrayList<>();
                for (String s : sl) {
                    ResearchStage.Point p = ResearchStage.Point.parse(s);
                    if (p != null)
                        kl.add(p);
                }
                if (kl.size() > 0)
                    entry.setRewardPoints(kl.toArray(new ResearchStage.Point[kl.size()]));
            }
        }

        JsonArray stagesJson = obj.get("stages").getAsJsonArray();
        ArrayList<ResearchStage> stages = new ArrayList<ResearchStage>();
        for (JsonElement element : stagesJson) {
            String[] sl2;
            JsonObject stageObj = element.getAsJsonObject();
            ResearchStage stage = new ResearchStage();
            stage.setText(stageObj.getAsJsonPrimitive("text").getAsString());
            if (stage.getText() == null) {
                throw new Exception("Illegal stage text in research JSon");
            }
            if (stageObj.has("recipes")) {
                stage.setRecipes(ResearchManager.arrayJsonToResourceLocations(stageObj.get("recipes").getAsJsonArray()));
            }
            if (stageObj.has("required_item")) {
                stage.setObtain(ResearchManager.parseJsonOreList(entry.getKey(), ResearchManager.arrayJsonToString(stageObj.get("required_item").getAsJsonArray())));
            }
            if (stageObj.has("required_craft")) {
                String[] s1 = ResearchManager.arrayJsonToString(stageObj.get("required_craft").getAsJsonArray());
                stage.setCraft(ResearchManager.parseJsonOreList(entry.getKey(), s1));
                if (stage.getCraft() != null && stage.getCraft().length > 0) {
                    int[] refs = new int[stage.getCraft().length];
                    int q = 0;
                    Object[] arrobject = stage.getCraft();
                    int n = arrobject.length;
                    for (int i = 0; i < n; ++i) {
                        Object stack = arrobject[i];
                        int code = stack instanceof ItemStack ? ResearchManager.createItemStackHash((ItemStack) stack) : ("oredict:" + (String) stack).hashCode();
                        craftingReferences.add(code);
                        refs[q] = code;
                        ++q;
                    }
                    stage.setCraftReference(refs);
                }
            }
            if (stageObj.has("required_points")) {
                String[] sl = arrayJsonToString(stageObj.get("required_points").getAsJsonArray());
                if (sl != null && sl.length > 0) {
                    ArrayList<ResearchStage.Point> pl = new ArrayList<>();
                    for (String s : sl) {
                        ResearchStage.Point p = ResearchStage.Point.parse(s);
                        if (p != null)
                            pl.add(p);
                    }
                    if (pl.size() > 0)
                        stage.setPoints(pl.toArray(new ResearchStage.Point[pl.size()]));
                }
            }

            if (stageObj.has("required_research")) {
                stage.setResearch(ResearchManager.arrayJsonToString(stageObj.get("required_research").getAsJsonArray()));
                if (stage.getResearch() != null && stage.getResearch().length > 0) {
                    String[] rKey = new String[stage.getResearch().length];
                    String[] rIcn = new String[stage.getResearch().length];
                    for (int a = 0; a < stage.getResearch().length; ++a) {
                        String[] ss = stage.getResearch()[a].split(";");
                        rKey[a] = ss[0];
                        rIcn[a] = ss.length > 1 ? ss[1] : null;
                    }
                    stage.setResearch(rKey);
                    stage.setResearchIcon(rIcn);
                }
            }

            if (stageObj.has("warp"))
                stage.setWarp(stageObj.getAsJsonPrimitive("warp").getAsInt());

            stages.add(stage);
        }

        if (stages.size() > 0) {
            entry.setStages(stages.toArray(new ResearchStage[stages.size()]));
        }

        if (obj.get("addenda") != null) {
            JsonArray addendaJson = obj.get("addenda").getAsJsonArray();
            ArrayList<ResearchAddendum> addenda = new ArrayList<ResearchAddendum>();
            for (JsonElement element : addendaJson) {
                JsonObject addendumObj = element.getAsJsonObject();
                ResearchAddendum addendum = new ResearchAddendum();
                addendum.setText(addendumObj.getAsJsonPrimitive("text").getAsString());
                addendum.setTitle(addendumObj.getAsJsonPrimitive("title").getAsString());
                if (addendum.getText() == null) {
                    throw new Exception("Illegal addendum text in research JSon");
                } else if (addendum.getTitle() == null) {
                    throw new Exception("Illegal addendum title in research JSON");
                }
                if (addendumObj.has("recipes")) {
                    addendum.setRecipes(ResearchManager.arrayJsonToResourceLocations(addendumObj.get("recipes").getAsJsonArray()));
                }
                if (addendumObj.has("required_research")) {
                    addendum.setResearch(ResearchManager.arrayJsonToString(addendumObj.get("required_research").getAsJsonArray()));
                }
                addenda.add(addendum);
            }
            if (addenda.size() > 0) {
                entry.setAddenda(addenda.toArray(new ResearchAddendum[addenda.size()]));
            }
        }
        return entry;
    }

    public static int createItemStackHash(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        stack.setCount(1);
        return stack.toString().hashCode();
    }

    private static String[] arrayJsonToString(JsonArray jsonArray) {
        ArrayList<String> out = new ArrayList<String>();
        for (JsonElement element : jsonArray) {
            out.add(element.getAsString());
        }
        return out.size() == 0 ? null : out.toArray(new String[out.size()]);
    }

    private static ResourceLocation[] arrayJsonToResourceLocations(JsonArray jsonArray) {
        ArrayList<ResourceLocation> out = new ArrayList<ResourceLocation>();
        for (JsonElement element : jsonArray) {
            out.add(new ResourceLocation(element.getAsString()));
        }
        return out.size() == 0 ? null : out.toArray(new ResourceLocation[out.size()]);
    }

    private static Integer[] arrayJsonToInt(JsonArray jsonArray) {
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (JsonElement element : jsonArray) {
            out.add(element.getAsInt());
        }
        return out.size() == 0 ? null : out.toArray(new Integer[out.size()]);
    }

    private static ItemStack[] parseJsonItemList(String key, String[] stacks) {
        if (stacks == null || stacks.length == 0) {
            return null;
        }
        ItemStack[] work = new ItemStack[stacks.length];
        int idx = 0;
        for (String s : stacks) {
            ItemStack stack = ResearchManager.parseJSONtoItemStack(s = s.replace("'", "\""));
            if (stack == null || stack.isEmpty()) continue;
            work[idx] = stack;
            ++idx;
        }
        ItemStack[] out = null;
        if (idx > 0) {
            out = Arrays.copyOf(work, idx);
        }
        return out;
    }

    private static Object[] parseJsonOreList(String key, String[] stacks) {
        if (stacks == null || stacks.length == 0) {
            return null;
        }
        Object[] work = new Object[stacks.length];
        int idx = 0;
        for (String s : stacks) {
            if ((s = s.replace("'", "\"")).startsWith("oredict:")) {
                String[] st = s.split(":");
                if (st.length <= 1) continue;
                work[idx] = st[1];
                ++idx;
                continue;
            }
            ItemStack stack = ResearchManager.parseJSONtoItemStack(s);
            if (stack == null || stack.isEmpty()) continue;
            work[idx] = stack;
            ++idx;
        }
        Object[] out = null;
        if (idx > 0) {
            out = Arrays.copyOf(work, idx);
        }
        return out;
    }

    public static ItemStack parseJSONtoItemStack(String entry) {
        if (entry == null) {
            return null;
        }
        String[] split = entry.split(";");
        String name = split[0];
        int num = -1;
        int dam = -1;
        String nbt = null;
        for (int a = 1; a < split.length; ++a) {
            if (split[a].startsWith("{")) {
                nbt = split[a];
                nbt.replaceAll("'", "\"");
                break;
            }
            int q = -1;
            try {
                q = Integer.parseInt(split[a]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (q >= 0 && num < 0) {
                num = q;
                continue;
            }
            if (q < 0 || dam >= 0) continue;
            dam = q;
        }
        if (num < 0) {
            num = 1;
        }
        if (dam < 0) {
            dam = 0;
        }
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
            if (it != null) {
                stack = new ItemStack(it, num);
                if (nbt != null) {
                    stack.setTag(JsonToNBT.getTagFromJson((String) nbt));
                }
            }
        } catch (Exception it) {
        }
        return stack;
    }

    private static void addResearchToCategory(ResearchEntry ri) {
        ResearchCategory rl = ResearchCategories.getResearchCategory(ri.getCategory());
        if (rl != null && !rl.research.containsKey(ri.getKey())) {
            for (ResearchEntry rr : rl.research.values()) {
                if (rr.getDisplayColumn() != ri.getDisplayColumn() || rr.getDisplayRow() != ri.getDisplayRow())
                    continue;
                return;
            }
            rl.research.put(ri.getKey(), ri);
            if (ri.getDisplayColumn() < rl.minDisplayColumn) {
                rl.minDisplayColumn = ri.getDisplayColumn();
            }
            if (ri.getDisplayRow() < rl.minDisplayRow) {
                rl.minDisplayRow = ri.getDisplayRow();
            }
            if (ri.getDisplayColumn() > rl.maxDisplayColumn) {
                rl.maxDisplayColumn = ri.getDisplayColumn();
            }
            if (ri.getDisplayRow() > rl.maxDisplayRow) {
                rl.maxDisplayRow = ri.getDisplayRow();
            }
        } else {
        }
    }

}
