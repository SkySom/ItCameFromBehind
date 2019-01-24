package io.sommers.itcamefrombehind.api.digestibleobject;

import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface IDigestibleObject {
    default boolean canBeDigested(IDigestiveSystem digestionSystem, ICapabilityProvider provider) {
        return Optional.ofNullable(this.getRecipe())
                .map(recipe -> recipe.canBeDigested(digestionSystem, provider))
                .orElse(false);
    }

    @Nullable
    IDigestibleRecipe getRecipe();
}
