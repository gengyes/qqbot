package io.farewell12345.github.faqbot.Plugin.SobelImgEdge

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.*
import java.net.URL
import javax.imageio.ImageIO


object ImageEge {
    @MiraiInternalApi
    fun sobelImageEge(event: MessageEvent): Boolean {
        return try {
            val img: Image = event.message.firstIsInstanceOrNull<Image>()!!
            GlobalScope.launch {
                try {
                    val url = img.queryUrl()
                    val image = URL(
                        url
                    ).openConnection().getInputStream()
                    val file = ImageIO.read(image)
                    val imgEge = Sobel().edgeExtract2(file)
                    val bs = ByteArrayOutputStream()
                    val imOut = ImageIO.createImageOutputStream(bs)
                    ImageIO.write(imgEge, "jpg", imOut)
                    val inputStream = ByteArrayInputStream(bs.toByteArray())
                    inputStream.sendAsImageTo(event.subject)
                }catch (e: Exception){

                }
            }
            true
        }catch (e: Exception){
            false
        }
    }
}