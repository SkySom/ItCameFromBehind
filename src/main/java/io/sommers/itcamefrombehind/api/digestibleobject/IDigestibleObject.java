package io.sommers.itcamefrombehind.api.digestibleobject;

import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;

public interface IDigestibleObject {
    default boolean canBeDigested(IDigestiveSystem digestionSystem, ICapabilityProvider provider) {
        return this.getRecipe().canBeDigested(digestionSystem, provider);
    }

    @Nonnull
    IDigestibleRecipe getRecipe();
}
