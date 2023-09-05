/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.ui.client.clickgui.style;

import me.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import me.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import me.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import me.ccbluex.liquidbounce.utils.MinecraftInstance;

public abstract class Style extends MinecraftInstance {

    public abstract void drawPanel(final int mouseX, final int mouseY, final Panel panel);

    public abstract void drawDescription(final int mouseX, final int mouseY, final String text);

    public abstract void drawButtonElement(final int mouseX, final int mouseY, final ButtonElement buttonElement);

    public abstract void drawModuleElement(final int mouseX, final int mouseY, final ModuleElement moduleElement);

}
