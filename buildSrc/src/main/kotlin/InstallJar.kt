import java.io.*
import java.net.URI
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

abstract class InstallJarTask : DefaultTask() {
	@get:Input
	abstract val source: Property<URI>

	@get:OutputFile
	abstract val installFile: RegularFileProperty

	fun from(url: String) {
		source.set(URI(url))
	}

	fun destination(file: File) {
		installFile.set(file)
	}

	@TaskAction fun install() {
		val jarFile = installFile.get().asFile

		if (jarFile.exists()) {
			logger.lifecycle("Jar is located at '${jarFile.path}'.")
			return
		}

		jarFile.parentFile?.mkdirs()

		val connection = source.get().toURL().openConnection()
		val totalBytes = connection.contentLengthLong

		connection.getInputStream().use { inputStream ->
			FileOutputStream(jarFile).use { outputStream ->
				val buffer = ByteArray(65536)
				var totalRead = 0L

				while (true) {
					val bytesRead = inputStream.read(buffer)
					if (bytesRead == -1) break

					outputStream.write(buffer, 0, bytesRead)
					totalRead += bytesRead

					print("Downloading client file: " +
						"${"%.2f".format(totalRead / (1024f * 1024f))} MiB / " +
						"${"%.2f".format(totalBytes / (1024f * 1024f))} MiB " +
						"(${"%.0f".format((totalRead * 100f) / totalBytes)}%)"
					)
					System.out.flush()
				}
				println()	
			}
		}

		logger.lifecycle("\nInstalled to '${jarFile.path}'.")
	}
}