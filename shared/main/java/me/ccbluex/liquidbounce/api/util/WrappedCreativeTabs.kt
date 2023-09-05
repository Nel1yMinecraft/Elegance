/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.util

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.enums.ItemType
import me.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs
import me.ccbluex.liquidbounce.api.minecraft.item.IItem
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.injection.backend.WrapperImpl.classProvider

abstract class WrappedCreativeTabs(val name: String) {
    lateinit var representedType: ICreativeTabs

    init {
        LiquidBounce.wrapper.classProvider.wrapCreativeTab(name, this)
    }

    open fun displayAllReleventItems(items: MutableList<IItemStack>) {}
    open fun getTranslatedTabLabel(): String = "asdf"
    open fun getTabIconItem(): IItem = classProvider.getItemEnum(ItemType.WRITABLE_BOOK)
    open fun hasSearchBar(): Boolean = true
}