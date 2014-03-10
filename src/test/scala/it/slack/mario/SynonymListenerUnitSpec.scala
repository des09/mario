package it.slack.mario

import org.mockito.Matchers.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.PircBotX
import org.pircbotx.User
import scala.concurrent.Promise
import scala.concurrent.Future
import scala.concurrent.future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.scalatest.concurrent.AsyncAssertions.Waiter

class SynonymListenerUnitSpec extends BaseListenerUnitSpec {
    describe("SynonymListener") {
        it("looks up synonyms and says them") {
            val w = new Waiter

            val message = new MessageEvent[PircBotX](bot, channel, user, ".syn home")

            val thesaurus = mock[Thesaurus]
            val response = future { Array("house", "base") }//Future.successful(Array("house", "base"))
            when(thesaurus.synonymsOf("home")) thenReturn response


            val listener = new SynonymListener(thesaurus)

            listener.onMessage(message)

            verify(thesaurus).synonymsOf("home")

            w {
                verify(outputChannel).message(user, "synonyms of home: house, base")
            }
        }
    }
}
