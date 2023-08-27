/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, array = false)
class HUD : Module() {
    private val toggleMessageModeValue =
        ListValue("ToggleMessageMode", arrayOf("None", "Chat", "Notifications"), "Notifications")
    private val chatNotificationsModeValue = ListValue("ChatStyle", arrayOf("Enabled-Sth","Sth-Enabled"),"Enabled-Sth").displayable { toggleMessageModeValue.get().equals("Chat") }
    private val toggleSoundValue = ListValue(
        "ToggleSound",
        arrayOf("None", "Default", "Custom", "Sigma", "Sinka", "Fallen", "Pride", "LB+"),
        "Custom"
    )
    val blackHotbarValue = BoolValue("BlackHotbar", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("Blur", false)
    val fontChatValue = BoolValue("FontChat", false)
    val chatRect = BoolValue("ChatRect", true)
    val chatAnimValue = BoolValue("ChatAnimation", true)
    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return

        LiquidBounce.hud.render(false)
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }
    @EventTarget(ignoreCondition = true)
    fun onTick(event: TickEvent) {
        LiquidBounce.moduleManager.toggleMessageMode = toggleMessageModeValue.values.indexOf(toggleMessageModeValue.get())
        LiquidBounce.moduleManager.toggleSoundMode = toggleSoundValue.values.indexOf(toggleSoundValue.get())
        LiquidBounce.moduleManager.toggleChatMode = toggleSoundValue.values.indexOf(chatNotificationsModeValue.get())
    }
    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
                !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))) mc.entityRenderer.loadShader(classProvider.createResourceLocation("liquidbounce/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
                mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("liquidbounce/blur.json")) mc.entityRenderer.stopUseShader()
    }
    companion object {
        @JvmField
        val gidentspeed = IntegerValue("GidentSpeed", 100, 1, 1000)
        @JvmField
        val redValue = IntegerValue("Red", 255, 0, 255)
        @JvmField
        val greenValue = IntegerValue("Green", 255, 0, 255)
        @JvmField
        val blueValue = IntegerValue("Blue", 255, 0, 255)
        @JvmField
        val redValue2 = IntegerValue("Red2", 255, 0, 255)
        @JvmField
        val greenValue2 = IntegerValue("Green2", 255, 0, 255)
        @JvmField
        val blueValue2 = IntegerValue("Blue2", 255, 0, 255)
        @JvmField
        val rainbowSpeed = IntegerValue("Rainbow-Speed", 1500, 500, 7000)
        @JvmField
        val shadowValue = ListValue("ShadowMode", arrayOf("LiquidBounce", "Outline", "Default", "Custom"), "Outline")
        @JvmField
        val shadowstrenge = FloatValue("ShadowStrengh", 0.1f, 0.1f, 1f)
        val blurRadius = IntegerValue("BlurRadius", 10, 1, 50)
    }
    init {
        state = true
    }
}