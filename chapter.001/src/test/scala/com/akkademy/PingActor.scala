package com.akkademy

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.scalatest.{FunSpecLike,Matchers}

class PingActor extends FunSpecLike with Matchers {
	val system = ActorSystem()
	implicit val timeout = Timeout(5 seconds)
	val pongActor = system.actorOf(Props(classOf[PongActor]))
	
	describe("pong actor") {
		it("should respone with pong") {
			// 出错的时候打印的堆栈是混在一起的
			val future = pongActor ? "ping"
			// 接收到的结果是Future[AnyRef]用mapTo转换为String类型
			val result = Await.result(future.mapTo[String], 1 second)
			assert(result=="pong")
		}
		it("should fail on unknown message") {
			val future = pongActor ? "unkonwn"
			intercept[Exception] {
				Await.result(future.mapTo[String], 1 second)
			}
		}
	}
	
	describe("FutureExamples") {
		import scala.concurrent.ExecutionContext.Implicits.global
		it("should print to console") {
			// (pongActor?"ping").foreach({
			//	case s:String => println("replied with:" + s)
			// })
			
			// 成功的返回后，再次调用
			(pongActor?"ping").foreach({
				case s:String=>
					println("---1--->"+s)
					(pongActor?"ping").map(s=>println("---2--->"+s))
			})
			
			// val f:Future[String] = askpong("ping").flatMap(s=>askpong("ping"))
			
			// 正常失败的时候
			askpong("failed").failed.foreach{
				case e:Exception=> println("---x--->got exception")
			}
			
			// 失败的时候，需要一个默认值
			val f2 = askpong("cause_error").recover{
				case t:Exception => "default"
			}
			
			// 从失败中恢复
			askpong("cause_error").recoverWith({
				case t:Exception => askpong("ping")
			})

			// 处理future列表，如果有一个返回失败，则整个都会失败
			val future_lst1:List[Future[String]] = List("ping","ping","ping").map(x=>askpong(x))
		  val lst1:Future[List[String]] = Future.sequence(future_lst1)
		  lst1.map(f=>println(f))
		  
		  // 让失败的有默认值，则整个不会失败
		  val future_lst2:List[Future[String]] = List("ping","pong","failed").map(x=>askpong(x))
		  val lst2:Future[List[String]] = Future.sequence(future_lst2.map(f=>f.recover{
		  	case e:Exception => "exception"
		  }))
		  lst2.map(f=>println(f))

			Thread.sleep(1000)
		}
	}
	
	def askpong(message:String):Future[String] = (pongActor?message).mapTo[String]
	
}