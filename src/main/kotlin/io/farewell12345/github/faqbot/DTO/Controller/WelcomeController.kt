package io.farewell12345.github.faqbot.DTO.Controller

import com.google.gson.Gson
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Welcome
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.logger
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.GroupImage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiInternalApi
import java.lang.Exception
import java.util.*

object WelcomeController {
    private fun upDateWelcomeTalk(group: Group, talk: Answer):Boolean{
        try {
            val gson = Gson()
            DB.database.update(Welcome) {
                set(Welcome.talk, gson.toJson(talk))
                where {
                    it.group eq group.id
                }
            }
            return true
        }catch (e: Exception){
            logger().info(e)
        }
        return false
    }
    fun searchWelcomeTalk(group: Group): String? {
        try{
            val query = DB.database
                .from(Welcome)
                .select()
                .where {
                    (Welcome.group eq group.id)
                }
            query.forEach {
                return it[Welcome.talk]
            }
        }catch (e: Exception){
            logger().info(e)
        }
        return null
    }
    fun appendWelcomeTalk(group: Group, talk: Answer):Boolean{
        if (searchWelcomeTalk(group) ==null){
            val gson = Gson()
            DB.database.insert(Welcome){
                set(Welcome.group,group.id)
                set(Welcome.talk,gson.toJson(talk))
            }
            return true
        }
        return false
    }
    @MiraiInternalApi
    fun changeWelcome(group:Group, messageChain: MessageChain):Boolean{
        val imgList = LinkedList<String>()
        val atList = LinkedList<Long>()
        var text = ""
        messageChain.forEach {
            when(it){
                is GroupImage ->{
                    imgList.add(it.imageId)
                }
                is At ->{
                    atList.add(it.target)
                }
                is PlainText ->{
                    text +=it.content
                }
            }
        }
        return upDateWelcomeTalk(group, Answer(imgList, atList, text))
    }

}