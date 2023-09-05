/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.ccbluex.liquidbounce.api.enums.EnumFacingType
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
class FastBow : Module() {

    private val packetsValue = IntegerValue("Packets", 20, 3, 20)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (!thePlayer.isUsingItem)
            return

        val currentItem = thePlayer.inventory.getCurrentItemInHand()

        if (currentItem != null && classProvider.isItemBow(currentItem.item)) {
            // TODO Find out what this is suppose to do
            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(WBlockPos.ORIGIN, 255, currentItem, 0F, 0F, 0F))

            val yaw = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.yaw
            else
                thePlayer.rotationYaw

            val pitch = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.pitch
            else
                thePlayer.rotationPitch

            for (i in 0 until packetsValue.get())
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerLook(yaw, pitch, true))

            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
            thePlayer.itemInUseCount = currentItem.maxItemUseDuration - 1
        }
    }
}