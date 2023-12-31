/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator

import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField
import me.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import me.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import org.lwjgl.input.Keyboard

class GuiTheAltening(private val prevGui: GuiAltManager) : WrappedGuiScreen() {

    // Data Storage
    companion object {
        var apiKey: String = ""
    }

    // Buttons
    private lateinit var loginButton: IGuiButton
    private lateinit var generateButton: IGuiButton

    // User Input Fields
    private lateinit var apiKeyField: IGuiTextField
    private lateinit var tokenField: IGuiTextField

    // Status
    private var status = ""

    /**
     * Initialize The Altening Generator GUI
     */
    override fun initGui() {
        // Enable keyboard repeat events
        Keyboard.enableRepeatEvents(true)

        // Login button
        loginButton = classProvider.createGuiButton(2, representedScreen.width / 2 - 100, 75, "Login")
        representedScreen.buttonList.add(loginButton)

        // Generate button
        generateButton = classProvider.createGuiButton(1, representedScreen.width / 2 - 100, 140, "Generate")
        representedScreen.buttonList.add(generateButton)

        // Buy & Back buttons
        representedScreen.buttonList.add(classProvider.createGuiButton(3, representedScreen.width / 2 - 100, representedScreen.height - 54, 98, 20, "Buy"))
        representedScreen.buttonList.add(classProvider.createGuiButton(0, representedScreen.width / 2 + 2, representedScreen.height - 54, 98, 20, "Back"))

        // Token text field
        tokenField = classProvider.createGuiTextField(666, Fonts.font40, representedScreen.width / 2 - 100, 50, 200, 20)
        tokenField.isFocused = true
        tokenField.maxStringLength = Integer.MAX_VALUE

        // Api key password field
        apiKeyField = classProvider.createGuiPasswordField(1337, Fonts.font40, representedScreen.width / 2 - 100, 115, 200, 20)
        apiKeyField.maxStringLength = 18
        apiKeyField.text = apiKey
        super.initGui()
    }

    /**
     * Draw screen
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        // Draw background to screen
        representedScreen.drawBackground(0)
        RenderUtils.drawRect(30.0f, 30.0f, representedScreen.width - 30.0f, representedScreen.height - 30.0f, Integer.MIN_VALUE)

        // Draw title and status
        Fonts.font35.drawCenteredString("TheAltening", representedScreen.width / 2.0f, 6.0f, 0xffffff)
        Fonts.font35.drawCenteredString(status, representedScreen.width / 2.0f, 18.0f, 0xffffff)

        // Draw fields
        apiKeyField.drawTextBox()
        tokenField.drawTextBox()

        // Draw text
        Fonts.font40.drawCenteredString("§7Token:", representedScreen.width / 2.0f - 84, 40.0f, 0xffffff)
        Fonts.font40.drawCenteredString("§7API-Key:", representedScreen.width / 2.0f - 78, 105.0f, 0xffffff)
        Fonts.font40.drawCenteredString("§7Use coupon code 'liquidbounce' for 20% off!", representedScreen.width / 2.0f, representedScreen.height - 65.0f, 0xffffff)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    /**
     * Handle button actions
     */
    override fun actionPerformed(button: IGuiButton) {
        if (!button.enabled) return

        when (button.id) {
            0 -> mc.displayGuiScreen(prevGui.representedScreen)
            1 -> {
                loginButton.enabled = false
                generateButton.enabled = false
                apiKey = apiKeyField.text

                status = "§cGenerating account..."
            }
        }
    }

    /**
     * Handle key typed
     */
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        // Check if user want to escape from screen
        if (Keyboard.KEY_ESCAPE == keyCode) {
            // Send back to prev screen
            mc.displayGuiScreen(prevGui.representedScreen)
            return
        }

        // Check if field is focused, then call key typed
        if (apiKeyField.isFocused) apiKeyField.textboxKeyTyped(typedChar, keyCode)
        if (tokenField.isFocused) tokenField.textboxKeyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    /**
     * Handle mouse clicked
     */
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        // Call mouse clicked to field
        apiKeyField.mouseClicked(mouseX, mouseY, mouseButton)
        tokenField.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    /**
     * Handle screen update
     */
    override fun updateScreen() {
        apiKeyField.updateCursorCounter()
        tokenField.updateCursorCounter()
        super.updateScreen()
    }

    /**
     * Handle gui closed
     */
    override fun onGuiClosed() {
        // Disable keyboard repeat events
        Keyboard.enableRepeatEvents(false)

        // Set API key
        apiKey = apiKeyField.text
        super.onGuiClosed()
    }
}