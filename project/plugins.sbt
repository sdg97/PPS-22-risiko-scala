addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.4.0")
// addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3") // does not work for Scala 3
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")
addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.3.1")
addDependencyTreePlugin
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")

resolvers += Resolver.jcenterRepo
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.9.0")