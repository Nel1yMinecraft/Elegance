/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package me.rainyfall

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MoveEvent
import me.ccbluex.liquidbounce.event.Render3DEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import me.ccbluex.liquidbounce.features.module.modules.movement.Fly
import me.ccbluex.liquidbounce.features.module.modules.movement.Speed
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.render.ColorManager
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.util.math.AxisAlignedBB
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


@ModuleInfo(name = "TargetStrafe", description = "pwq", category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {
    private val renderModeValue = ListValue("RenderMode", arrayOf("Circle", "Pentagon", "None"), "Pentagon")
    private val thirdPersonViewValue = BoolValue("ThirdPersonView", false)
    private val radiusValue = FloatValue("Radius", 0.1f, 0.5f, 5.0f)
    private val holdSpaceValue = BoolValue("HoldSpace", false)
    private val onlySpeedValue = BoolValue("OnlySpeed", false)
    private val onlyflyValue = BoolValue("keyFly", false)
    var direction = -1
    private val ka = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura

    /**
     *
     * @param event Render3DEvent
     */
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val target = ka.target ?: return
        if (renderModeValue.get() != "None" && canStrafe(target) && !target.isDead) {
            val counter = intArrayOf(0)
            if (renderModeValue.get().equals("Circle", true)) {
                GL11.glPushMatrix()
                GL11.glDisable(3553)
                GL11.glEnable(2848)
                GL11.glEnable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3042)
                GL11.glBlendFunc(770, 771)
                GL11.glHint(3154, 4354)
                GL11.glHint(3155, 4354)
                GL11.glHint(3153, 4354)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                GL11.glLineWidth(1.0f)
                GL11.glBegin(3)
                val x =
                    target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                val y =
                    target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
                val z =
                    target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                for (i in 0..359) {
                    val rainbow = Color(
                        Color.HSBtoRGB(
                            ((mc.thePlayer!!.ticksExisted / 70.0 + sin(i / 50.0 * 1.75)) % 1.0f).toFloat(),
                            0.7f,
                            1.0f
                        )
                    )
                    GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                    GL11.glVertex3d(
                        x + radiusValue.get() * cos(i * 6.283185307179586 / 45.0),
                        y,
                        z + radiusValue.get() * sin(i * 6.283185307179586 / 45.0)
                    )
                }
                GL11.glEnd()
                GL11.glDepthMask(true)
                GL11.glEnable(2929)
                GL11.glDisable(2848)
                GL11.glDisable(2881)
                GL11.glEnable(2832)
                GL11.glEnable(3553)
                GL11.glPopMatrix()
            } else {
                val rad = radiusValue.get()
                GL11.glPushMatrix()
                GL11.glDisable(3553)
                RenderUtils.startDrawing()
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                GL11.glLineWidth(1.0f)
                GL11.glBegin(3)
                val x =
                    target.lastTickPosX + (target.posX - target.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                val y =
                    target.lastTickPosY + (target.posY - target.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
                val z =
                    target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                for (i in 0..10) {
                    counter[0] = counter[0] + 1
                    val rainbow = Color(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                    //final Color rainbow = new Color(Color.HSBtoRGB((float) ((mc.thePlayer.ticksExisted / 70.0 + sin(i / 50.0 * 1.75)) % 1.0f), 0.7f, 1.0f));
                    GL11.glColor3f(rainbow.red / 255.0f, rainbow.green / 255.0f, rainbow.blue / 255.0f)
                    if (rad < 0.8 && rad > 0.0) GL11.glVertex3d(
                        x + rad * cos(i * 6.283185307179586 / 3.0),
                        y,
                        z + rad * sin(i * 6.283185307179586 / 3.0)
                    )
                    if (rad < 1.5 && rad > 0.7) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 4.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 4.0)
                        )
                    }
                    if (rad < 2.0 && rad > 1.4) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 5.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 5.0)
                        )
                    }
                    if (rad < 2.4 && rad > 1.9) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 6.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 6.0)
                        )
                    }
                    if (rad < 2.7 && rad > 2.3) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 7.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 7.0)
                        )
                    }
                    if (rad < 6.0 && rad > 2.6) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 8.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 8.0)
                        )
                    }
                    if (rad < 7.0 && rad > 5.9) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 9.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 9.0)
                        )
                    }
                    if (rad < 11.0) if (rad > 6.9) {
                        counter[0] = counter[0] + 1
                        RenderUtils.glColor(ColorManager.astolfoRainbow(counter[0] * 100, 5, 107))
                        GL11.glVertex3d(
                            x + rad * cos(i * 6.283185307179586 / 10.0),
                            y,
                            z + rad * sin(i * 6.283185307179586 / 10.0)
                        )
                    }
                }
                GL11.glEnd()
                GL11.glDepthMask(true)
                GL11.glEnable(2929)
                RenderUtils.stopDrawing()
                GL11.glEnable(3553)
                GL11.glPopMatrix()
            }
        }
    }

    /**
     *
     * @param event MoveEvent
     */
    @EventTarget
    fun onMove(event: MoveEvent) {
        val target = ka.target ?: return
        if (!canStrafe(target) || target.isDead) return
        var aroundVoid = false
        for (x in -1..0) for (z in -1..0) if (isVoid(x, z)) aroundVoid = true

        var yaw = RotationUtils.getRotationFromEyeHasPrev(target).yaw

        if (mc.thePlayer!!.isCollidedHorizontally || aroundVoid) direction *= -1

        var targetStrafe =
            (if (mc.thePlayer!!.moveStrafing != 0F) mc.thePlayer!!.moveStrafing * direction else direction.toFloat())
        if (!isBlockUnder()) targetStrafe = 0f

        val rotAssist = 45 / mc.thePlayer!!.getDistanceToEntity(target)
        val moveAssist = (45f / getStrafeDistance(target)).toDouble()

        var mathStrafe = 0f

        if (targetStrafe > 0) {
            if ((target.entityBoundingBox.minY > mc.thePlayer!!.entityBoundingBox.maxY || target.entityBoundingBox.maxY < mc.thePlayer!!.entityBoundingBox.minY) && mc.thePlayer!!.getDistanceToEntity(
                    target
                ) < radiusValue.get()
            ) yaw += -rotAssist
            mathStrafe += -moveAssist.toFloat()
        } else if (targetStrafe < 0) {
            if ((target.entityBoundingBox.minY > mc.thePlayer!!.entityBoundingBox.maxY || target.entityBoundingBox.maxY < mc.thePlayer!!.entityBoundingBox.minY) && mc.thePlayer!!.getDistanceToEntity(
                    target
                ) < radiusValue.get()
            ) yaw += rotAssist
            mathStrafe += moveAssist.toFloat()
        }

        val doSomeMath = doubleArrayOf(
            cos(Math.toRadians((yaw + 90f + mathStrafe).toDouble())),
            sin(Math.toRadians((yaw + 90f + mathStrafe).toDouble()))
        )
        val moveSpeed = sqrt(event.x.pow(2.0) + event.z.pow(2.0))

        val asLast = doubleArrayOf(
            moveSpeed * doSomeMath[0],
            moveSpeed * doSomeMath[1]
        )

        event.x = asLast[0]
        event.z = asLast[1]

        if (!thirdPersonViewValue.get()) return
        mc2.gameSettings.thirdPersonView = if (canStrafe(target)) 3 else 0
    }

    private fun canStrafe(target: IEntityLivingBase?): Boolean {
        return target != null && (!holdSpaceValue.get() || mc.thePlayer!!.movementInput.jump) && ((!onlySpeedValue.get() || LiquidBounce.moduleManager[Speed::class.java].state) || (onlyflyValue.get() && LiquidBounce.moduleManager[Fly::class.java].state))
    }

    private fun getStrafeDistance(target: IEntityLivingBase): Float {
        return (mc.thePlayer!!.getDistanceToEntity(target) - radiusValue.get()).coerceAtLeast(
            mc.thePlayer!!.getDistanceToEntity(
                target
            ) - (mc.thePlayer!!.getDistanceToEntity(target) - radiusValue.get() / (radiusValue.get() * 2))
        )
    }

    private fun isVoid(xPos: Int, zPos: Int): Boolean {
        if (mc.thePlayer!!.posY < 0.0) return true
        var off = 0
        while (off < mc.thePlayer!!.posY.toInt() + 2) {
            val bb = mc.thePlayer!!.entityBoundingBox.offset(xPos.toDouble(), -off.toDouble(), zPos.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
        }
        return true
    }

    fun isBlockUnder(): Boolean {
        if (mc2.player.posY < 0) return false
        var off = 0
        while (off < mc2.player.posY.toInt() + 2) {
            val bb: AxisAlignedBB =
                mc2.player.entityBoundingBox.offset(0.0, -off.toDouble(), 0.0)
            if (mc2.world.getCollisionBoxes(mc2.player, bb).isNotEmpty()
            ) {
                return true
            }
            off += 2
        }
        return false
    }

}