/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.INetworkManager
import me.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient
import me.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import me.ccbluex.liquidbounce.api.util.WrappedCollection
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.network.play.INetHandlerPlayClient
import java.util.*

class INetHandlerPlayClientImpl(val wrapped: NetHandlerPlayClient) : IINetHandlerPlayClient {
    override val networkManager: INetworkManager
        get() = wrapped.networkManager.wrap()
    override val playerInfoMap: Collection<INetworkPlayerInfo>
        get() = WrappedCollection(wrapped.playerInfoMap, INetworkPlayerInfo::unwrap, NetworkPlayerInfo::wrap)

    override fun getPlayerInfo(uuid: UUID): INetworkPlayerInfo? = wrapped.getPlayerInfo(uuid)?.wrap()

    override fun addToSendQueue(packet: IPacket) = wrapped.sendPacket(packet.unwrap())

    override fun equals(other: Any?): Boolean {
        return other is INetHandlerPlayClientImpl && other.wrapped == this.wrapped
    }
}

inline fun IINetHandlerPlayClient.unwrap(): INetHandlerPlayClient = (this as INetHandlerPlayClientImpl).wrapped
inline fun NetHandlerPlayClient.wrap(): IINetHandlerPlayClient = INetHandlerPlayClientImpl(this)