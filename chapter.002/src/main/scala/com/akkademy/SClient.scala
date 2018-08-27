package com.akkademy

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import com.akkademy.messages._

class SClient(host:String, port:Int) extends Actor{
  private implicit val timeout = Timeout(5 seconds)
  var master:ActorSelection = _
  
  override def preStart():Unit = {
    master = context.actorSelection(s"akka.tcp://akkademy-db-master@$host:$port/user/akkademy-db-master")
    master ! ConnectRequest(host, port)
  }
  
  override def receive:Receive = {
    case message =>
      println("worker receive a message:" + message.toString())
  }
  
  def set(key:String, value:Object) = {
    master ? SetRequest(key,value)
  }
  
  def get(key:String) = {
    master ? GetRequest(key)
  }
}


