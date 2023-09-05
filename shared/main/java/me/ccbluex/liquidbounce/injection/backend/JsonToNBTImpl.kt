/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.nbt.IJsonToNBT
import me.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound
import net.minecraft.nbt.JsonToNBT

object JsonToNBTImpl : IJsonToNBT {
    override fun getTagFromJson(s: String): INBTTagCompound = JsonToNBT.getTagFromJson(s).wrap()
}