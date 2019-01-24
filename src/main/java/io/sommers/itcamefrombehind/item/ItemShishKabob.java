package io.sommers.itcamefrombehind.item;

import com.teamacronymcoders.base.items.IHasItemMeshDefinition;
import com.teamacronymcoders.base.items.ItemBaseNoModel;
import com.teamacronymcoders.base.util.CapabilityUtils;
import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.digestibleobject.DigestibleItem;
import io.sommers.itcamefrombehind.digestibleobject.DigestibleObjectItemProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemShishKabob extends ItemBaseNoModel implements IHasItemMeshDefinition {
    public ItemShishKabob() {
        super("shish_kabob");
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        IDigestibleObject digestibleObject = itemStack.getCapability(Digestion.CAP_DIGESTIBLE_OBJECT, null);
        IDigestiveSystem digestiveSystem = player.getCapability(Digestion.CAP_DIGESTION_SYSTEM, null);

        if (digestibleObject != null && digestiveSystem != null) {
            if (digestibleObject.canBeDigested(digestiveSystem, player) &&
                    digestiveSystem.canDigest(digestibleObject, player)) {
                player.setActiveHand(hand);
                return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving) {
        IDigestibleObject digestibleObject = stack.getCapability(Digestion.CAP_DIGESTIBLE_OBJECT, null);
        IDigestiveSystem digestiveSystem = entityLiving.getCapability(Digestion.CAP_DIGESTION_SYSTEM, null);

        if (digestibleObject != null && digestiveSystem != null) {
            if (digestibleObject.canBeDigested(digestiveSystem, entityLiving) &&
                    digestiveSystem.canDigest(digestibleObject, entityLiving)) {
                digestiveSystem.beginDigestion(digestibleObject, entityLiving);
                return new ItemStack(Items.STICK);
            }
        }
        return stack;
    }

    @Override
    public ResourceLocation getResourceLocation(ItemStack itemStack) {
        return CapabilityUtils.getCapability(itemStack, Digestion.CAP_DIGESTIBLE_OBJECT)
                .map(IDigestibleObject::getRecipe)
                .map(IForgeRegistryEntry::getRegistryName)
                .orElseGet(() -> new ResourceLocation("minecraft", "stick"));

    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        Digestion.DIGESTIBLE_RECIPES
                .getKeys()
                .parallelStream()
                .map(this::createItemStack)
                .forEach(itemStacks::add);
        return itemStacks;
    }

    private ItemStack createItemStack(ResourceLocation entry) {
        ItemStack itemStack = new ItemStack(this, 1, 0);
        itemStack.getOrCreateSubCompound("DIGESTION").setString("RECIPE_NAME", entry.toString());
        return itemStack;
    }

    @Override
    public List<ResourceLocation> getAllVariants() {
        return Digestion.DIGESTIBLE_RECIPES.getKeys()
                .parallelStream()
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new DigestibleObjectItemProvider(new DigestibleItem(stack));
    }
}
