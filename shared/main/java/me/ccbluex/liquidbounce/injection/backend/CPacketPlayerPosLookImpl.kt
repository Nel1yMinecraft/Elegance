/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerPosLook
import net.minecraft.network.play.client.CPacketPlayer

class CPacketPlayerPosLookImpl<T : CPacketPlayer.PositionRotation>(wrapped: T) : PacketImpl<T>(wrapped), ICPacketPlayerPosLook

inline fun ICPacketPlayerPosLook.unwrap(): CPacketPlayer.PositionRotation = (this as CPacketPlayerPosLookImpl<*>).wrapped
inline fun CPacketPlayer.PositionRotation.wrap(): ICPacketPlayerPosLook = CPacketPlayerPosLookImpl(this)