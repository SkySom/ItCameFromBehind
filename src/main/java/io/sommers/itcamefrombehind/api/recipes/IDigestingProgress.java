package io.sommers.itcamefrombehind.api.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public interface IDigestingProgress extends INBTSerializable<NBTTagCompound> {
    int getTicksRemaining();

    boolean digest(float digestionModifier);

    NonNullList<ItemStack> getOutputs();

    @Nonnull
    IDigestibleRecipe getRecipe();
}
