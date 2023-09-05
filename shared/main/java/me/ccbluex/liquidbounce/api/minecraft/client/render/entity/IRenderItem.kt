/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.client.render.entity

import me.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack

interface IRenderItem {
    var zLevel: Float

    fun renderItemAndEffectIntoGUI(stack: IItemStack, x: Int, y: Int)
    fun renderItemIntoGUI(stack: IItemStack, x: Int, y: Int)
    fun renderItemOverlays(fontRenderer: IFontRenderer, stack: IItemStack, x: Int, y: Int)
}