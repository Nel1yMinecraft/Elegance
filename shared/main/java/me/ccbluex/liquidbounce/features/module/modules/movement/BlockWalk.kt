/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.api.enums.BlockType
import me.ccbluex.liquidbounce.event.BlockBBEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "BlockWalk", description = "Allows you to walk on non-fullblock blocks.", category = ModuleCategory.MOVEMENT)
class BlockWalk : Module() {
    private val cobwebValue = BoolValue("Cobweb", true)
    private val snowValue = BoolValue("Snow", true)

    @EventTarget
    fun onBlockBB(event: BlockBBEvent) {
        if (cobwebValue.get() && event.block == classProvider.getBlockEnum(BlockType.WEB) || snowValue.get() && event.block == classProvider.getBlockEnum(BlockType.SNOW_LAYER))
            event.boundingBox = classProvider.createAxisAlignedBB(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(),
                    event.x + 1.0, event.y + 1.0, event.z + 1.0)
    }
}
