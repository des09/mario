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
import dispatch._, Defaults._

class DirectMessageListener(nickname: String) extends ListenerAdapter[PircBotX] {
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

class SynonymListener(val apiKey: String) extends ListenerAdapter[PircBotX] {
    val synonymAsk = (".syn ([A-Za-z]+)") r

    def synonymsOf(word: String): Future[Array[String]] = {
        val query = "http://words.bighugelabs.com/api/2/" + apiKey + "/" + word + "/"
        println("query " + query)
        val svc = url(query)
        Http(svc OK as.String).map { response =>
            println("response " + response)
            response.split("\n").map { line =>
                line.split('|').last
            }
        }
    }

    override def onMessage(event: MessageEvent[PircBotX]) = {
        val message = event.getMessage

        message match {
            case synonymAsk(word) =>
                synonymsOf(word).map { synonyms =>
                    synonyms.grouped(25).foreach { group =>
                        event.getChannel.send.message(event.getUser, "synonyms of %s: %s".format(word, group.mkString(", ")))
                    }
                }

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
    val words_bighugelabs_com_api_key = config.getString("words_bighugelabs_com_api_key")
    val hasWordsBighugelabsComApiKey = words_bighugelabs_com_api_key.size > 0

    override def toString: String = {
        "Settings(nickname: %s, username: %s, server_hostname: %s, server_port: %s, server_uses_ssl: %s, channel: %s, words_bighugelabs_com_api_key: %s)".
            format(nickname, username, server_hostname, server_port, server_uses_ssl, channel, words_bighugelabs_com_api_key)
    }
}

class Mario {
    def run() = {
        val logger = LoggerFactory.getLogger(getClass)

        val settings = new Settings

        logger.info(settings.toString)

        val configurationBuilder = new Configuration.Builder()
            .setName(settings.nickname)
            .setLogin(settings.username)
            .setAutoNickChange(true)
            .setCapEnabled(true)
            .setServer(settings.server_hostname, settings.server_port)
            .addAutoJoinChannel(settings.channel)
            .addListener(new DirectMessageListener(settings.nickname))
            .addListener(new QuitListener)

        if (settings.hasWordsBighugelabsComApiKey) {
            configurationBuilder.addListener(new SynonymListener(settings.words_bighugelabs_com_api_key))
        }

        if (settings.server_uses_ssl) {
            configurationBuilder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
        }

        val configuration = configurationBuilder.buildConfiguration()

        val bot = new PircBotX(configuration)
        bot.startBot()
    }
}

object Mario extends Mario {
    def main(args: Array[String]) = run
}
