package com.akkademy

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import com.typesafe.config._

object Main extends App {
  val host = "127.0.0.1"
  val port = 2253
  val conf = s"""
    |akka.actor.provider="akka.remote.RemoteActorRefProvider"
    |akka.remote.netty.tcp.hostname="$host"
    |akka.remote.netty.tcp.port="$port"
    """.stripMargin

  val config = ConfigFactory.parseString(conf)
  val system = ActorSystem("akkademy-db-worker", config)
  
  val master_host = "127.0.0.1"
  val master_port = 2252
  system.actorOf(Props(new SClient(master_host, master_port)), "akkademy-db-worker")
  system.awaitTermination()
}


