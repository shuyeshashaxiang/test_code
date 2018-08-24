package com.akkademy

import akka.actor.Actor
import akka.actor.Status

class PongActor extends Actor {
	override def receive:Receive = {
		case "ping" => sender()!"pong"
		case _ => sender()!Status.Failure(new Exception("unknown message"))
	}
}


