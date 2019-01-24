package io.sommers.itcamefrombehind.api.digestivesystem;

import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.recipes.IDigestingProgress;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface IDigestiveSystem extends INBTSerializable<NBTTagCompound> {
    boolean canDigest(IDigestibleObject digestibleObject, ICapabilityProvider provider);

    void beginDigestion(IDigestibleObject digestibleObject, ICapabilityProvider provider);

    List<IDigestingProgress> getCurrentlyDigesting();

    default float getBaseSpeedModifier() {
        return 1.0F;
    }
}
