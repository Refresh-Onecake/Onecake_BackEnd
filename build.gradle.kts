import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("plugin.allopen") version "1.4.32"
	kotlin("kapt") version "1.4.32"
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

group = "refresh"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}


dependencies {

	//kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//spring
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security:2.7.0")

	//db
	implementation("org.mariadb.jdbc:mariadb-java-client:2.4.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
	implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.0")

	// jwt
	implementation("org.springframework.security:spring-security-jwt:1.1.1.RELEASE")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// model mapper
	implementation("org.modelmapper:modelmapper:2.4.4")

	// validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// swagger
//	implementation("org.springdoc:springdoc-openapi-ui:1.6.1")
//	implementation("org.springdoc:springdoc-openapi-webmvc-core:1.6.1")
	implementation("io.springfox:springfox-boot-starter:3.0.0")

	// aws s3
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
