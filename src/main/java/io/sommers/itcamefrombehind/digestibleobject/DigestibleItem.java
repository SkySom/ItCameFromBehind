package io.sommers.itcamefrombehind.digestibleobject;

import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class DigestibleItem implements IDigestibleObject {
    private final IDigestibleRecipe digestibleRecipe;

    public DigestibleItem(ItemStack itemStack) {
        this.digestibleRecipe = Optional.ofNullable(itemStack.getSubCompound("DIGESTION"))
                .map(tag -> tag.getString("RECIPE_NAME"))
                .filter(name -> !name.isEmpty())
                .map(ResourceLocation::new)
                .map(Digestion.DIGESTIBLE_RECIPES::getValue)
                .orElse(null);
    }

    @Nullable
    @Override
    public IDigestibleRecipe getRecipe() {
        return digestibleRecipe;
    }
}
