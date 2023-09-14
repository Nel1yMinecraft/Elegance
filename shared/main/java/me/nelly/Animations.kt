/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package me.nelly


import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.ListValue


@ModuleInfo(name = "Animations", description = "Powered by RyF and ColorByte", category = ModuleCategory.RENDER)
class Animations : Module() {
    companion object {
        @JvmStatic
        private val modeValue = ListValue(
            "Mode",
            arrayOf(
                "1.7", "Old", "Push", "WindMill", "Flux", "SigmaOld", "Zoom", "Jello", "Rotate360", "Jello2", "SigmaNew"
            ), "WindMill"
        )
        @JvmStatic
        private val onlySword = BoolValue("EverythingBlock", false)
        @JvmStatic
        private val tabShowPlayerSkinValue = BoolValue("TabShowPlayerSkin", true)
        @JvmStatic
        val rightClickBlocking = BoolValue("RightClickBlocking", true)
        @JvmStatic
        val blockValue = BoolValue("Blocking", true)
        @JvmStatic
        val breakDownAnimation = BoolValue("ItemBreakDownAnimation", true)
        @JvmStatic
        val SpeedSwing = FloatValue("Swing-Speed", 1F, 0F, 12F)
        @JvmStatic
        val rotate360speed =
            FloatValue("Rotate-Speed", 1f, 0f, 10f).displayable { modeValue.get().toLowerCase().equals("rotate360") }
        @JvmStatic
        private val RotateItemsValue = BoolValue("Rotate-Items", false)
        @JvmStatic
        val RotateItemsWhenEatingOrDrinkingValue =
            BoolValue("Eating-Rotate", false).displayable { RotateItemsValue.get() }
        @JvmStatic
        val RotateItemWhenBlockingValue = BoolValue("Blocking-Rotate", false).displayable { RotateItemsValue.get() }
        @JvmStatic
        val transformFirstPersonRotate =
            ListValue(
                "RotateMode",
                arrayOf("RotateY", "RotateXY", "Custom", "None"),
                "RotateY"
            ).displayable { RotateItemsValue.get() }
        @JvmStatic
        val SpeedRotate = FloatValue("Rotate-Speed", 1f, 0f, 10f).displayable { RotateItemsValue.get() }
        @JvmStatic
        val RotateX = FloatValue(
            "RotateXAxis",
            0f,
            -180f,
            180f
        ).displayable { RotateItemsValue.get() && transformFirstPersonRotate.get().equals("custom", ignoreCase = true) }
        @JvmStatic
        val RotateY = FloatValue(
            "RotateYAxis",
            0f,
            -180f,
            180f
        ).displayable { RotateItemsValue.get() && transformFirstPersonRotate.get().equals("custom", ignoreCase = true) }
        @JvmStatic
        val RotateZ = FloatValue(
            "RotateZAxis",
            0f,
            -180f,
            180f
        ).displayable { RotateItemsValue.get() && transformFirstPersonRotate.get().equals("custom", ignoreCase = true) }

        @JvmField
        val guiAnimations: ListValue =
            ListValue("GuiAnimation", arrayOf("None", "Zoom", "VSlide", "HSlide", "HVSlide"), "Zoom")

        @JvmField
        val tabAnimations = ListValue("Tab-Animation", arrayOf("None", "Zoom", "Slide"), "Zoom")

        @JvmField
        val HeldItemPosX = FloatValue("Held-itemPosX", 0f, -1f, 1f)

        @JvmField
        val HeldItemPosY = FloatValue("Held-itemPosY", 0f, -1f, 1f)

        @JvmField
        val HeldItemPosZ = FloatValue("Held-itemPosZ", 0f, -1f, 1f)

        @JvmField
        val heldBlockPos = BoolValue("HeldBlockingPos", true)
        @JvmStatic
        private val blockItemPosX = FloatValue("Blocking-itemPosX", 0f, -1f, 1f).displayable { !heldBlockPos.get() }
        @JvmStatic
        private val blockItemPosY = FloatValue("Blocking-itemPosY", 0f, -1f, 1f).displayable { !heldBlockPos.get() }
        @JvmStatic
        private val blockItemPosZ = FloatValue("Blocking-itemPosZ", 0f, -1f, 1f).displayable { !heldBlockPos.get() }

        @JvmStatic
        fun getModeValue(): ListValue {
            return modeValue
        }
        @JvmStatic
        fun getOnlySword(): Boolean {
            return !onlySword.get()
        }
        @JvmStatic
        fun getRotateItems(): Boolean {
            return RotateItemsValue.get()
        }

        @JvmStatic
        fun blockItemPosX(): Float {
            return if (heldBlockPos.get()) HeldItemPosX.get() else blockItemPosX.get()
        }

        @JvmStatic
        fun blockItemPosY(): Float {
            return if (heldBlockPos.get()) HeldItemPosY.get() else blockItemPosY.get()
        }

        @JvmStatic
        fun blockItemPosZ(): Float {
            return if (heldBlockPos.get()) HeldItemPosZ.get() else blockItemPosZ.get()
        }

        @JvmStatic
        var flagRenderTabOverlay = false
            get() = field && tabShowPlayerSkinValue.get()

        @JvmField
        var Scale = FloatValue("Scale", 1f, 0f, 2f)
    }
}
