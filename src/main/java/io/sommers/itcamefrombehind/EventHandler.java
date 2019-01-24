package io.sommers.itcamefrombehind;

import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.api.recipes.IDigestibleRecipe;
import io.sommers.itcamefrombehind.api.recipes.IDigestingProgress;
import io.sommers.itcamefrombehind.digestivesystem.DigestionSystemProvider;
import io.sommers.itcamefrombehind.json.DigestibleRecipeJsonLoader;
import io.sommers.itcamefrombehind.json.JsonLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Map;

@EventBusSubscriber(modid = ItCameFromBehind.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void attachEntityDigestiveSystem(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        ResourceLocation name = new ResourceLocation(ItCameFromBehind.MOD_ID, "digestive_system");
        Entity entity = entityAttachCapabilitiesEvent.getObject();
        if (entity instanceof EntityPlayer) {
            entityAttachCapabilitiesEvent.addCapability(name, new DigestionSystemProvider());
        }
    }

    @SubscribeEvent
    public static void addDigestionRecipes(RegistryEvent.Register<IDigestibleRecipe> recipeRegister) {
        new DigestibleRecipeJsonLoader().load()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(recipeRegister.getRegistry()::register);
    }

    @SubscribeEvent
    public static void createRecipeRegistry(RegistryEvent.NewRegistry newRegistryEvent) {
        new RegistryBuilder<IDigestibleRecipe>()
                .setName(new ResourceLocation(ItCameFromBehind.MOD_ID, "digestible_recipes"))
                .setType(IDigestibleRecipe.class)
                .create();
    }

    @SubscribeEvent
    public static void handleDigestion(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.END && playerTickEvent.side == Side.SERVER) {
            EntityPlayer entityPlayer = playerTickEvent.player;
            IDigestiveSystem digestiveSystem = entityPlayer.getCapability(Digestion.CAP_DIGESTION_SYSTEM, null);

            if (digestiveSystem != null) {
                for (IDigestingProgress progress : digestiveSystem.getCurrentlyDigesting()) {
                    if (progress.digest(digestiveSystem.getBaseSpeedModifier())) {
                        for (ItemStack itemStack : progress.getOutputs()) {
                            entityPlayer.world.spawnEntity(new EntityItem(entityPlayer.world,
                                    entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, itemStack));
                        }
                    }
                }
            }
        }
    }
}
