/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import me.ccbluex.liquidbounce.features.module.modules.render.XRay;
import me.ccbluex.liquidbounce.file.FileConfig;
import me.ccbluex.liquidbounce.file.FileManager;
import me.ccbluex.liquidbounce.utils.ClientUtils;

import java.io.*;

public class XRayConfig extends FileConfig {

    /**
     * Constructor of config
     *
     * @param file of config
     */
    public XRayConfig(final File file) {
        super(file);
    }

    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Override
    protected void loadConfig() throws IOException {
        final XRay xRay = (XRay) LiquidBounce.moduleManager.getModule(XRay.class);

        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }

        final JsonArray jsonArray = new JsonParser().parse(new BufferedReader(new FileReader(getFile()))).getAsJsonArray();

        xRay.getXrayBlocks().clear();

        for (final JsonElement jsonElement : jsonArray) {
            try {
                final IBlock block = LiquidBounce.wrapper.getFunctions().getBlockFromName(jsonElement.getAsString());

                if (xRay.getXrayBlocks().contains(block)) {
                    ClientUtils.getLogger().error("[FileManager] Skipped xray block '" + block.getRegistryName() + "' because the block is already added.");
                    continue;
                }

                xRay.getXrayBlocks().add(block);
            } catch (final Throwable throwable) {
                ClientUtils.getLogger().error("[FileManager] Failed to add block to xray.", throwable);
            }
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Override
    protected void saveConfig() throws IOException {
        final XRay xRay = (XRay) LiquidBounce.moduleManager.getModule(XRay.class);

        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }

        final JsonArray jsonArray = new JsonArray();

        for (final IBlock block : xRay.getXrayBlocks())
            jsonArray.add(FileManager.PRETTY_GSON.toJsonTree(LiquidBounce.wrapper.getFunctions().getIdFromBlock(block)));

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonArray));
        printWriter.close();
    }
}
