package com.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.akkademy.messages.SetRequest
import org.scalatest.{FunSpecLike,Matchers}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class SClientTest extends FunSpecLike with Matchers {
  val client = new SClient("127.0.0.1", 2552)
	describe("test_akkademy_db_worker") {
	  it("should set a value") {
	    client.set("123", new Integer(123))
	    val f = client.get("123")
	    val r = Await.result(f, 10 seconds)
	    r should equal(123)
	  }
	}
}