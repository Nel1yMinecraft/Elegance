/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.util

import me.ccbluex.liquidbounce.api.minecraft.event.IClickEvent

interface IChatStyle {
    var chatClickEvent: IClickEvent?
    var underlined: Boolean
    var color: WEnumChatFormatting?
}