package me.ccbluex.liquidbounce.utils

import com.google.gson.JsonObject
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType

class ClientUtils2 {

    companion object {
        fun displayChatMessage(commandChatMode: Boolean, message: String) {
            if (MinecraftInstance.mc.thePlayer == null) {
                ClientUtils.getLogger().info("(MCChat) $message")
                return
            }

            val jsonObject = JsonObject()
            if (commandChatMode) {
                jsonObject.addProperty(
                    "text",
                    ("§b${LiquidBounce.CLIENT_NAME} §7» " + message)
                )

                MinecraftInstance.mc.thePlayer!!.addChatMessage(
                    LiquidBounce.wrapper.functions.jsonToComponent(
                        jsonObject.toString()
                    )
                )
            } else {
                jsonObject.addProperty("text", message)

                MinecraftInstance.mc.thePlayer!!.addChatMessage(
                    LiquidBounce.wrapper.functions.jsonToComponent(
                        jsonObject.toString()
                    )
                )
            }
        }

        fun notificationsTransform(title: String, content: String, type: NotifyType) {
            LiquidBounce.hud.addNotification(
                Notification(
                    title, content, type
                )
            )
        }
    }
}