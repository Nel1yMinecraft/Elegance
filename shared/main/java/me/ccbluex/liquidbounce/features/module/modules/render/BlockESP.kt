/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.render

import me.ccbluex.liquidbounce.api.enums.BlockType
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Render3DEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.block.BlockUtils.getBlock
import me.ccbluex.liquidbounce.utils.block.BlockUtils.getBlockName
import me.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.BlockValue
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.IntegerValue
import me.ccbluex.liquidbounce.value.ListValue
import java.awt.Color
import java.util.*

@ModuleInfo(name = "BlockESP", description = "Allows you to see a selected block through walls.", category = ModuleCategory.RENDER)
class BlockESP : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Box", "2D"), "Box")
    private val blockValue = BlockValue("Block", 168)
    private val radiusValue = IntegerValue("Radius", 40, 5, 120)
    private val blockLimitValue = IntegerValue("BlockLimit", 256, 0, 2056)
    private val colorRedValue = IntegerValue("R", 255, 0, 255)
    private val colorGreenValue = IntegerValue("G", 179, 0, 255)
    private val colorBlueValue = IntegerValue("B", 72, 0, 255)
    private val colorRainbow = BoolValue("Rainbow", false)
    private val searchTimer = MSTimer()
    private val posList: MutableList<WBlockPos> = ArrayList()
    private var thread: Thread? = null

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (searchTimer.hasTimePassed(1000L) && (thread == null || !thread!!.isAlive)) {
            val radius = radiusValue.get()
            val selectedBlock = functions.getBlockById(blockValue.get())

            if (selectedBlock == null || selectedBlock == classProvider.getBlockEnum(BlockType.AIR))
                return

            thread = Thread(Runnable {
                val blockList: MutableList<WBlockPos> = ArrayList()

                for (x in -radius until radius) {
                    for (y in radius downTo -radius + 1) {
                        for (z in -radius until radius) {
                            val thePlayer = mc.thePlayer!!

                            val xPos = thePlayer.posX.toInt() + x
                            val yPos = thePlayer.posY.toInt() + y
                            val zPos = thePlayer.posZ.toInt() + z

                            val blockPos = WBlockPos(xPos, yPos, zPos)
                            val block = getBlock(blockPos)

                            if (block == selectedBlock && blockList.size < blockLimitValue.get()) blockList.add(blockPos)
                        }
                    }
                }
                searchTimer.reset()

                synchronized(posList) {
                    posList.clear()
                    posList.addAll(blockList)
                }
            }, "BlockESP-BlockFinder")

            thread!!.start()
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        synchronized(posList) {
            val color = if (colorRainbow.get()) rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
            for (blockPos in posList) {
                when (modeValue.get().toLowerCase()) {
                    "box" -> RenderUtils.drawBlockBox(blockPos, color, true)
                    "2d" -> RenderUtils.draw2D(blockPos, color.rgb, Color.BLACK.rgb)
                }
            }
        }
    }

    override val tag: String
        get() = getBlockName(blockValue.get())
}