/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.render;

import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import me.ccbluex.liquidbounce.event.EventTarget;
import me.ccbluex.liquidbounce.event.PacketEvent;
import me.ccbluex.liquidbounce.features.module.Module;
import me.ccbluex.liquidbounce.features.module.ModuleCategory;
import me.ccbluex.liquidbounce.features.module.ModuleInfo;
import me.ccbluex.liquidbounce.ui.client.clickgui.style.styles.AstolfoStyle;
import me.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidBounceStyle;
import me.ccbluex.liquidbounce.ui.client.clickgui.style.styles.NullStyle;
import me.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import me.ccbluex.liquidbounce.utils.render.ColorUtils;
import me.ccbluex.liquidbounce.value.BoolValue;
import me.ccbluex.liquidbounce.value.FloatValue;
import me.ccbluex.liquidbounce.value.IntegerValue;
import me.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce","Astolfo", "Null", "Slowly"}, "Astolfo") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static final ListValue animationValue = new ListValue("Animation", new String[] {"Azura", "Slide", "SlideBounce", "Zoom", "ZoomBounce", "None"}, "Slide");
    public static final ListValue backgroundValue = new ListValue("Background", new String[] {"Default", "None"}, "Default");

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        updateStyle();

        mc.displayGuiScreen(classProvider.wrapGuiScreen(LiquidBounce.clickGui));
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LiquidBounce.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LiquidBounce.clickGui.style = new NullStyle();
                break;
            case "slowly":
                LiquidBounce.clickGui.style = new SlowlyStyle();
                break;
            case "astolfo":
                LiquidBounce.clickGui.style = new AstolfoStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final IPacket packet = event.getPacket();

        if (classProvider.isSPacketCloseWindow(packet) && classProvider.isClickGui(mc.getCurrentScreen())) {
            event.cancelEvent();
        }
    }
}
