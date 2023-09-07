/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package me.ccbluex.liquidbounce.ui.client.mainmenu;

import GuiRecording
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import me.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import me.ccbluex.liquidbounce.ui.client.ColorByteGuiMainMenu


class GuiMainMenu : WrappedGuiScreen() {
        override fun initGui() {
            representedScreen.buttonList.add(
                classProvider.createGuiButton(
                    0,
                    representedScreen.width / 2 - 100,
                    representedScreen.height / 4 + 48,
                    "ClassicGuiMainMenu"
                )
            )
            representedScreen.buttonList.add(
                classProvider.createGuiButton(
                    1,
                    representedScreen.width / 2 - 100,
                    representedScreen.height / 4 + 48 + 25,
                    "ColorByteGuiMainMenu"
                )
            )
            representedScreen.buttonList.add(
                classProvider.createGuiButton(
                    2,
                    representedScreen.width / 2 - 100,
                    representedScreen.height / 4 + 48 + 50,
                    "LBGuiMainMenu"
                )
            )
            representedScreen.buttonList.add(
                classProvider.createGuiButton(
                    3,
                    representedScreen.width / 2 - 100,
                    representedScreen.height / 4 + 48 + 75,
                    "GuiRecording"
                )
            )
        }
    override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(ClassicGuiMainMenu()))
            1 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(ColorByteGuiMainMenu()))
            2 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(me.ccbluex.liquidbounce.ui.client.GuiMainMenu()))
            3 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiRecording()))
        }
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        representedScreen.drawBackground(1)
        representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }
}