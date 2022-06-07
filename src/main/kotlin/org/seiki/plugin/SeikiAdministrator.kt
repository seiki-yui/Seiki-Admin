package org.seiki.plugin

import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.seiki.plugin.command.Group
import org.seiki.plugin.command.Send

object SeikiAdministrator : KotlinPlugin(
    JvmPluginDescription(
        id = "org.seiki.admin",
        name = "Seiki Administrator",
        version = "1.0-SNAPSHOT"
    ) {
        author("xiao-zheng233")
    }
) {
    override fun onEnable() {
        logger.info { "Seiki-Administrator Loaded!" }
        val commandList: List<Command> = listOf(Group, Send)
        commandList.forEach {
            CommandManager.registerCommand(it)
        }
    }
}