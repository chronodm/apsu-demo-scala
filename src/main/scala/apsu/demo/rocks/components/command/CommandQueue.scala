package apsu.demo.rocks.components.command

import scala.collection.immutable.Queue

/**
 * CommandQueue
 *
 * @author david
 */
case class CommandQueue(commands: Queue[Command]) {

  // TODO unit tests

  def +(additionalCmd: Command): CommandQueue = {
    CommandQueue(commands.enqueue(additionalCmd))
  }

  def +(additionalCommands: Queue[Command]): CommandQueue = {
    CommandQueue(commands.enqueue(additionalCommands))
  }
}
