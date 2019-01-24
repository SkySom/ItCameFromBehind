package io.sommers.itcamefrombehind.digestivesystem;

import com.google.common.collect.Lists;
import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.api.recipes.IDigestingProgress;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Objects;

public class DigestiveSystem implements IDigestiveSystem, INBTSerializable<NBTTagCompound> {
    private final List<IDigestingProgress> digestiveProgresses;
    private final float baseSpeedModifier;

    public DigestiveSystem() {
        this(1.0F);
    }

    public DigestiveSystem(float baseSpeedModifier) {
        digestiveProgresses = Lists.newArrayList();
        this.baseSpeedModifier = baseSpeedModifier;
    }

    @Override
    public boolean canDigest(IDigestibleObject digestibleObject, ICapabilityProvider provider) {
        return digestiveProgresses.size() < 3;
    }

    @Override
    public void beginDigestion(IDigestibleObject digestibleObject, ICapabilityProvider provider) {
        if (canDigest(digestibleObject, provider)) {
            digestiveProgresses.add(digestibleObject.getRecipe().beginDigesting());
        }
    }

    @Override
    public List<IDigestingProgress> getCurrentlyDigesting() {
        return digestiveProgresses;
    }

    @Override
    public float getBaseSpeedModifier() {
        return baseSpeedModifier;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        NBTTagList digestiveProgressTag = new NBTTagList();
        for (IDigestingProgress progress : digestiveProgresses) {
            NBTTagCompound progressCompound = new NBTTagCompound();
            progressCompound.setTag("RECIPE_NAME", new NBTTagString(
                    Objects.requireNonNull(progress.getRecipe().getRegistryName()).toString()));
            progressCompound.setTag("PROGRESS", progress.serializeNBT());
        }
        nbtTagCompound.setTag("DIGESTIVE_PROGRESSES", digestiveProgressTag);
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList recipeProgresses = nbt.getTagList("DIGESTIVE_PROGRESSES", 10);
        for (int i = 0; i < recipeProgresses.tagCount(); i++) {
            NBTTagCompound progressCompound = recipeProgresses.getCompoundTagAt(i);
            IDigestibleRecipe recipe = Digestion.DIGESTIBLE_RECIPES.getValue(
                    new ResourceLocation(progressCompound.getString("RECIPE_NAME")));
            if (recipe != null) {
                IDigestingProgress progress = recipe.beginDigesting();
                progress.deserializeNBT(nbt.getCompoundTag("PROGRESS"));
                this.digestiveProgresses.add(progress);
            }
        }
    }
}
