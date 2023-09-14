/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package me.nelly.fdp.module

import me.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import me.ccbluex.liquidbounce.event.ClientShutdownEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "PotionSpoof", description = "Form FDPClient Fix BY nelly", category = ModuleCategory.MISC)
class PotionSpoof : Module() {

    private val speedValue = BoolValue("Speed", false)
    private val moveSlowDownValue = BoolValue("Slowness", false)
    private val hasteValue = BoolValue("Haste", false)
    private val digSlowDownValue = BoolValue("MiningFatigue", false)
    private val blindnessValue = BoolValue("Blindness", false)
    private val strengthValue = BoolValue("Strength", false)
    private val jumpBoostValue = BoolValue("JumpBoost", false)
    private val weaknessValue = BoolValue("Weakness", false)
    private val regenerationValue = BoolValue("Regeneration", false)
    private val witherValue = BoolValue("Wither", false)
    private val resistanceValue = BoolValue("Resistance", false)
    private val poisonValue = BoolValue("Poison", false)
    override fun onDisable() {
        if (mc2.player != null) {
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.DIG_SPEED).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.BLINDNESS).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.MOVE_SLOWDOWN).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.MOVE_SPEED).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.DIG_SPEED).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.JUMP).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.WEAKNESS).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.REGENERATION).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.WITHER).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.POISON).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.DIG_SLOWDOWN).id)
            mc.thePlayer!!.removePotionEffectClient(classProvider.getPotionEnum(PotionType.MOVE_SPEED).id)
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent?) {
        if(state) {
            if (speedValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.MOVE_SPEED).id, 1337, 1))
            }
            if (hasteValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.DIG_SPEED).id, 1337, 1))
            }
            if (moveSlowDownValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.MOVE_SLOWDOWN).id, 1337, 1))
            }
            if (blindnessValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.BLINDNESS).id, 1337, 1))
            }
            if (strengthValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.STRENGTH).id, 1337, 1))
            }
            if (jumpBoostValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.JUMP).id, 1337, 1))
            }
            if (weaknessValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.WEAKNESS).id, 1337, 1))
            }
            if (regenerationValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.REGENERATION).id, 1337, 1))
            }
            if (witherValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.WITHER).id, 1337, 1))
            }
            if (resistanceValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.REGENERATION).id, 1337, 1))
            }
            if (digSlowDownValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.DIG_SLOWDOWN).id, 1337, 1))
            }
            if (poisonValue.get()) {
                mc.thePlayer?.addPotionEffect(classProvider.createPotionEffect(classProvider.getPotionEnum(PotionType.POISON).id, 1337, 1))
            }
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onShutdown(event: ClientShutdownEvent?) {
        onDisable()
    }
}