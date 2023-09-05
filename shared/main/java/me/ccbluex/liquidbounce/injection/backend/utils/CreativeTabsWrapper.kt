package me.ccbluex.liquidbounce.injection.backend.utils

import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.util.WrappedCreativeTabs
import me.ccbluex.liquidbounce.api.util.WrappedMutableList
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.injection.backend.wrap
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

class CreativeTabsWrapper(val wrapped: WrappedCreativeTabs, name: String) : CreativeTabs(name) {
    override fun getTabIconItem(): ItemStack = ItemStack(wrapped.getTabIconItem().unwrap())
    override fun displayAllRelevantItems(items: NonNullList<ItemStack>) = wrapped.displayAllReleventItems(WrappedMutableList(items!!, IItemStack::unwrap, ItemStack::wrap))
    override fun getTranslatedTabLabel(): String = wrapped.getTranslatedTabLabel()
    override fun hasSearchBar(): Boolean = wrapped.hasSearchBar()
}