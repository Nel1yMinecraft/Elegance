/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.client.network

import me.ccbluex.liquidbounce.api.minecraft.INetworkManager
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import java.util.*

interface IINetHandlerPlayClient {
    val networkManager: INetworkManager
    val playerInfoMap: Collection<INetworkPlayerInfo>

    fun getPlayerInfo(uuid: UUID): INetworkPlayerInfo?
    fun addToSendQueue(classProviderCPacketHeldItemChange: IPacket)

}