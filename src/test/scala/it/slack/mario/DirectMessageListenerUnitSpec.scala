package it.slack.mario

import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.PircBotX

class DirectMessageListenerUnitSpec extends BaseListenerUnitSpec {
    describe("DirectMessageListener") {
        it("responds when mentioned") {
            val message = new MessageEvent[PircBotX](bot, channel, user, "nickname: got a sec?")

            val listener = new DirectMessageListener("nickname")

            listener.onMessage(message)

            verify(outputChannel).message(user, "I'm very busy saving the princess")
        }

        it("ignores other messages") {
            val message = new MessageEvent[PircBotX](bot, channel, user, "sol: what's the news?")

            val listener = new DirectMessageListener("nickname")

            listener.onMessage(message)

            verify(outputChannel, never).message(user, "I'm very busy saving the princess")
        }
    }
}
