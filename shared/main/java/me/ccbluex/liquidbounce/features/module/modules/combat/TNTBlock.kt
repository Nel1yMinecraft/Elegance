/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MotionEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "TNTBlock", description = "Automatically blocks with your sword when TNT around you explodes.", category = ModuleCategory.COMBAT)
class TNTBlock : Module() {
    private val fuseValue = IntegerValue("Fuse", 10, 0, 80)
    private val rangeValue = FloatValue("Range", 9F, 1F, 20F)
    private val autoSwordValue = BoolValue("AutoSword", true)
    private var blocked = false

    @EventTarget
    fun onMotionUpdate(event: MotionEvent?) {
        val thePlayer = mc.thePlayer ?: return
        val theWorld = mc.theWorld ?: return

        for (entity in theWorld.loadedEntityList) {
            if (classProvider.isEntityTNTPrimed(entity) && thePlayer.getDistanceToEntity(entity) <= rangeValue.get()) {
                val tntPrimed = entity.asEntityTNTPrimed()

                if (tntPrimed.fuse <= fuseValue.get()) {
                    if (autoSwordValue.get()) {
                        var slot = -1
                        var bestDamage = 1f
                        for (i in 0..8) {
                            val itemStack = thePlayer.inventory.getStackInSlot(i)

                            if (itemStack != null && classProvider.isItemSword(itemStack.item)) {
                                val itemDamage = itemStack.item!!.asItemSword().damageVsEntity + 4f

                                if (itemDamage > bestDamage) {
                                    bestDamage = itemDamage
                                    slot = i
                                }
                            }
                        }

                        if (slot != -1 && slot != thePlayer.inventory.currentItem) {
                            thePlayer.inventory.currentItem = slot
                            mc.playerController.updateController()
                        }
                    }

                    if (thePlayer.heldItem != null && classProvider.isItemSword(thePlayer.heldItem!!.item)) {
                        mc.gameSettings.keyBindUseItem.pressed = true
                        blocked = true
                    }

                    return
                }
            }
        }

        if (blocked && !mc.gameSettings.isKeyDown(mc.gameSettings.keyBindUseItem)) {
            mc.gameSettings.keyBindUseItem.pressed = false
            blocked = false
        }
    }
}