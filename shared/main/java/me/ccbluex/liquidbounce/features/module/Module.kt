/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.LiquidBounce.moduleManager
import me.ccbluex.liquidbounce.event.Listenable
import me.ccbluex.liquidbounce.injection.backend.Backend
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import me.ccbluex.liquidbounce.utils.ClientUtils2
import me.ccbluex.liquidbounce.utils.MinecraftInstance
import me.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import me.ccbluex.liquidbounce.value.Value
import org.lwjgl.input.Keyboard

open class Module : MinecraftInstance(), Listenable {
    var isSupported: Boolean

    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var name: String
    var chinesename: String
    var description: String
    var category: ModuleCategory
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    var array = true
        set(array) {
            field = array

            if (!LiquidBounce.isStarting)
                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    private val canEnable: Boolean

    var slideStep = 0F

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!

        name = moduleInfo.name
        chinesename = moduleInfo.chinesename
        description = moduleInfo.description
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        array = moduleInfo.array
        canEnable = moduleInfo.canEnable
        isSupported = Backend.REPRESENTED_BACKEND_VERSION in moduleInfo.supportedVersions
    }
    var higt = 0F

    // Current state of module
    var state = false
        set(value) {
            if (field == value)
                return

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            if (!LiquidBounce.isStarting) {
                when (moduleManager.toggleSoundMode) {
                    1 -> (if (value) mc.soundHandler.playSound(
                        "block.stone_pressureplate.click_on",
                        1F
                    ) else mc.soundHandler.playSound("block.stone_pressureplate.click_off", 1F))

                    2 -> (if (value) LiquidBounce.tipSoundManager.enableSound else LiquidBounce.tipSoundManager.disableSound).asyncPlay()
                    3 -> (if (value) LiquidBounce.tipSoundManager.sigmaenableSound else LiquidBounce.tipSoundManager.sigmadisableSound).asyncPlay()
                    4 -> (if (value) LiquidBounce.tipSoundManager.sinkaenableSound else LiquidBounce.tipSoundManager.sinkadisableSound).asyncPlay()
                    5 -> (if (value) LiquidBounce.tipSoundManager.fallenenableSound else LiquidBounce.tipSoundManager.fallendisableSound).asyncPlay()
                    6 -> (if (value) LiquidBounce.tipSoundManager.prideenableSound else LiquidBounce.tipSoundManager.pridedisableSound).asyncPlay()
                    7 -> (if (value) LiquidBounce.tipSoundManager.lbplusenableSound else LiquidBounce.tipSoundManager.lbplusdisableSound).asyncPlay()
                }
                when (moduleManager.toggleMessageMode) {
                    1 -> when (moduleManager.toggleChatMode) {
                        0 -> ClientUtils2.displayChatMessage(true,"${if (value) "§aEnabled" else "§cDisabled"} §r${name}.")
                        1 -> ClientUtils2.displayChatMessage(true,"§r${name} was ${if (value) "§aEnabled" else "§cDisabled"}.")
                    }
                    2 -> LiquidBounce.hud.addNotification(
                        Notification(
                            "Module", "${
                                if (value) "Enabled "
                                else "Disabled "
                            }$name", if (value) NotifyType.SUCCESS
                            else NotifyType.ERROR
                        )
                    )
                }
            }


            // Call on enabled or disabled
            if (value) {
                onEnable()

                if (canEnable)
                    field = true
            } else {
                onDisable()
                field = false
            }

            // Save module state
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }


    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F

    // Tag
    open val tag: String?
        get() = null

    val tagName: String
        get() = "$name${if (tag == null) "" else " §7$tag"}"

    val colorlessTagName: String
        get() = "$name${if (tag == null) "" else " " + stripColor(tag)}"

    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = values.find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>().filter { it.isSupported }

    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}