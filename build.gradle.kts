plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("gg.jte.gradle") version "3.1.16"
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
tasks.named("compileKotlin") {
	dependsOn("generateJte")
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
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("gg.jte:jte-spring-boot-starter-3:3.1.16")
	implementation("io.github.wimdeblauwe:htmx-spring-boot:4.0.1")
	implementation("io.sentry:sentry-spring-boot-starter-jakarta")
	//implementation("org.springframework.ai:spring-ai-starter-model-anthropic")
	//implementation("org.springframework.ai:spring-ai-starter-model-openai")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
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

jte {
	generate()
	binaryStaticContent = true
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
