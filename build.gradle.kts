import arc.util.*
import ent.*
import java.io.*
import java.util.*
import java.util.jar.*

buildscript {
	val (mindustry, mindustryVersion, mindustrySource) = when(val version = providers.gradleProperty("mindustryVersion").get()) {
		"latest" -> Triple("Mindustry", "latest", "Anuken/Mindustry/releases/latest/download/dependencies.jar")
		"be" -> Triple("MindustryBuilds", "latest", "Anuken/MindustryBuilds/releases/download/master/latest.jar")
		else -> Triple("Mindustry", version, "Anuken/Mindustry/releases/download/[revision]/dependencies.jar")
	}

	dependencies {
		classpath("Anuken:$mindustry:$mindustryVersion")
	}

	configurations.configureEach {
		// Resolve the correct Mindustry dependency.
		resolutionStrategy.eachDependency {
			if (requested.group == "Anuken" && requested.name.startsWith("Mindustry")) {
				useTarget("Anuken:$mindustry:$mindustryVersion")
			}
		}
	}

	repositories {
		ivy {
			url = uri("https://github.com")
			patternLayout {
				artifact(mindustrySource)
				metadataSources{ artifact() }
			}
			content {
				includeVersion("Anuken", mindustry, mindustryVersion)
			}
		}
	}
}

plugins {
	java
	id("com.github.GglLfr.EntityAnno") apply false
}

val (mindustry, mindustryVersion, mindustrySource) = when(val version = providers.gradleProperty("mindustryVersion").get()) {
    "latest" -> Triple("Mindustry", "latest", "Anuken/Mindustry/releases/latest/download/dependencies.jar")
    "be" -> Triple("MindustryBuilds", "latest", "Anuken/MindustryBuilds/releases/download/master/latest.jar")
    else -> Triple("Mindustry", version, "Anuken/Mindustry/releases/download/[revision]/dependencies.jar")
}

val entVersion = providers.gradleProperty("entVersion").get()

val modArtifact = providers.gradleProperty("modArtifact").get()
val modFetch = providers.gradleProperty("modFetch").get()
val modGenSrc = providers.gradleProperty("modGenSrc").get()
val modGen = providers.gradleProperty("modGen").get()

val modVersion = providers.gradleProperty("modVersion").get()
val alpha = providers.gradleProperty("alpha").get().toBoolean()

fun mindustry(): String {
	return "Anuken:$mindustry:$mindustryVersion"
}

fun entity(module: String): String {
	return "com.github.GglLfr.EntityAnno$module:$entVersion"
}

fun jarName(type: String): String {
	return "$modArtifact$type-${modVersion + (if (alpha) "-alpha" else "")}.jar"
}

allprojects {
	apply(plugin = "java")
	sourceSets["main"].java.setSrcDirs(listOf(
		layout.projectDirectory.dir("src"),
		layout.projectDirectory.dir("build/generated/sources/annotationProcessor/java/main")
	))

	dependencies {
		registerTransform(TrimSources::class) {
			from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, ArtifactTypeDefinition.JAR_TYPE)
			to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar-stripped")
		}
	}

	configurations.configureEach {
		// Resolve the correct Mindustry dependency.
		resolutionStrategy.eachDependency {
			if (requested.group == "Anuken" && requested.name.startsWith("Mindustry")) {
				useTarget("Anuken:$mindustry:$mindustryVersion")
			}
		}
	}

	configurations.matching{ it.isCanBeResolved }.configureEach {
		attributes {
			attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar-stripped")
		}
	}

	repositories {
		// Use Ivy repository for Mindustry builds.
		ivy {
			url = uri("https://github.com")
			patternLayout {
				artifact(mindustrySource)
				metadataSources{ artifact() }
			}
			content {
				includeVersion("Anuken", mindustry, mindustryVersion)
			}
		}

		// Necessary Maven repositories to pull dependencies from.
		mavenLocal()
		mavenCentral()
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://oss.sonatype.org/content/repositories/releases/")
		maven("https://raw.githubusercontent.com/GglLfr/EntityAnnoMaven/main")
	}

	tasks.withType<JavaCompile>().configureEach {
		options.apply {
			compilerArgs.add("-Xlint:-options")
			compilerArgs.add("-implicit:none")
			compilerArgs.addAll(providers.gradleProperty("org.gradle.jvmargs").get()
				.split(Regex("\\s+"))
				.filter{ it.startsWith("--add-opens") }
				.map{ "--add-exports=${it.substring("--add-opens=".length)}" }
			)

			isIncremental = true
			isFork = false
			encoding = "UTF-8"
		}

		sourceCompatibility = "17"
		targetCompatibility = "17"
	}
}

project(":") {
	apply(plugin = "com.github.GglLfr.EntityAnno")

	val localMindustryVersion = mindustryVersion
	val mindustryDir = providers.gradleProperty("mindustryDir").getOrNull()?.let(::File)?.takeIf{ it.isDirectory }
		?: File("C:/Program Files (x86)/Steam/steamapps/common/Mindustry").takeIf{ it.isDirectory }
		?: File(OS.getAppDataDirectoryString("Mindustry")).takeIf{ it.isDirectory }
		?: layout.projectDirectory.dir("Mindustry").asFile
	val nativeClient = mindustryDir.resolve("Mindustry.exe").exists()

	configure<EntityAnnoExtension> {
		mindustryVersion = localMindustryVersion
		revisionDir = layout.projectDirectory.dir("revisions").asFile
		fetchPackage = modFetch
		genSrcPackage = modGenSrc
		genPackage = modGen
	}

	dependencies {
		// Use the entity generation annotation processor.
		compileOnly(entity(":entity"))
		annotationProcessor(entity(":entity"))

		compileOnly(mindustry())
	}

	val jar = tasks.named<Jar>("jar") {
		dependsOn(":tools:generate")

		archiveFileName = jarName("Desktop")

		// Deliberately check if the mod meta is actually written in HJSON, since, well, some people actually use
		// it. But this is also not mentioned in the `README.md`, for the mischievous reason of driving beginners
		// into using JSON instead.
		val metaJson = layout.projectDirectory.file("mod.json")
		val metaHjson = layout.projectDirectory.file("mod.hjson")

		if (metaJson.asFile.exists() && metaHjson.asFile.exists()) {
			throw IllegalStateException("Ambiguous mod meta: both `mod.json` and `mod.hjson` exist.")
		} else if (!metaJson.asFile.exists() && !metaHjson.asFile.exists()) {
			throw IllegalStateException("Missing mod meta: neither `mod.json` nor `mod.hjson` exist.")
		}

		val isJson = metaJson.asFile.exists()
		val usedMeta = if(isJson) metaJson else metaHjson

		from(
			files(sourceSets["main"].output.classesDirs),
			files(sourceSets["main"].output.resourcesDir),
			configurations.runtimeClasspath.map{ conf -> conf.map{ if (it.isDirectory) it else zipTree(it) }},

			files(layout.projectDirectory.dir("assets")),
			layout.projectDirectory.file("icon.png"),
			usedMeta
		)

		metaInf.from(layout.projectDirectory.file("LICENSE"))
	}

	val dex = tasks.register<Jar>("dex") {
		description = "Builds an Android-compatible JAR from the desktop-only JAR. Use this file for GitHub release."
		inputs.files(jar)

		archiveFileName = jarName("")

		val desktopJar = jar.flatMap{ it.archiveFile }
		val dexJar = File(temporaryDir, "Dex.jar")

		val androidSdkVersion = providers.gradleProperty("androidSdkVersion").get()
		val androidBuildVersion = providers.gradleProperty("androidBuildVersion").get()
		val androidMinVersion = providers.gradleProperty("androidMinVersion").get()

		val classpaths = configurations.compileClasspath.get().toList() + configurations.runtimeClasspath.get().toList()
		val providers = project.providers

		from(zipTree(desktopJar), zipTree(dexJar))
		doFirst {
			// Find Android SDK root.
			val sdkRoot = File(
				OS.env("ANDROID_HOME") ?: OS.env("ANDROID_SDK_ROOT")
				?: throw IllegalStateException("Neither `ANDROID_HOME` nor `ANDROID_SDK_ROOT` are set.")
			)

			// Find `d8`.
			val d8 = File(sdkRoot, "build-tools/$androidBuildVersion/${if (OS.isWindows) "d8.bat" else "d8"}")
			if (!d8.exists()) throw IllegalStateException("Android SDK `build-tools;$androidBuildVersion` isn't installed or is corrupted.")

			// Initialize a release build.
			val input = desktopJar.get().asFile
			val command = arrayListOf("$d8", "--release", "--min-api", androidMinVersion, "--output", "$dexJar", "$input")

			// Include all compile and runtime classpath.
			classpaths.forEach {
				if (it.exists()) command.addAll(arrayOf("--classpath", it.path))
			}

			// Include Android platform as library.
			val androidJar = File(sdkRoot, "platforms/android-$androidSdkVersion/android.jar")
			if (!androidJar.exists()) throw IllegalStateException("Android SDK `platforms;android-$androidSdkVersion` isn't installed or is corrupted.")

			command.addAll(arrayOf("--lib", "$androidJar"))
			if (OS.isWindows) command.addAll(0, arrayOf("cmd", "/c").toList())

			// Run `d8`.
			providers.exec{ commandLine(command) }.result.get().rethrowFailure()
		}
	}

	val copyJar = tasks.register<DefaultTask>("copyJar") {
		description = "Copies the desktop JAR to the `mods/` folder."
		mustRunAfter(jar)

		val sourceFile = layout.projectDirectory.file("build/libs/${jarName("Desktop")}").asFile
		val destination =
			if (nativeClient && mindustryDir.path.replace('\\', '/').contains("/steamapps/common/Mindustry", ignoreCase = true))
			mindustryDir.resolve("saves/mods") else mindustryDir.resolve("mods")
		val destFile = destination.resolve(sourceFile.name)

		inputs.file(sourceFile).optional()
		outputs.file(destFile)

		doLast {
			if (!sourceFile.exists()) {
				logger.lifecycle("JAR not found. Skipping task.")
				return@doLast
			}

			destination.mkdirs()
			sourceFile.copyTo(destFile, overwrite = true)

			logger.lifecycle("Copied ${sourceFile.name} to '${destination.path}'.")
		}
	}

	val downloadClient = tasks.register<InstallJarTask>("downloadClient") {
		description = "Checks for the Mindustry client matching `mindustryVersion` and downloads it if it does not exist."

		if (nativeClient) {
			enabled = false
		} else {
			val versionProperties = buildscript.classLoader.getResourceAsStream("version.properties").use {
				val props = Properties()
				props.load(it)
				props
			}
			val build = versionProperties.getProperty("build")
			val type = versionProperties.getProperty("type")

			mindustryDir.mkdirs()

			from(when(type) {
				"official" -> "https://github.com/Anuken/Mindustry/releases/download/v$build/Mindustry.jar"
				"bleeding-edge" -> "https://github.com/Anuken/MindustryBuilds/releases/download/v$build/Mindustry-BE-Desktop-$build.jar"
				else -> throw GradleException("Invalid Mindustry version type '$type'; cannot download client.")
			})
			destination(mindustryDir.resolve("Mindustry-$build.jar"))

			val launcher = mindustryDir.resolve("Mindustry.bat")
			outputs.file(launcher)

			doLast {
				launcher.writeText(
					"""
					@echo off
					java -cp "%~dp0Mindustry-$build.jar" mindustry.desktop.DesktopLauncher %*
					""".trimIndent()
				)
			}
		}
	}

	tasks.register<Exec>("runClient") {
		description = "Runs Mindustry with the current mod and client."
		dependsOn(copyJar, downloadClient)

		doFirst {
			if (nativeClient) {
				logger.lifecycle("Native Mindustry executable detected; skipping download.")
				commandLine(mindustryDir.resolve("Mindustry.exe").absolutePath)
			} else {
				commandLine(mindustryDir.resolve("Mindustry.bat").absolutePath)
			}
			logger.lifecycle("Starting Mindustry.")
		}
	}
}

abstract class TrimSources : TransformAction<TransformParameters.None> {
	@get:InputArtifact
	abstract val file: Provider<FileSystemLocation>

	override fun transform(outputs: TransformOutputs) {
		val input = file.get().asFile
		val classes = outputs.file(input.name)

		JarFile(input).use { jar ->
			val entries = jar.entries()
			JarOutputStream(FileOutputStream(classes)).use { classes ->
				for (entry in entries) {
					if (entry.name.endsWith(".java")) continue

					classes.putNextEntry(JarEntry(entry.name))
					jar.getInputStream(entry).use{ it.copyTo(classes) }
					classes.closeEntry()
				}
			}
		}
	}
}