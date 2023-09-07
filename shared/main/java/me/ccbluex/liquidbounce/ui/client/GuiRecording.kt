import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import me.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.opengl.*
import java.awt.image.BufferedImage
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import javax.swing.JOptionPane

class GuiRecording : WrappedGuiScreen() {
    val fps = 60
    lateinit var process: Process
    lateinit var framebuffer: Framebuffer

    override fun initGui() {
        representedScreen.buttonList.add(
            classProvider.createGuiButton(
                0,
                representedScreen.width / 2 - 100,
                representedScreen.height / 4 + 48,
                "开始录制"
            )
        )
        representedScreen.buttonList.add(
            classProvider.createGuiButton(
                1,
                representedScreen.width / 2 - 100,
                representedScreen.height / 4 + 48 + 25,
                "结束录制 并渲染"
            )
        )

    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        representedScreen.drawBackground(1)
        representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
            0 -> startRecording()
            1 -> stopRecording()
        }
    }

    private fun startRecording() {
        try {
            val screenWidth = mc.displayWidth
            val screenHeight = mc.displayHeight

            val outputFileName = "recording_${System.currentTimeMillis()}.mp4"
            val command = "ffmpeg -f rawvideo -pix_fmt rgba -s ${screenWidth}x${screenHeight} -r $fps -i - -vf vflip -c:v libx264 -preset ultrafast -qp 0 $outputFileName"
            print(command)
            // 启动FFmpeg进程开始录制
            process = Runtime.getRuntime().exec(command)

            // 弹出提示框
            JOptionPane.showMessageDialog(null, "开始录制!", "GuiRecording", JOptionPane.INFORMATION_MESSAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        try {
            // 终止FFmpeg进程
            process.destroy()
            process.waitFor()
            process.destroyForcibly()

            JOptionPane.showMessageDialog(null, "录制完成!", "GuiRecording", JOptionPane.INFORMATION_MESSAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
