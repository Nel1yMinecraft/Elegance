package me.ccbluex.liquidbounce.ui.cnfont;

import me.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import me.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class FontLoaders {
    public static FontDrawer C15;
    public static FontDrawer C18;
    public static FontDrawer C20;
    public static FontDrawer C25;
    public static FontDrawer C30;
    public static FontDrawer C35;
    public static FontDrawer C40;


    public static void initFonts() {

        ClientUtils.getLogger().info("Loading CNFonts...");

        C15 = getFont("weiruanyahei.ttf", 15, true);
        C18 = getFont("weiruanyahei.ttf", 18, true);
        C20 = getFont("weiruanyahei.ttf", 20, true);
        C25 = getFont("weiruanyahei.ttf", 25, true);
        C30 = getFont("weiruanyahei.ttf", 30, true);
        C35 = getFont("weiruanyahei.ttf", 35, true);
        C40 = getFont("weiruanyahei.ttf", 40, true);

    }

    public static FontDrawer getFont(String name, int size, boolean antiAliasing) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/" + name)).getInputStream()).deriveFont(Font.PLAIN, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontDrawer(font, antiAliasing);
    }
}
