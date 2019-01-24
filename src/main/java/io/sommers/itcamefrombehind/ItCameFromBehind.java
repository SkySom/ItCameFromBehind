package io.sommers.itcamefrombehind;

import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import io.sommers.itcamefrombehind.api.digestivesystem.IDigestiveSystem;
import io.sommers.itcamefrombehind.digestivesystem.DigestiveSystem;
import io.sommers.itcamefrombehind.item.ItemShishKabob;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ItCameFromBehind.MOD_ID,
        name = ItCameFromBehind.MOD_NAME,
        version = ItCameFromBehind.VERSION
)
public class ItCameFromBehind extends BaseModFoundation<ItCameFromBehind> {

    public static final String MOD_ID = "itcamefrombehind";
    public static final String MOD_NAME = "ItCameFromBehind";
    public static final String VERSION = "1.0.0";

    @Instance(MOD_ID)
    public static ItCameFromBehind INSTANCE;

    public ItCameFromBehind() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        CapabilityManager.INSTANCE.register(IDigestiveSystem.class, new NBTCapStorage<>(), DigestiveSystem::new);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void registerItems(ItemRegistry registry) {
        super.registerItems(registry);
        registry.register(new ItemShishKabob());
    }

    @Override
    public boolean hasConfig() {
        return false;
    }

    @Override
    public ItCameFromBehind getInstance() {
        return this;
    }
}
