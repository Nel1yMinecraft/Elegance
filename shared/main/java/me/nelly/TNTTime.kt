package me.nelly

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Render2DEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos

@ModuleInfo("TNTTime","TNTTime",ModuleCategory.MISC)
class TNTTime: Module() {
    var tntTimer: Int = 0

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (tntTimer == 1) {
            ClientUtils.displayChatMessage("TNT爆炸时间: $tntTimer")
        }
        if (tntTimer == 2) {
            ClientUtils.displayChatMessage("TNT爆炸时间: $tntTimer")
        }
        if (tntTimer == 3) {
            ClientUtils.displayChatMessage("TNT爆炸时间: $tntTimer")
        }
        if (tntTimer == 4) {
            ClientUtils.displayChatMessage("TNT爆炸时间: $tntTimer")
        }
        if (tntTimer == 5) {
            ClientUtils.displayChatMessage("TNT爆炸时间: $tntTimer")
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val entity = mc2.player
        if (entity is net.minecraft.entity.player.EntityPlayer && entity.world.isRemote) {
            val posX = entity.posX.toInt()
            val posY = entity.posY.toInt()
            val posZ = entity.posZ.toInt()

            if (entity.world.getBlockState(BlockPos(posX, posY - 1, posZ)).block == Blocks.TNT) {
                tntTimer++
                if (tntTimer >= 60) {
                    tntTimer = 60
                }
            } else {
                tntTimer = 0
            }
        }
    }
}