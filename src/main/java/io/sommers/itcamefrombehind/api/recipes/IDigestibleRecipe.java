package io.sommers.itcamefrombehind.api.recipes;

import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface IDigestibleRecipe extends IForgeRegistryEntry<IDigestibleRecipe> {
    boolean matches(ItemStack itemStack);

    @Nonnull
    NonNullList<ItemStack> getOutputs();

    int getMinimumTicks();

    int getMaximumTicks();

    boolean canBeDigested(IDigestiveSystem system, ICapabilityProvider provider);

    @Nonnull
    IDigestingProgress beginDigesting();
}
