package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent

class DirectMessageListener(nickname: String) extends ListenerAdapter[PircBotX] {
    val byName = (nickname + ": .*") r
    val response = "I'm very busy saving the princess"

    override def onMessage(event: MessageEvent[PircBotX]) = {
        val message = event.getMessage

        message match {
            case byName() =>
                event.getChannel.send.message(event.getUser, response)

            case _ =>
        }
    }
}
