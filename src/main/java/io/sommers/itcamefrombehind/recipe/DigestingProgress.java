package io.sommers.itcamefrombehind.recipe;

import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.api.recipes.IDigestingProgress;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class DigestingProgress implements IDigestingProgress {
    private static final Random random = new Random();

    private final IDigestibleRecipe digestingRecipe;
    private int ticksRemaining;

    public DigestingProgress(IDigestibleRecipe digestingRecipe) {
        this.digestingRecipe = digestingRecipe;
        this.ticksRemaining = digestingRecipe.getMinimumTicks() +
                random.nextInt(digestingRecipe.getMaximumTicks() -
                        digestingRecipe.getMinimumTicks());
    }

    @Override
    public int getTicksRemaining() {
        return ticksRemaining;
    }

    @Override
    public boolean digest(float digestionModifier) {
        this.ticksRemaining = Math.round(this.ticksRemaining * digestionModifier);
        return ticksRemaining <= 0;
    }

    @Override
    public NonNullList<ItemStack> getOutputs() {
        return this.ticksRemaining > 0 ? null : this.digestingRecipe.getOutputs();
    }

    @Nonnull
    @Override
    public IDigestibleRecipe getRecipe() {
        return digestingRecipe;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setTag("TICKS_REMAINING", new NBTTagInt(ticksRemaining));
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.ticksRemaining = nbt.getInteger("TICKS_REMAINING");
    }
}
