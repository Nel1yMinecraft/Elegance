package me.nelly

//LiquidBounce
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import me.ccbluex.liquidbounce.utils.MinecraftInstance
import me.ccbluex.liquidbounce.utils.MovementUtils
//Minecraft
import net.minecraft.client.Minecraft
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.util.text.TextComponentString
//GL
import org.lwjgl.opengl.GL11

open class NellyUtils {
    companion object {
        @JvmField
        val mc = Minecraft.getMinecraft()

        @JvmField
        val mc2 = LiquidBounce.wrapper.minecraft

        @JvmField
        val thePlayer = mc2.thePlayer

        @JvmField
        val player = mc.player

        // 玩家正在移动
        @JvmField
        val isMoving = MovementUtils.isMoving

        // 玩家正在地上
        @JvmField
        val onGround = mc.player.onGround

        // 玩家正在空中
        @JvmField
        val onAir = mc.player.isAirBorne

        // 时间的速度
        @JvmField
        var timespeed = mc2.timer.timerSpeed


        // 发送发包
        fun sendPacket(packet: Packet<INetHandlerPlayServer>) {
            ArrayList<Packet<INetHandlerPlayServer>>().add(packet)
            MinecraftInstance.mc.netHandler.addToSendQueue(packet as IPacket)
        }

        // 如果玩家受伤
        fun isPlayerHurt(): Boolean = mc.player.hurtTime > 0

        // 发送消息(客户端)
        fun sendMessage(s: String) {
            mc.player.sendMessage(TextComponentString(s))
        }

        // 发送消息(玩家)
        fun sendChatMessage(s: String) {
            mc.player.sendChatMessage(s)
        }
        // X Y 宽度 高度 圆角值 blur值 shadow值 红色 绿色 蓝色 透明度(最大255) 不需要的则填0(blur shadow radius)
        fun drawRect(x: Float, y: Float, width: Float, height: Float, cornerRadius: Float, blur: Float, shadow: Float, red: Float, green: Float, blue: Float, alpha: Float, outline: Boolean, outlineWidth: Float, outlineRed: Float, outlineGreen: Float, outlineBlue: Float, outlineAlpha: Float) {
            GL11.glPushMatrix()
            GL11.glTranslatef(x, y, 0f)

            GL11.glColor4f(0f, 0f, 0f, shadow)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex2f(blur, blur)
            GL11.glVertex2f(width + blur, blur)
            GL11.glVertex2f(width + blur, height + blur)
            GL11.glVertex2f(blur, height + blur)
            GL11.glEnd()

            GL11.glColor4f(red, green, blue, alpha)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex2f(0f, 0f)
            GL11.glVertex2f(width, 0f)
            GL11.glVertex2f(width, height)
            GL11.glVertex2f(0f, height)
            GL11.glEnd()

            if (outline && outlineWidth > 0f) {
                GL11.glLineWidth(outlineWidth)
                GL11.glColor4f(outlineRed, outlineGreen, outlineBlue, outlineAlpha)
                GL11.glBegin(GL11.GL_LINE_LOOP)
                GL11.glVertex2f(-outlineWidth / 2, -outlineWidth / 2)
                GL11.glVertex2f(width + outlineWidth / 2, -outlineWidth / 2)
                GL11.glVertex2f(width + outlineWidth / 2, height + outlineWidth / 2)
                GL11.glVertex2f(-outlineWidth / 2, height + outlineWidth / 2)
                GL11.glEnd()
            }

            val segments = 360
            val anglePerSegment = 360f / segments

            GL11.glColor4f(red, green, blue, alpha)
            GL11.glBegin(GL11.GL_TRIANGLE_FAN)
            GL11.glVertex2f(width / 2, height / 2)

            val topLeft = cornerRadius
            val topRight = width - cornerRadius
            val bottomLeft = height - cornerRadius
            val bottomRight = height - cornerRadius

            for (i in 0..segments) {
                val angle = i * anglePerSegment
                val xCoord: Float
                val yCoord: Float

                if (angle >= 0f && angle < 90f) {
                    val normalizedAngle = angle / 90f
                    xCoord =
                        topLeft + cornerRadius * Math.sin(Math.toRadians(normalizedAngle * 90f.toDouble())).toFloat()
                    yCoord =
                        bottomLeft + cornerRadius * Math.sin(Math.toRadians((1f - normalizedAngle) * 90f.toDouble()))
                            .toFloat()
                } else if (angle >= 90f && angle < 180f) {
                    val normalizedAngle = (angle - 90f) / 90f
                    xCoord =
                        topRight + cornerRadius * Math.cos(Math.toRadians(normalizedAngle * 90f.toDouble())).toFloat()
                    yCoord = bottomRight + cornerRadius * Math.sin(Math.toRadians(normalizedAngle * 90f.toDouble()))
                        .toFloat()
                } else if (angle >= 180f && angle < 270f) {
                    val normalizedAngle = (angle - 180f) / 90f
                    xCoord = topRight + cornerRadius * Math.cos(Math.toRadians((1f - normalizedAngle) * 90f.toDouble()))
                        .toFloat()
                    yCoord =
                        topLeft + cornerRadius * Math.cos(Math.toRadians(normalizedAngle * 90f.toDouble())).toFloat()
                } else {
                    val normalizedAngle = (angle - 270f) / 90f
                    xCoord = topLeft + cornerRadius * Math.sin(Math.toRadians((1f - normalizedAngle) * 90f.toDouble()))
                        .toFloat()
                    yCoord = topLeft + cornerRadius * Math.cos(Math.toRadians((1f - normalizedAngle) * 90f.toDouble()))
                        .toFloat()
                }

                GL11.glVertex2f(xCoord, yCoord)
            }

            GL11.glEnd()

            GL11.glPopMatrix()
        }

        // 别用我 我就是单纯看上面的函数都没有调用我看着难受
        private fun sb() {
            if (onAir && isPlayerHurt())
                timespeed = 1f
            sendMessage("1")
            sb()
        }
    }
}
