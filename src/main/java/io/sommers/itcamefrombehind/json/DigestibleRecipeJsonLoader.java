package io.sommers.itcamefrombehind.json;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.sommers.itcamefrombehind.ItCameFromBehind;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.recipe.DigestibleRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DigestibleRecipeJsonLoader extends JsonLoader<IDigestibleRecipe> {
    public DigestibleRecipeJsonLoader() {
        super("/digestible_recipes", IDigestibleRecipe.class, ItCameFromBehind.INSTANCE.getLogger()::getLogger);
    }

    @Override
    public Optional<IDigestibleRecipe> parseObject(BufferedReader bufferedReader, JsonContext context) throws IOException {
        JsonElement element = this.gson.fromJson(bufferedReader, JsonElement.class);

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            int minimumTicks = JsonUtils.getInt(jsonObject, "minimumTicks", 1200);
            int maximumTicks = JsonUtils.getInt(jsonObject, "maximumTicks", 12000);
            JsonElement input = jsonObject.get("input");
            if (input != null) {
                JsonElement output = jsonObject.get("output");
                if (output != null) {
                    List<ItemStack> outputs = Lists.newArrayList();
                    if (output.isJsonArray()) {
                        for (JsonElement jsonElement : output.getAsJsonArray()) {
                            if (jsonElement.isJsonObject()) {
                                outputs.add(CraftingHelper.getItemStackBasic(jsonElement.getAsJsonObject(), context));
                            } else {
                                throw new JsonParseException("Failed to Find JSON Object for Output array");
                            }
                        }
                    } else if (output.isJsonObject()) {
                        outputs.add(CraftingHelper.getItemStackBasic(output.getAsJsonObject(), context));
                    } else {
                        throw new JsonParseException("Field 'output' Must be Array or Object");
                    }
                    return Optional.of(new DigestibleRecipe(minimumTicks, maximumTicks,
                            CraftingHelper.getIngredient(input, context), outputs));
                } else {
                    throw new JsonParseException("No Field 'output' Found");
                }
            } else {
                throw new JsonParseException("No Field 'input' Found");
            }
        } else {
            throw new JsonParseException("Json Must be an Object");
        }
    }
}
