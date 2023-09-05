/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.world

import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "NoSlowBreak", description = "Automatically adjusts breaking speed when using modules that influence it.", category = ModuleCategory.WORLD)
class NoSlowBreak : Module() {
    val airValue = BoolValue("Air", true)
    val waterValue = BoolValue("Water", false)
}
