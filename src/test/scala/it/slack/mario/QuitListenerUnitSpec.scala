package it.slack.mario

import org.mockito.Mockito.verify
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.pircbotx.PircBotX

class QuitListenerUnitSpec extends BaseListenerUnitSpec {
    describe("QuitListener") {

        it("quits when asked to") {
            val message = new PrivateMessageEvent[PircBotX](bot, user, "quit")

            val quitListener = new QuitListener

            quitListener.onPrivateMessage(message)

            verify(outputIRC).quitServer
        }
    }
}
