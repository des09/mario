package it.slack.mario

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class Settings {
    private val config = ConfigFactory.load()
    private val logger = LoggerFactory.getLogger(getClass)


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

    logger.info("Settings(" +
        "nickname: " + nickname + ", " +
        "username: " + username + ", " +
        "server_hostname: " + server_hostname + ", " +
        "server_port: " + server_port + ", " +
        "server_uses_ssl: " + server_uses_ssl + ", " +
        "channel: " + channel + ", " +
        "words_bighugelabs_com_api_key: " + words_bighugelabs_com_api_key +
        ")")
}
