import sbt._
import Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin.releaseSettings

object JobServerRelease {
  import ls.Plugin._
  import LsKeys._
  import sbtrelease.ReleasePlugin.ReleaseKeys._
  import sbtrelease.ReleaseStateTransformations._

  lazy val implicitlySettings = {
    lsSettings ++ Seq(
      homepage := Some(url("https://github.com/tap/tap-engines")),
      (tags in lsync) := Seq("spark", "akka", "rest"),
      (description in lsync) := "TAP",
      (externalResolvers in lsync) := Seq("TAP Bintray" at "http://dl.bintray.com/tap-engines/maven"),
      (ghUser in lsync) := Some("tap"),
      (ghRepo in lsync) := Some("tap"),
      (ghBranch in lsync) := Some("master")
    )
  }

  val syncWithLs = (ref: ProjectRef) => ReleaseStep(
                     check  = releaseTask(LsKeys.writeVersion in lsync in ref),
                     action = releaseTask(lsync in lsync in ref)
                   )

  lazy val ourReleaseSettings = releaseSettings ++ Seq(
    releaseProcess := Seq(
      checkSnapshotDependencies,
      runClean,
      runTest,
      inquireVersions,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      syncWithLs(thisProjectRef.value),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}
