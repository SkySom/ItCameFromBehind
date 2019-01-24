package io.sommers.itcamefrombehind.json;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JsonLoader<T> {
    private final String folders;
    private final Class<T> tClass;
    protected final Gson gson;
    protected final Supplier<Logger> logger;

    public JsonLoader(String folders, Class<T> tClass, Supplier<Logger> logger) {
        this.folders = folders;
        this.tClass = tClass;
        this.logger = logger;
        this.gson = new Gson();
    }

    public JsonLoader(String folders, Class<T> tClass, Supplier<Logger> logger, JsonDeserializer<T> deserializer) {
        this.folders = folders;
        this.tClass = tClass;
        this.logger = logger;
        this.gson = new GsonBuilder().registerTypeAdapter(tClass, deserializer).create();
    }

    public List<Map.Entry<ResourceLocation, T>> load() {
        return Loader.instance().getActiveModList()
                .parallelStream()
                .map(this::loadFilesForMod)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toList());
    }

    private Map<ResourceLocation, T> loadFilesForMod(ModContainer modContainer) {
        JsonContext ctx = new JsonContext(modContainer.getModId());
        Map<ResourceLocation, T> loadedObjects = Maps.newHashMap();
        CraftingHelper.findFiles(modContainer, "assets/" + modContainer.getModId() + folders,
                this::handlePreload,
                (root, file) -> {
                    this.handleFile(root, file, ctx)
                            .ifPresent(pair -> loadedObjects.put(pair.getKey(), pair.getValue()));
                    return true;
                }, true, true);

        return loadedObjects;
    }

    protected boolean handlePreload(Path root) {
        return true;
    }

    protected Optional<Pair<ResourceLocation, T>> handleFile(Path root, Path file, JsonContext context) {
        String relative = root.relativize(file).toString();
        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
            return Optional.empty();

        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
        final ResourceLocation key = new ResourceLocation(context.getModId(), name);

        Optional<T> object = handleFile(key, file, context);
        object.ifPresent(value -> {
            if (value instanceof IForgeRegistryEntry) {
                ((IForgeRegistryEntry) value).setRegistryName(key);
            }
        });
        return object.map(value -> Pair.of(key, value));
    }

    protected Optional<T> handleFile(ResourceLocation key, Path file, JsonContext context) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
            return parseObject(bufferedReader, context);
        } catch (IOException | JsonParseException | NullPointerException e) {
            logger.get().warn("Failed to load File for key: " + key, e);
            return Optional.empty();
        }
    }

    protected Optional<T> parseObject(BufferedReader bufferedReader, JsonContext context) throws JsonParseException, IOException {
        return Optional.ofNullable(gson.fromJson(bufferedReader, tClass));
    }
}