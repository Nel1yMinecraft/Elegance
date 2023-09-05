/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.misc

import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "Teams", description = "Prevents Killaura from attacking team mates.", category = ModuleCategory.MISC)
class Teams : Module() {

    private val scoreboardValue = BoolValue("ScoreboardTeam", true)
    private val colorValue = BoolValue("Color", true)
    private val gommeSWValue = BoolValue("GommeSW", false)
    private val armorColorValue = BoolValue("ArmorColor", false)

    /**
     * Check if [entity] is in your own team using scoreboard, name color or team prefix
     */
    fun isInYourTeam(entity: IEntityLivingBase): Boolean {
        val thePlayer = mc.thePlayer ?: return false

        if (scoreboardValue.get() && thePlayer.team != null && entity.team != null && thePlayer.team!!.isSameTeam(entity.team!!)) {
            return true
        }

        val displayName = thePlayer.displayName

        if (armorColorValue.get()) {
            val entityPlayer = entity.asEntityPlayer()
            val myHead = thePlayer.inventory.armorInventory[3]
            val entityHead = entityPlayer.inventory.armorInventory[3]

            if (myHead != null && entityHead != null) {
                val myItemArmor = myHead.item!!.asItemArmor()
                val entityItemArmor = myHead.item!!.asItemArmor()

                if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)) {
                    return true
                }
            }
        }

        if (gommeSWValue.get() && displayName != null && entity.displayName != null) {
            val targetName = entity.displayName!!.formattedText.replace("§r", "")
            val clientName = displayName.formattedText.replace("§r", "")

            if (targetName.startsWith("T") && clientName.startsWith("T")) {
                if (targetName.length >= 2 && clientName.length >= 2) {
                    return targetName[1] == clientName[1] && targetName[1].isDigit()
                }
            }
        }

        if (colorValue.get() && displayName != null && entity.displayName != null) {
            val targetName = entity.displayName!!.formattedText.replace("§r", "")
            val clientName = displayName.formattedText.replace("§r", "")

            if (clientName.length >= 2) {
                val clientColorChar = clientName[1]
                return targetName.startsWith("§$clientColorChar")
            }
        }

        return false
    }

    override val tag: String
        get() = "Color"
}
