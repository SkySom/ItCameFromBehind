package io.sommers.itcamefrombehind.recipe;

import com.google.gson.annotations.JsonAdapter;
import io.sommers.itcamefrombehind.NonnullListCollector;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.api.recipes.IDigestingProgress;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DigestibleRecipe extends IForgeRegistryEntry.Impl<IDigestibleRecipe> implements IDigestibleRecipe {
    private final int minimumTicks;
    private final int maximumTicks;
    private final Ingredient input;
    private final List<ItemStack> outputs;

    public DigestibleRecipe(int minimumTicks, int maximumTicks, Ingredient input, List<ItemStack> outputs) {
        this.minimumTicks = minimumTicks;
        this.maximumTicks = maximumTicks;
        this.input = input;
        this.outputs = outputs.parallelStream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    @Override
    public boolean matches(ItemStack itemStack) {
        return input.apply(itemStack);
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getOutputs() {
        return outputs.parallelStream()
                .map(ItemStack::copy)
                .collect(new NonnullListCollector<>());
    }

    @Override
    public int getMinimumTicks() {
        return minimumTicks;
    }

    @Override
    public int getMaximumTicks() {
        return maximumTicks;
    }

    @Override
    public boolean canBeDigested(IDigestiveSystem system, ICapabilityProvider provider) {
        return true;
    }

    @Nonnull
    @Override
    public IDigestingProgress beginDigesting() {
        return new DigestingProgress(this);
    }
}
