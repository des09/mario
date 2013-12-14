package it.slack.mario

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.pircbotx.PircBotX
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.pircbotx.User
import org.pircbotx.output.OutputIRC
import org.pircbotx.hooks.managers.ListenerManager
import org.pircbotx.Configuration

class QuitListenerUnitSpec extends BaseUnitSpec with MockitoSugar {
    describe("QuitListener") {
        val bot = mock[PircBotX]
        val user = mock[User]
        val outputIRC = mock[OutputIRC]
        val configuration = mock[Configuration[PircBotX]]
        val listenerManager = mock[ListenerManager[PircBotX]]

        when(bot.getConfiguration) thenReturn configuration
        when(configuration.getListenerManager) thenReturn listenerManager
        when(bot.sendIRC) thenReturn outputIRC

        it("quits when asked to") {
            val message = new PrivateMessageEvent[PircBotX](bot, user, "quit")

            val quitListener = new QuitListener

            quitListener.onPrivateMessage(message)

            verify(outputIRC).quitServer
        }
    }
}
