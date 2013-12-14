package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.PrivateMessageEvent

class QuitListener extends ListenerAdapter[PircBotX] {
    override def onPrivateMessage(event: PrivateMessageEvent[PircBotX]) = {
        if (event.getMessage == "quit") {
            event.getBot.sendIRC.quitServer
        }
    }
}
