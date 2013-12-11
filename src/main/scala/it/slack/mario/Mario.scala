package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.Configuration
import org.pircbotx.hooks.Event
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.pircbotx.UtilSSLSocketFactory
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class MainListener(nickname: String) extends ListenerAdapter[PircBotX] {
    val byName = (nickname + ": .*") r

    override def onMessage(event: MessageEvent[PircBotX]) = {
        val message = event.getMessage

        message match {
            case byName() =>
                event.getChannel.send.message(event.getUser, "I'm very busy saving the princess")

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

class Settings {
    private val config = ConfigFactory.load()

    // validate vs. reference.conf
    config.checkValid(ConfigFactory.defaultReference())

    // non-lazy fields, we want all exceptions at construct time
    val nickname = config.getString("nickname")
    val username = config.getString("username")
    val server_hostname = config.getString("server_hostname")
    val server_port = config.getInt("server_port")
    val server_uses_ssl = config.getBoolean("server_uses_ssl")
    val channel = config.getString("channel")

    override def toString: String = {
        "Settings(nickname: %s, username: %s, server_hostname: %s, server_port: %s, server_uses_ssl: %s, channel: %s)".
            format(nickname, username, server_hostname, server_port, server_uses_ssl, channel)
    }
}

object Mario {
    def main(args: Array[String]) = {
        val logger = LoggerFactory.getLogger(getClass)

        val settings = new Settings

        logger.info(settings.toString)

        var configurationBuilder = new Configuration.Builder()
            .setName(settings.nickname)
            .setLogin(settings.username)
            .setAutoNickChange(true)
            .setCapEnabled(true)
            .setServer(settings.server_hostname, settings.server_port)
            .addAutoJoinChannel(settings.channel)
            .addListener(new MainListener(settings.nickname))
            .addListener(new QuitListener)

        if (settings.server_uses_ssl) {
            configurationBuilder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
        }

        val configuration = configurationBuilder.buildConfiguration()

        val bot = new PircBotX(configuration)
        bot.startBot()
    }
}
