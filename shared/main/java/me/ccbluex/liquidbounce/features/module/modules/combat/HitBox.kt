/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "HitBox", description = "Makes hitboxes of targets bigger.", category = ModuleCategory.COMBAT)
class HitBox : Module() {

    val sizeValue = FloatValue("Size", 0.4F, 0F, 1F)

}