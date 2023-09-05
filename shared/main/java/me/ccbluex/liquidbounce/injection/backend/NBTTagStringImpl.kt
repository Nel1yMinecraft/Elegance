/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagString
import net.minecraft.nbt.NBTTagString

class NBTTagStringImpl<T : NBTTagString>(wrapped: T) : NBTBaseImpl<T>(wrapped), INBTTagString

inline fun INBTTagString.unwrap(): NBTTagString = (this as NBTTagStringImpl<*>).wrapped
inline fun NBTTagString.wrap(): INBTTagString = NBTTagStringImpl(this)