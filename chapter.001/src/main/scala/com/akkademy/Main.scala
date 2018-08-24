package com.akkademy

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

object Main extends App {
	val system = ActorSystem("akkademy")
	system.actorOf(Props[AkkademyDb], name="akkademy-db")
}


