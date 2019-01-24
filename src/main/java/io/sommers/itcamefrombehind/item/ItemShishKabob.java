package io.sommers.itcamefrombehind.item;

import com.teamacronymcoders.base.items.IHasItemMeshDefinition;
import com.teamacronymcoders.base.items.ItemBase;
import io.sommers.itcamefrombehind.api.Digestion;
import io.sommers.itcamefrombehind.api.digestibleobject.IDigestibleObject;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemShishKabob extends ItemBase implements IHasItemMeshDefinition {
    public ItemShishKabob() {
        super("shish_kabob");
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        IDigestibleObject digestibleObject = player.getCapability(Digestion.CAP_DIGESTIBLE_OBJECT, null);
        IDigestiveSystem digestiveSystem = player.getCapability(Digestion.CAP_DIGESTION_SYSTEM, null);

        if (digestibleObject != null && digestiveSystem != null) {
            if (digestibleObject.canBeDigested(digestiveSystem, player) &&
                    digestiveSystem.canDigest(digestibleObject, player)) {
                player.setActiveHand(hand);
                return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }
        }
        return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
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
        return Optional.ofNullable(itemStack.getSubCompound("DIGESTION"))
                .map(tag -> tag.getString("RECIPE_NAME"))
                .filter(name -> !name.isEmpty())
                .map(ResourceLocation::new)
                .orElseGet(() -> new ResourceLocation("minecraft", "stick"));

    }


    @Override
    public List<ResourceLocation> getAllVariants() {
        return Digestion.DIGESTIBLE_RECIPES.getKeys()
                .parallelStream()
                .collect(Collectors.toList());
    }
}
