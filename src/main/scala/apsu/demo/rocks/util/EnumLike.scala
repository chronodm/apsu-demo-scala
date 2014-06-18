package apsu.demo.rocks.util

/**
 * EnumLike
 *
 * @author david
 */
trait EnumLike[E] {
  def name: String
}

class EnumRegistry[E <: EnumLike[E]] {

  private val nameRegistry = scala.collection.mutable.Map[String, E]()

  def register(e: E) {
    nameRegistry.get(e.name) match {
      case Some(e2) => throw new IllegalArgumentException(s"Can't register $e; ${e.name} already exists: $e2")
      case _ => nameRegistry(e.name) = e
    }
  }

  def forName(name: String): Option[E] = nameRegistry.get(name)

  def all: Iterable[E] = {
    nameRegistry.values.toBuffer[E].sortWith((t1, t2) => t1.name < t2.name)
  }

}
