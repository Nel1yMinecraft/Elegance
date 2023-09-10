// By Nel1y
package me.ccbluex.liquidbounce.ui.client.mainmenu;

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
                    "FDPGuiMainMenu"
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
                    "Exit"
                )
            )
        }
    override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(ClassicGuiMainMenu()))
            1 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(ColorByteGuiMainMenu()))
            2 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(me.ccbluex.liquidbounce.ui.client.GuiMainMenu()))
            3 -> while (true) mc.shutdown()
        }
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        representedScreen.drawBackground(1)
        representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }
}