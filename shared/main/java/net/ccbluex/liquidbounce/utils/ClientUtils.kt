package net.ccbluex.liquidbounce.utils

import com.google.gson.JsonObject
import net.ccbluex.liquidbounce.LiquidBounce

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

                MinecraftInstance.mc.thePlayer!!.addChatMessage(LiquidBounce.wrapper.functions.jsonToComponent(jsonObject.toString()))
            } else {
                jsonObject.addProperty("text", message)

                MinecraftInstance.mc.thePlayer!!.addChatMessage(LiquidBounce.wrapper.functions.jsonToComponent(jsonObject.toString()))
            }
        }
    }
}