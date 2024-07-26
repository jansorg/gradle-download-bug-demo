import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.kotlin.daemon.common.toHexString

plugins {
    kotlin("jvm") version "2.0.0"
    id("de.undercouch.download") version "5.6.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

allprojects {
    tasks {
        val downloadJson by registering(Download::class) {
            src("https://httpbin.org/json")
            dest(layout.buildDirectory.file("my-file.json"))
        }

        val verifyJson by registering(Verify::class) {
            dependsOn(downloadJson)

            algorithm("sha256")
            src(layout.buildDirectory.file("my-file.json"))
            checksum("bab3dfd2a6c992e4bf589eee05fc9650cc9d6988660b947a7a87b99420d108f9")
        }

        create("download") {
            doLast("download JSON file") {
                download.run {
                    src("https://httpbin.org/json")
                    dest("other.json")
                    onlyIfModified(true)
                }
            }
        }
    }
}
