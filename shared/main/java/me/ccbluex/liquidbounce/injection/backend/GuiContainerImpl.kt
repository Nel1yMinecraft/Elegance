/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer
import me.ccbluex.liquidbounce.api.minecraft.inventory.IContainer
import me.ccbluex.liquidbounce.api.minecraft.inventory.ISlot
import me.ccbluex.liquidbounce.injection.backend.utils.toClickType
import me.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer
import net.minecraft.client.gui.inventory.GuiContainer

open class GuiContainerImpl<T : GuiContainer>(wrapped: T) : GuiScreenImpl<T>(wrapped), IGuiContainer {
    override fun handleMouseClick(slot: ISlot, slotNumber: Int, clickedButton: Int, clickType: Int) = (wrapped as IMixinGuiContainer).publicHandleMouseClick(slot.unwrap(), slotNumber, clickedButton, clickType.toClickType())
    override val inventorySlots: IContainer?
        get() = wrapped.inventorySlots?.wrap()
}

inline fun IGuiContainer.unwrap(): GuiContainer = (this as GuiContainerImpl<*>).wrapped
inline fun GuiContainer.wrap(): IGuiContainer = GuiContainerImpl(this)