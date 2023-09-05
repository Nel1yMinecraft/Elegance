package me.ccbluex.liquidbounce.injection.backend.utils

import me.ccbluex.liquidbounce.api.minecraft.client.IMinecraft
import me.ccbluex.liquidbounce.api.util.WrappedGuiSlot
import me.ccbluex.liquidbounce.injection.backend.GuiSlotImpl
import me.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.client.gui.GuiSlot

class GuiSlotWrapper(val wrapped: WrappedGuiSlot, mc: IMinecraft, width: Int, height: Int, topIn: Int, bottomIn: Int, slotHeightIn: Int) : GuiSlot(mc.unwrap(), width, height, topIn, bottomIn, slotHeightIn) {

    init {
        wrapped.represented = GuiSlotImpl(this)
    }

    override fun getSize(): Int = wrapped.getSize()

    override fun drawSlot(slotIndex: Int, xPos: Int, yPos: Int, heightIn: Int, mouseXIn: Int, mouseYIn: Int, partialTicks: Float) = wrapped.drawSlot(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn)

    override fun isSelected(slotIndex: Int): Boolean = wrapped.isSelected(slotIndex)

    override fun drawBackground() = wrapped.drawBackground()

    public override fun elementClicked(slotIndex: Int, isDoubleClick: Boolean, mouseX: Int, mouseY: Int) = wrapped.elementClicked(slotIndex, isDoubleClick, mouseX, mouseY)
}