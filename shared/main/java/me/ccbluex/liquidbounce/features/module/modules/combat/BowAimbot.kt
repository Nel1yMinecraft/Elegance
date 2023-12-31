/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Render3DEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.EntityUtils
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.ListValue
import java.awt.Color

@ModuleInfo(name = "BowAimbot", description = "Automatically aims at players when using a bow.", category = ModuleCategory.COMBAT)
class BowAimbot : Module() {

    private val silentValue = BoolValue("Silent", true)
    private val predictValue = BoolValue("Predict", true)
    private val throughWallsValue = BoolValue("ThroughWalls", false)
    private val predictSizeValue = FloatValue("PredictSize", 2F, 0.1F, 5F)
    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Direction"), "Direction")
    private val markValue = BoolValue("Mark", true)

    private var target: IEntity? = null

    override fun onDisable() {
        target = null
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        target = null

        if (classProvider.isItemBow(mc.thePlayer?.itemInUse?.item)) {
            val entity = getTarget(throughWallsValue.get(), priorityValue.get()) ?: return

            target = entity
            RotationUtils.faceBow(target, silentValue.get(), predictValue.get(), predictSizeValue.get())
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (target != null && !priorityValue.get().equals("Multi", ignoreCase = true) && markValue.get())
            RenderUtils.drawPlatform(target, Color(37, 126, 255, 70))
    }

    private fun getTarget(throughWalls: Boolean, priorityMode: String): IEntity? {
        val targets = mc.theWorld!!.loadedEntityList.filter {
            classProvider.isEntityLivingBase(it) && EntityUtils.isSelected(it, true) &&
                    (throughWalls || mc.thePlayer!!.canEntityBeSeen(it))
        }

        return when {
            priorityMode.equals("distance", true) -> targets.minBy { mc.thePlayer!!.getDistanceToEntity(it) }
            priorityMode.equals("direction", true) -> targets.minBy { RotationUtils.getRotationDifference(it) }
            priorityMode.equals("health", true) -> targets.minBy { it.asEntityLivingBase().health }
            else -> null
        }
    }

    fun hasTarget() = target != null && mc.thePlayer!!.canEntityBeSeen(target!!)
}