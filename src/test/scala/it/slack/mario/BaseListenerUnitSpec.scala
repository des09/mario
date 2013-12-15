package it.slack.mario

import org.mockito.Mockito.when
import org.pircbotx.Channel
import org.pircbotx.Configuration
import org.pircbotx.hooks.managers.ListenerManager
import org.pircbotx.output.OutputChannel
import org.pircbotx.output.OutputIRC
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.scalatest.mock.MockitoSugar
import org.scalatest.BeforeAndAfter

class BaseListenerUnitSpec extends BaseUnitSpec with MockitoSugar with BeforeAndAfter {
    var bot: PircBotX = _
    var user: User = _
    var outputIRC: OutputIRC = _
    var channel: Channel = _
    var outputChannel: OutputChannel = _
    var listenerManager: ListenerManager[PircBotX] = _
    var configuration: Configuration[PircBotX] = _

    before {
        bot = mock[PircBotX]
        user = mock[User]
        outputIRC = mock[OutputIRC]
        channel = mock[Channel]
        outputChannel = mock[OutputChannel]
        configuration = mock[Configuration[PircBotX]]
        listenerManager = mock[ListenerManager[PircBotX]]

        when(bot.getConfiguration) thenReturn configuration
        when(configuration.getListenerManager) thenReturn listenerManager
        when(bot.sendIRC) thenReturn outputIRC
        when(channel.send) thenReturn outputChannel
    }
}
