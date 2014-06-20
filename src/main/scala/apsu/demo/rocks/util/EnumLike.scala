package apsu.demo.rocks.util

import scala.collection.mutable

/**
 * An approximation of Java-style rich enums based on the (pre-Java 5) pattern
 * presented in Effective Java 1st edition. Allows pattern matching. Usage:
 * <pre>
 *   object MyEnum {
 *       val firstInstance = MyEnum("firstInstance")
 *       val secondInstance = MyEnum("secondInstance")
 *
 *       def all = MyEnumRegistry.all
 *       def forName(name: String) = MyEnumRegistry.forName(name)
 *   }
 *
 *   case class MyEnum private(name: String) extends EnumLike[MyEnum] {
 *       MyEnumRegistry.register(this)
 *   }
 *
 *   private object MyEnumRegistry extends EnumRegistry[MyEnum]
 * </pre>
 *
 * @author david
 */
trait EnumLike[E] {
  def name: String
}

class EnumRegistry[E <: EnumLike[E]] {

  private val nameRegistry = mutable.OpenHashMap[String, E]()

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
