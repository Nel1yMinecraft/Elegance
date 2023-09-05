/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.render

import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "NoSwing", description = "Disabled swing effect when hitting an entity/mining a block.", category = ModuleCategory.RENDER)
class NoSwing : Module() {
    val serverSideValue = BoolValue("ServerSide", true)
}