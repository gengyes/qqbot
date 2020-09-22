@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)


import BotManager.BotsManager
import Listener.BaseListeners
import curd.logger
import kotlinx.coroutines.joinAll
import net.mamoe.mirai.event.registerEvents


suspend fun main() {
    BaseListeners.listeners.forEach {
        BotsManager.registerEvents(it)
    }
    val bot = BotsManager.loginBot()
    val logger = logger()
    joinAll(BotsManager.jobs)
}