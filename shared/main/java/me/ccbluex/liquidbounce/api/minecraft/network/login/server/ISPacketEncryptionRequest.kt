package me.ccbluex.liquidbounce.api.minecraft.network.login.server

interface ISPacketEncryptionRequest {
    val verifyToken: ByteArray
}