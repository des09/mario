package it.slack.mario

import org.slf4j.LoggerFactory

object Mario {
    private val logger = LoggerFactory.getLogger(getClass)

    def getSynonymListener(settings: Settings): Option[SynonymListener] = {
        if (settings.hasWordsBighugelabsComApiKey) {
            val thesaurus = new BighugelabsWords(settings.words_bighugelabs_com_api_key)
            Some(new SynonymListener(thesaurus))
        }
        else {
            None
        }
    }

    def main(args: Array[String]) = {
        val settings = new Settings

        val quitListener = new QuitListener
        val directMessageListener = new DirectMessageListener(settings.nickname)
        val synonymListener = getSynonymListener(settings)

        val mario = new Bot(settings, quitListener, directMessageListener, synonymListener)

        mario.run()
    }

}
