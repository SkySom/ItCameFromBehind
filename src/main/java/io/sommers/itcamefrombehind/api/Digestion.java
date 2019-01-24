package io.sommers.itcamefrombehind.api;

import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class Digestion {
    @CapabilityInject(IDigestiveSystem.class)
    public static Capability<IDigestiveSystem> CAP_DIGESTION_SYSTEM;

    @CapabilityInject(IDigestibleObject.class)
    public static Capability<IDigestibleObject> CAP_DIGESTIBLE_OBJECT;

    public static IForgeRegistry<IDigestibleRecipe> DIGESTIBLE_RECIPES =
            GameRegistry.findRegistry(IDigestibleRecipe.class);
}
