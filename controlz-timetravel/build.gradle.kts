import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinSerialization)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.doseeare.controlz"
version = "0.1.0"

kotlin {
	jvm()
	
	androidTarget {
		publishLibraryVariants("release")
		withSourcesJar(publish = true)
	}
	
	val xcfName = "controlz-timetravelKit"
	
	listOf(
		iosArm64(),
		iosX64(),
		iosSimulatorArm64()
	).forEach {
		it.binaries.framework {
			baseName = xcfName
			isStatic = true
		}
	}
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.kotlinx.coroutines.core)
				implementation(libs.kotlinx.serialization)
				implementation(libs.kotlinx.datetime)
				implementation(libs.androidx.lifecycle.viewmodelCompose)
				implementation(libs.androidx.lifecycle.runtimeCompose)
				implementation(libs.jsonTree)
				
				implementation(compose.runtime)
				implementation(compose.foundation)
				implementation(compose.material3)
				implementation(compose.materialIconsExtended)
				implementation(compose.ui)
				implementation(compose.components.resources)
			}
		}
		
		commonTest {
			dependencies {
				implementation(libs.kotlin.test)
			}
		}
		
		androidMain {
			dependencies {
				// Add Android-specific dependencies here. Note that this source set depends on
				// commonMain by default and will correctly pull the Android artifacts of any KMP
				// dependencies declared in commonMain.
			}
		}
		
	
		
		iosMain {
			dependencies {
				// Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
				// Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
				// part of KMP’s default source set hierarchy. Note that this source set depends
				// on common by default and will correctly pull the iOS artifacts of any
				// KMP dependencies declared in commonMain.
			}
		}
	}
	
}

android {
	namespace = "io.github.doseeare.controlz"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	
	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}
	
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	
	publishing {
		singleVariant("release") {
			withSourcesJar()
		}
	}
}

// Настройка публикации через vanniktech.mavenPublish
mavenPublishing {
	// Coordinates для Maven Central
	
	configure(
		KotlinMultiplatform(
			javadocJar = JavadocJar.Empty(),
			sourcesJar = true
		)
	)
	
	publishToMavenCentral(host = SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)
	signAllPublications()
	
	coordinates(
		groupId = "io.github.doseeare.controlz",
		artifactId = "timetravel",
		version = version.toString()
	)
	pom {
		name.set("Time Travel")
		description.set("A library for easily debugging application state changes")
		url.set("https://github.com/doseeare/ControlZ")
		
		licenses {
			license {
				name.set("The Apache License, Version 2.0")
				url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
			}
		}
		
		developers {
			developer {
				id.set("doseeare")
				name.set("Dastan Beksultanov")
				email.set("doseeare@gmail.com")
			}
		}
		
		scm {
			url.set("https://github.com/doseeare/ControlZ/")
			connection.set("scm:git:git://github.com/doseeare/controlz.git")
			developerConnection.set("scm:git:ssh://git@github.com/doseeare/controlz.git")
		}
		
		issueManagement {
			system.set("GitHub")
			url.set("https://github.com/doseeare/ControlZ/issues")
		}
	}
}
