# apsu-demo-scala 

A demonstration of the [apsu-core](https://github.com/chronodm/apsu-core-scala) entity framework.

## Rocks (apsu.demo.rocks) 

A game where you fly around and shoot rocks until the rocks hit you
and you die. (Note: dying not yet implemented.)

To build and run:

- download apsu-core and build and install it in your local Maven
  repository using `./gradlew install`
- download apsu-core-scala and build a distribution using
  `./gradlew installApp`
- run `build/install/apsu-demo-scala/bin/apsu-demo-scala` (or
  the `.bat` equivalent)

Note that Rocks uses double precision, which is completely
unnecessary but makes the code simpler. We can get away with it
because the game isn't very taxing.