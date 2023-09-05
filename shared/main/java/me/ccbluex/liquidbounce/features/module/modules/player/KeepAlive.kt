/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.api.enums.ItemType
import me.ccbluex.liquidbounce.api.enums.WEnumHand
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MotionEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

import me.ccbluex.liquidbounce.utils.InventoryUtils
import me.ccbluex.liquidbounce.utils.createUseItemPacket
import me.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "KeepAlive", description = "Tries to prevent you from dying.", category = ModuleCategory.PLAYER)
class KeepAlive : Module() {

    val modeValue = ListValue("Mode", arrayOf("/heal", "Soup"), "/heal")

    private var runOnce = false

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.isDead || thePlayer.health <= 0) {
            if (runOnce) return

            when (modeValue.get().toLowerCase()) {
                "/heal" -> thePlayer.sendChatMessage("/heal")
                "soup" -> {
                    val soupInHotbar = InventoryUtils.findItem(36, 45, classProvider.getItemEnum(ItemType.MUSHROOM_STEW))

                    if (soupInHotbar != -1) {
                        mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(soupInHotbar - 36))
                        mc.netHandler.addToSendQueue(createUseItemPacket(thePlayer.inventory.getStackInSlot(soupInHotbar), WEnumHand.MAIN_HAND))
                        mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(thePlayer.inventory.currentItem))
                    }
                }
            }

            runOnce = true
        } else
            runOnce = false
    }
}