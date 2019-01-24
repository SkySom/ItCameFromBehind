package io.sommers.itcamefrombehind.digestibleobject;

import io.sommers.itcamefrombehind.api.Digestion;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DigestibleObjectItemProvider implements ICapabilityProvider {
    private final DigestionItem digestionItem;

    public DigestibleObjectItemProvider(DigestionItem digestionItem) {
        this.digestionItem = digestionItem;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Digestion.CAP_DIGESTIBLE_OBJECT;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == Digestion.CAP_DIGESTIBLE_OBJECT ?
                Digestion.CAP_DIGESTIBLE_OBJECT.cast(digestionItem) : null;
    }
}
