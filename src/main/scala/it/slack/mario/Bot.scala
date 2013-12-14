package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.Configuration
import org.pircbotx.UtilSSLSocketFactory

class Bot(settings: Settings, quitListener: QuitListener, directMessageListener: DirectMessageListener, synonymListener: Option[SynonymListener]) {
    def run() = {
        val configurationBuilder = new Configuration.Builder()
            .setName(settings.nickname)
            .setLogin(settings.username)
            .setAutoNickChange(true)
            .setCapEnabled(true)
            .setServer(settings.server_hostname, settings.server_port)
            .addAutoJoinChannel(settings.channel)
            .addListener(quitListener)
            .addListener(directMessageListener)

        synonymListener.foreach(configurationBuilder.addListener)

        if (settings.server_uses_ssl) {
            configurationBuilder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
        }

        val configuration = configurationBuilder.buildConfiguration()

        val bot = new PircBotX(configuration)
        bot.startBot()
    }
}
