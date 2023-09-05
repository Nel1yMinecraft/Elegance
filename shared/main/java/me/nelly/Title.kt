/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package me.nelly

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.TextValue
import org.lwjgl.opengl.Display.setTitle

@ModuleInfo(name = "Title", description = "Title", category = ModuleCategory.MISC)
class Title : Module() {
    private val mainTitle = TextValue("FirstTitle", LiquidBounce.CLIENT_NAME)
    private val midTitle = TextValue("MiddleTitle", LiquidBounce.CLIENT_VERSION.toString())
    private val showPlayTime = BoolValue("ShowPlayTime", true)
    var title: String = ""
    private var ticks = 0
    private var seconds = 0
    private var minutes = 0
    private var hours = 0
    private var timeText = ""

    override fun onDisable() {
        setTitle("${LiquidBounce.CLIENT_NAME} ${LiquidBounce.CLIENT_VERSION}")
        super.onDisable()
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        ticks++
        if (ticks == 20) {
            seconds++
            ticks = 0
        }
        if (seconds == 60) {
            minutes++
            seconds = 0
        }
        if (minutes == 60) {
            hours++
            minutes = 0
        }
        timeText = "$hours 时 $minutes 分 $seconds 秒 "
        setTitle(mainTitle.get() + " | " + midTitle + " | " + if (showPlayTime.get()) " $timeText" else "")
    }
}