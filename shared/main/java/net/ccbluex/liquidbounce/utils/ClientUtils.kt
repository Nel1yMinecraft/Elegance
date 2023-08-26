package net.ccbluex.liquidbounce.utils

import com.google.gson.JsonObject
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.moduleManager
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType

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

        fun notificationsTransform(title: String, content: String, type: NotifyType, onlyNotifications: Boolean) {
            if (!onlyNotifications) return
            when (moduleManager.toggleMessageMode) {
                2 -> LiquidBounce.hud.addNotification(
                    Notification(
                        title, content, type
                    )
                )
            }

        }

        fun notificationsTransform(title: String, content: String, type: NotifyType, time: Int) {
            when (moduleManager.toggleMessageMode) {
                1 -> ClientUtils.displayChatMessage("§b${LiquidBounce.CLIENT_NAME} §7» §b$title §7» §6$content")
                2 -> LiquidBounce.hud.addNotification(
                    Notification(
                        title, content, type, time
                    )
                )
            }
        }
        fun notificationsTransform(title: String, content: String, type: NotifyType) {
            when (moduleManager.toggleMessageMode) {
                1 -> ClientUtils.displayChatMessage("§b${LiquidBounce.CLIENT_NAME} §7» §b$title §7» §6$content")
                2 -> LiquidBounce.hud.addNotification(
                    Notification(
                        title, content, type
                    )
                )

            }
        }
    }
}