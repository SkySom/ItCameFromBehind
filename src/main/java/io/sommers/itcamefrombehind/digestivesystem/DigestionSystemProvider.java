package io.sommers.itcamefrombehind.digestivesystem;

import io.sommers.itcamefrombehind.api.Digestion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DigestionSystemProvider implements ICapabilitySerializable<NBTTagCompound> {
    private final DigestiveSystem digestiveSystem;

    public DigestionSystemProvider() {
        this(new DigestiveSystem());
    }

    public DigestionSystemProvider(DigestiveSystem digestiveSystem) {
        this.digestiveSystem = digestiveSystem;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Digestion.CAP_DIGESTION_SYSTEM;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == Digestion.CAP_DIGESTION_SYSTEM ?
                Digestion.CAP_DIGESTION_SYSTEM.cast(digestiveSystem) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return digestiveSystem.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        digestiveSystem.deserializeNBT(nbt);
    }
}
