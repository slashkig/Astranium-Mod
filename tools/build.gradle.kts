tasks.register<JavaExec>("generate") {
	classpath = sourceSets["main"].runtimeClasspath
	mainClass.set("astramod.tools.Tools")
}