/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGui
import net.minecraft.client.gui.Gui

open class GuiImpl<T : Gui>(val wrapped: T) : IGui

inline fun IGui.unwrap(): Gui = (this as GuiImpl<*>).wrapped
inline fun Gui.wrap(): IGui = GuiImpl(this)