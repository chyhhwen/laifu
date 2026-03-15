plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	kotlin("jvm") version "1.9.24"
}

group = "laifu.fu"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group == "org.jetbrains.kotlin") {
			useVersion("1.9.24")
		}
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["sentryVersion"] = "8.16.0"
extra["springAiVersion"] = "1.0.0"
extra["springCloudVersion"] = "2025.0.0"

dependencies {
	//compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("io.github.wimdeblauwe:htmx-spring-boot:4.0.1")
	implementation("io.sentry:sentry-spring-boot-starter-jakarta")
	//implementation("org.springframework.ai:spring-ai-starter-model-anthropic")
	//implementation("org.springframework.ai:spring-ai-starter-model-openai")
	// implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
	//implementation("org.springframework.session:spring-session-data-redis")
	implementation("org.springframework.session:spring-session-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("org.xerial:sqlite-jdbc")
	developmentOnly("org.springframework.ai:spring-ai-spring-boot-docker-compose")
	implementation("me.friwi:jcefmaven:135.0.20")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation(kotlin("stdlib-jdk8"))
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
		mavenBom("io.sentry:sentry-bom:${property("sentryVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}

// JCEF（jcefmaven）在 JDK 9+ 會觸發 JPMS 強封裝限制，需要顯式打開/導出部分 AWT 內部包。
// 否則在 macOS 上會報：IllegalAccessError: cannot access class sun.awt.AWTAccessor
val jcefJvmArgs = listOf(
	"--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
	"--add-exports=java.desktop/sun.lwawt=ALL-UNNAMED",
	"--add-exports=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
)

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	jvmArgs(jcefJvmArgs)
}

tasks.withType<JavaExec>().configureEach {
	// 覆蓋其他 JavaExec（例如自定義 launcher）啟動時的行為
	jvmArgs(jcefJvmArgs)
}

// --- jpackage 多平台自動打包任務 ---
tasks.register<Exec>("packageApp") {
	group = "distribution"
	description = "Packages the application into a standalone native bundle based on the current OS."

	dependsOn("bootJar")

	val jarName = "${project.name}-${project.version}.jar"
	val outputDir = layout.buildDirectory.dir("dist").get().asFile.absolutePath
	val inputDir = layout.buildDirectory.dir("libs").get().asFile.absolutePath
	val osName = System.getProperty("os.name").lowercase()

	// 根據作業系統決定打包類型
	val type = when {
		osName.contains("mac") -> "app-image" // 產出 .app 目錄，若要安裝檔改為 "dmg"
		osName.contains("win") -> "app-image" // 改為 "app-image" 以避免依賴 WiX，若要安裝檔則改回 "exe" 或 "msi"
		osName.contains("linux") -> "app-image" // 改為 "app-image"
		else -> "app-image"
	}

	// 確保輸出目錄存在
	doFirst {
		val distDir = file(outputDir)
		if (distDir.exists()) distDir.deleteRecursively()
		distDir.mkdirs()
	}

	val jpackageArgs = mutableListOf(
		"jpackage",
		"--input", inputDir,
		"--dest", outputDir,
		"--name", "FuChat",
		"--main-jar", jarName,
		"--main-class", "org.springframework.boot.loader.launch.JarLauncher",
		"--type", type,
		"--vendor", "Laifu",
		"--app-version", "1.0.0",
		"--java-options", "-Djava.awt.headless=false",
		"--java-options", "-Dspring.profiles.active=prod"
	)

	// 設定圖標 (Windows: .ico, macOS: .icns, Linux: .png)
	val iconFile = when {
		osName.contains("win") -> file("chi.ico")
		osName.contains("mac") -> file("chi.icns")
		else -> file("chi.png")
	}
	if (iconFile.exists()) {
		jpackageArgs.addAll(listOf("--icon", iconFile.absolutePath))
	}

	// macOS 特有參數
	if (osName.contains("mac")) {
		jpackageArgs.addAll(listOf(
			"--mac-package-identifier", "com.laifu.fuchat",
			"--java-options", "--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
			"--java-options", "--add-exports=java.desktop/sun.lwawt=ALL-UNNAMED",
			"--java-options", "--add-exports=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
		))
	}

	if (osName.contains("win") && (type == "exe" || type == "msi")) {
		jpackageArgs.addAll(listOf(
			"--win-shortcut",
			"--win-menu"
		))
	}

	doFirst {
		println("正在執行 jpackage，參數：$jpackageArgs")
		val distDir = file(outputDir)
		if (distDir.exists()) distDir.deleteRecursively()
		distDir.mkdirs()
	}

	commandLine(jpackageArgs)

	doLast {
		println("打包完成！類型: $type, 產出物在: $outputDir")
	}
}
