package com.akkademy

import akka.actor.Actor

class ScalaPongActor extends Actor {
	override def receive:Receive = {
		case "ping" => sender()!"pong"
		case _ => sender()!Status.Failure(new Exception("unknown message"))
	}
}


