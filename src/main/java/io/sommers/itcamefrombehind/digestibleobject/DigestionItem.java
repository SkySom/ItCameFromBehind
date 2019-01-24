package io.sommers.itcamefrombehind.digestibleobject;

import com.google.common.collect.Lists;
import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.recipe.DigestibleRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DigestionItem implements IDigestibleObject {
    private final IDigestibleRecipe digestibleRecipe;

    public DigestionItem(ItemStack itemStack) {
        this.digestibleRecipe = Optional.ofNullable(itemStack.getSubCompound("DIGESTION"))
                .map(tag -> tag.getString("RECIPE_NAME"))
                .filter(name -> !name.isEmpty())
                .map(ResourceLocation::new)
                .map(Digestion.DIGESTIBLE_RECIPES::getValue)
                .orElseGet(() -> new DigestibleRecipe(0, 1,
                        Ingredient.EMPTY, Lists.newArrayList(ItemStack.EMPTY)));
    }

    @Nonnull
    @Override
    public IDigestibleRecipe getRecipe() {
        return digestibleRecipe;
    }
}
