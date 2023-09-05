package me.ccbluex.liquidbounce.utils

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.enums.WEnumHand
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import me.ccbluex.liquidbounce.injection.backend.Backend
import me.ccbluex.liquidbounce.injection.backend.WrapperImpl.classProvider

inline fun createUseItemPacket(itemStack: IItemStack?, hand: WEnumHand): IPacket {
    @Suppress("ConstantConditionIf")
    return if (Backend.MINECRAFT_VERSION_MINOR == 8) {
        classProvider.createCPacketPlayerBlockPlacement(itemStack)
    } else {
        classProvider.createCPacketTryUseItem(hand)
    }
}
fun createUseItemPacket(hand: WEnumHand): IPacket {
    return classProvider.createCPacketTryUseItem(hand)
}
inline fun createOpenInventoryPacket(): IPacket {
    @Suppress("ConstantConditionIf")
    return if (Backend.MINECRAFT_VERSION_MINOR == 8) {
        classProvider.createCPacketClientStatus(ICPacketClientStatus.WEnumState.OPEN_INVENTORY_ACHIEVEMENT)
    } else {
        classProvider.createCPacketEntityAction(LiquidBounce.wrapper.minecraft.thePlayer!!, ICPacketEntityAction.WAction.OPEN_INVENTORY)
    }
}
