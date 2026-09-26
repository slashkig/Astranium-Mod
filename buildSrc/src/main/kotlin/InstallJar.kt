import java.io.*
import java.net.URI
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

abstract class InstallJar : DefaultTask() {
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

		jarFile.parentFile.mkdirs()

		val connection = source.get().toURL().openConnection()
		val contentLength = connection.contentLengthLong

		val input = connection.getInputStream()
		val output = FileOutputStream(jarFile)

		val buffer = ByteArray(8192)
		var totalRead = 0L
		var prevPercent = -1

		input.use { inputStream ->
			output.use { outputStream ->
				while (true) {
					val bytesRead = inputStream.read(buffer)
					if (bytesRead == -1) break

					outputStream.write(buffer, 0, bytesRead)
					totalRead += bytesRead

					if (contentLength > 0) {
						val percent = (totalRead * 100 / contentLength).toInt()
						if (percent != prevPercent) {
							print("\rDownloading ${jarFile.name}: $percent%")
							System.out.flush()
							prevPercent = percent
						}
					}
				}
			}
		}

		logger.lifecycle("\nInstalled to '${jarFile.path}'.")
	}
}