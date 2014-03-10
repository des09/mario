package it.slack.mario

import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import dispatch._, Defaults._

trait Thesaurus {
    def synonymsOf(word: String): Future[Array[String]]
}

class BighugelabsWords(private val apiKey: String) extends Thesaurus {
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
}

class SynonymListener(val thesaurus: Thesaurus) extends ListenerAdapter[PircBotX] {
    val synonymAsk = (".syn ([A-Za-z]+)") r

    override def onMessage(event: MessageEvent[PircBotX]) = {
        val message = event.getMessage

        message match {
            case synonymAsk(word) =>
                thesaurus.synonymsOf(word).map { synonyms =>
                    synonyms.grouped(25).foreach { group =>
                        event.getChannel.send.message(event.getUser, "synonyms of %s: %s".format(word, group.mkString(", ")))
                    }
                }

            case _ =>
        }
    }
}
