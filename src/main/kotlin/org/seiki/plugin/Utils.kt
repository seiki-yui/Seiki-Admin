package org.seiki.plugin

import kotlinx.coroutines.TimeoutCancellationException
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.nextMessage

suspend fun MessageEvent.getOrWait(): MessageChain? =
    runCatching {
        this@getOrWait.nextMessage(30_000)
    }.getOrElse {
        when (it) {
            is TimeoutCancellationException -> {
                messageChainOf(PlainText("超时未发送!"), message.quote()).sendTo(subject)
                return null
            }
            else -> throw it
        }
    }

fun Throwable.buildMessage() = buildMessageChain {
    +PlainText("Warning! $this\n")
    if (this@buildMessage.cause != null) +PlainText("Caused by: ${this@buildMessage.cause}")
    +Image("{D3A4F304-847D-BB7B-1534-8ABFDC7575B4}.png")
}

suspend fun <T : Contact, R> T.runCatching(block: suspend T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        e.buildMessage().sendTo(this)
        Result.failure<R>(e).also { throw e }
    }
}

