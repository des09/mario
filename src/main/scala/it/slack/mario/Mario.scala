package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.Configuration
import org.pircbotx.hooks.Event
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent

class MainListener extends ListenerAdapter[PircBotX] {
    val byName = "mario: .*".r

    override def onMessage(event: MessageEvent[PircBotX]) = {
        val message = event.getMessage

        message match {
            case byName() =>
                event.getChannel.send.message(event.getUser, "I'm very busy saving the princess");

            case _ =>
        }
    }
}

class QuitListener extends ListenerAdapter[PircBotX] {
    override def onPrivateMessage(event: PrivateMessageEvent[PircBotX]) = {
        if (event.getMessage == "quit") {
            event.getBot.sendIRC.quitServer
        }
    }
}

object Mario {
    def main(args: Array[String]) = {
        val configuration = new Configuration.Builder()
            .setName("mario")
            .setLogin("mario")
            .setAutoNickChange(true)
            .setCapEnabled(true)
            .setServer("localhost", 6697)
            .addAutoJoinChannel("#testmario")
            .addListener(new MainListener)
            .addListener(new QuitListener)
            .buildConfiguration();

        val bot = new PircBotX(configuration)
        bot.startBot()
    }
}
