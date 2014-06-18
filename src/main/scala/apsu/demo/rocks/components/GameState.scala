package apsu.demo.rocks.components

import apsu.demo.rocks.util.{EnumLike, EnumRegistry}

/**
 * GameState
 *
 * @author david
 */
object GameState {
  val init = GameState("init")
  val title = GameState("title")
  val setup = GameState("setup")
  val playing = GameState("playing")
  val gameOver = GameState("gameOver")
}

case class GameState private(name: String) extends EnumLike[GameState] {
  GameStateRegistry.register(this)
}

private object GameStateRegistry extends EnumRegistry[GameState]
