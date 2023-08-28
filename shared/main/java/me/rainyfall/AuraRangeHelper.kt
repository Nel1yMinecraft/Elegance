package me.rainyfall

import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "AuraRangeHelper", description = "AutoRange", category = ModuleCategory.COMBAT)
class AuraRangeHelper : Module() {
    private var fovvalue = BoolValue("EnableAirFov", true)
    private var airfov = FloatValue("AirFov", 120F, 100F, 180F).displayable { fovvalue.get() }
    private var fovvalue2 = BoolValue("EnableGroundFov", true)
    private var groundfov = FloatValue("GroundFov", 180F, 100F, 180F).displayable { fovvalue2.get() }
    private var enablegroundrange = BoolValue("EnableGroundRange", true)
    private var enablegroundblockrange = BoolValue("EnableGroundBlockRange", true)
    private var enablegroundhurttime = BoolValue("EnableHurtTime", true)
    private var enableairrange = BoolValue("EnableAirRange", true)
    private var enableairblockrange = BoolValue("EnableAirBlockRange", true)
    private var enableairhurttime = BoolValue("EnableAirHurtTime", true)
    private var groundrange = FloatValue("GroundRange", 3.10F, 1.00F, 4.00F).displayable { enablegroundrange.get() }
    private var groundblockrange =
        FloatValue("GroundBlockRange", 3.20F, 1.00F, 4.00F).displayable { enablegroundblockrange.get() }
    private var groundhurttime = IntegerValue("GroundHurttime", 10, 1, 10).displayable { enablegroundhurttime.get() }
    private var airrange = FloatValue("AirRange", 2.96F, 1.00F, 4.00F).displayable { enableairrange.get() }
    private var airblockrange =
        FloatValue("AirBlockRange", 3.20F, 1.00F, 4.00F).displayable { enableairblockrange.get() }
    private var airhurttime = IntegerValue("AirHurttime", 10, 1, 10).displayable { enableairhurttime.get() }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val aura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
        if (!aura.state) return
        if (mc.thePlayer!!.onGround) {
            if (fovvalue2.get()) aura.fovValue.set(groundfov.get())
            if (enablegroundrange.get()) aura.rangeValue.set(groundrange.get())
            if (enablegroundblockrange.get()) aura.blockRangeValue.set(groundblockrange.get())
            if (enablegroundhurttime.get()) aura.hurtTimeValue.set(groundhurttime.get())
        } else {
            if (fovvalue.get()) aura.fovValue.set(airfov.get())
            if (enableairrange.get()) aura.rangeValue.set(airrange.get())
            if (enableairblockrange.get()) aura.blockRangeValue.set(airblockrange.get())
            if (enableairhurttime.get()) aura.hurtTimeValue.set(airhurttime.get())
        }

    }
}
