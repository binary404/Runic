package binary404.mystica.common.core.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ShapedArcaneRecipeBuilder {

    private final Item result;
    private final int count;
    private int mysticCost;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

    public ShapedArcaneRecipeBuilder(IItemProvider resultIn, int count) {
        this.result = resultIn.asItem();
        this.count = count;
    }

    public static ShapedArcaneRecipeBuilder arcaneRecipe(IItemProvider resultIn) {
        return arcaneRecipe(resultIn, 1);
    }

    public static ShapedArcaneRecipeBuilder arcaneRecipe(IItemProvider resultIn, int countIn) {
        return new ShapedArcaneRecipeBuilder(resultIn, countIn);
    }

    public ShapedArcaneRecipeBuilder key(Character symbol, ITag<Item> tagIn) {
        return this.key(symbol, Ingredient.fromTag(tagIn));
    }

    public ShapedArcaneRecipeBuilder key(Character symbol, IItemProvider itemIn) {
        return this.key(symbol, Ingredient.fromItems(itemIn));
    }

    public ShapedArcaneRecipeBuilder key(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalStateException("Symbol '" + symbol + "' is already definied!");
        } else if (symbol == ' ') {
            throw new IllegalStateException("Symbol ' ' is reversed and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    public ShapedArcaneRecipeBuilder patternLine(String patternIn) {
        if (!this.pattern.isEmpty() && patternIn.length() != this.pattern.get(0).length()) {
            throw new IllegalStateException("Pattern must be the same width on every line");
        } else {
            this.pattern.add(patternIn);
            return this;
        }
    }

    public ShapedArcaneRecipeBuilder mysticCost(int mysticCost) {
        this.mysticCost = mysticCost;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation location = Registry.ITEM.getKey(this.result);
        if ((new ResourceLocation(save)).equals(location)) {
            throw new IllegalStateException("Shaped recipe should remove its save argument");
        } else {
            this.build(consumerIn, new ResourceLocation(save));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        consumerIn.accept(new ShapedArcaneRecipeBuilder.Result(id, this.result, this.count, this.mysticCost, this.pattern, this.key));
    }

    private void validate(ResourceLocation id) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for (String s : this.pattern) {
                for (int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
            } else if (this.pattern.size() == 1 && this.pattern.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.mysticCost < 0) {
                throw new IllegalStateException("No Mystic defined");
            }
        }
    }

    public class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final int mystic;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;

        public Result(ResourceLocation id, Item result, int count, int mystic, List<String> pattern, Map<Character, Ingredient> key) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.mystic = mystic;
            this.pattern = pattern;
            this.key = key;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("mystic", this.mystic);

            JsonArray array = new JsonArray();

            for (String s : this.pattern) {
                array.add(s);
            }

            json.add("pattern", array);
            JsonObject object = new JsonObject();

            for (Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                object.add(String.valueOf(entry.getKey()), entry.getValue().serialize());
            }

            json.add("key", object);

            JsonObject item = new JsonObject();
            item.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                item.addProperty("count", this.count);
            }

            json.add("result", item);
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ShapedArcaneRecipe.SERIALIZER;
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }

}
