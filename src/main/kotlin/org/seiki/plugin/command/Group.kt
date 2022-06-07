package org.seiki.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.UserMessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.sendTo
import org.seiki.plugin.SeikiAdministrator
import org.seiki.plugin.buildMessage
import org.seiki.plugin.getOrWait
import org.seiki.plugin.runCatching

object Group : CompositeCommand(
    SeikiAdministrator, "group",
    description = "群管理"
) {
    @SubCommand
    @Description("改群名")
    suspend fun CommandSenderOnMessage<UserMessageEvent>.rename() {
        kotlin.runCatching {
            subject!!.sendMessage("请在30秒内发送待修改的群名...")
            this.fromEvent.getOrWait()?.content?.let { (subject as Group).name = it }
        }.onSuccess {
            subject!!.sendMessage("已修改为 \"${(subject as Group).name}\"")
        }.onFailure {
            it.buildMessage().sendTo(subject!!)
        }
    }

    @SubCommand
    @Description("禁言")
    suspend fun CommandSenderOnMessage<UserMessageEvent>.mute(member: Member, time: Int) {
        if ((subject!! as Group).botAsMember.permission == MemberPermission.MEMBER) {
            subject!!.sendMessage("我没有那个权限啊")
        } else {
            subject!!.runCatching {
                member.mute(time)
            }.onSuccess {
                subject!!.sendMessage("已禁言 ${member.nameCardOrNick}(${member.id})")
            }.onFailure {
                it.buildMessage().sendTo(subject!!)
            }
        }
    }
}