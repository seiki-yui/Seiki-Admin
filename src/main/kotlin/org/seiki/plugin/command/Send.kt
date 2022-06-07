package org.seiki.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.UserMessageEvent
import net.mamoe.mirai.message.data.sendTo
import org.seiki.plugin.SeikiAdministrator
import org.seiki.plugin.buildMessage
import org.seiki.plugin.getOrWait
import org.seiki.plugin.runCatching

object Send : CompositeCommand(
    SeikiAdministrator, "send",
    "发送消息"
) {
    @SubCommand
    @Description("发送到某群")
    suspend fun CommandSenderOnMessage<UserMessageEvent>.group(group: Group) {
        subject!!.sendMessage("请在30秒内发送的消息...")
        subject!!.runCatching {
            this@group.fromEvent.getOrWait()?.sendTo(group)
        }.onSuccess {
            subject!!.sendMessage("已发送至 ${group.name}(${group.id})")
        }.onFailure {
            it.buildMessage().sendTo(subject!!)
        }
    }

    @SubCommand
    @Description("发送到某人")
    suspend fun CommandSenderOnMessage<UserMessageEvent>.user(user: User) {
        subject!!.sendMessage("请在30秒内发送的消息...")
        subject!!.runCatching {
            this@user.fromEvent.getOrWait()?.sendTo(user)
        }.onSuccess {
            subject!!.sendMessage("已发送至 ${user.nameCardOrNick}(${user.id})")
        }.onFailure {
            it.buildMessage().sendTo(subject!!)
        }
    }
}