package me.ccbluex.liquidbounce.utils.extensions

import me.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo

fun INetworkPlayerInfo.getFullName(): String {
    if (displayName != null)
        return displayName!!.formattedText

    val team = playerTeam
    val name = gameProfile.name
    return team?.formatString(name) ?: name
}