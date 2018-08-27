package com.akkademy

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object Main extends App {
  val host = "127.0.0.1"
  val port = 2252
  val conf = s"""
    |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
    |akka.remote.netty.tcp.hostname = "$host"
    |akka.remote.netty.tcp.port = "$port"
    """.stripMargin
  
  val config = ConfigFactory.parseString(conf)
	val system = ActorSystem("akkademy-db-master", config)
	val master = system.actorOf(Props[AkkademyDb], "akkademy-db-master")
	master ! messages.StartRequest()	
	
	system.awaitTermination()
}


