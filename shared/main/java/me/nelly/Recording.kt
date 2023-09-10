package me.nelly

import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.shader.Framebuffer
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane

@ModuleInfo("Recording","Nel1y", ModuleCategory.MISC)
class Recording : Module() {
    val fps = FloatValue("FPS", 60f)
    lateinit var process: Process
    lateinit var framebuffer: Framebuffer
    lateinit var saveDirectory: File

    override fun onDisable() {
        stopRecording()
    }

    override fun onEnable() {
        selectSaveDirectory()
        startRecording()
    }

    private fun selectSaveDirectory() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            saveDirectory = fileChooser.selectedFile
        }
    }

    private fun startRecording() {
        try {
            val screenWidth = mc.displayWidth
            val screenHeight = mc.displayHeight

            val outputFileName = "recording_${System.currentTimeMillis()}.mp4"

            val savePath = saveDirectory.absolutePath

            val command =
                "ffmpeg -f rawvideo -pix_fmt rgba -s ${screenWidth}x${screenHeight} -r ${fps.get()} -i - -vf vflip -c:v libx264 -preset ultrafast -qp 0 $savePath/$outputFileName"
            process = Runtime.getRuntime().exec(command)

            JOptionPane.showMessageDialog(null, "开始录制!", "GuiRecording", JOptionPane.INFORMATION_MESSAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        try {
            process.destroy()
            process.waitFor()
            process.destroyForcibly()

            JOptionPane.showMessageDialog(null, "录制完成!", "GuiRecording", JOptionPane.INFORMATION_MESSAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}