import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle

object GradleProfilerPublishing : BuildType({
    name = "Gradle profiler Publishing"
    description = "Publish Gradle profiler Gradle's Artifactory repository"

    artifactRules = "build/reports/** => .teamcity/reports"

    gradleProfilerVcs()

    params {
        javaHome(Os.linux, JavaVersion.ORACLE_JAVA_8)
        text("ARTIFACTORY_USERNAME", "bot-build-tool", allowEmpty = true)
        password("ARTIFACTORY_PASSWORD", "credentialsJSON:d94612fb-3291-41f5-b043-e2b3994aeeb4", display = ParameterDisplay.HIDDEN)
        password("pgpSigningKey", "credentialsJSON:ae6de222-f77d-4c71-b94e-fac6da485143", display = ParameterDisplay.HIDDEN)
        password("pgpSigningPassphrase", "credentialsJSON:f75db3bd-4f5b-4591-b5cb-2e76d91f57f5", display = ParameterDisplay.HIDDEN)
        password("mavenCentralStagingRepoUser", "credentialsJSON:ce6ff00a-dc06-4b9b-aa1f-7b01bea2eb2f", display = ParameterDisplay.HIDDEN)
        password("mavenCentralStagingRepoPassword", "credentialsJSON:f3c71885-0cec-49c9-adcf-d21536fcf1ca", display = ParameterDisplay.HIDDEN)

        param("env.ORG_GRADLE_PROJECT_artifactoryUsername", "%ARTIFACTORY_USERNAME%")
        param("env.ORG_GRADLE_PROJECT_artifactoryPassword", "%ARTIFACTORY_PASSWORD%")
        param("env.ORG_GRADLE_PROJECT_githubToken", "%github.bot-teamcity.token%")
        param("env.ORG_GRADLE_PROJECT_sdkmanKey", "%gradleprofiler.sdkman.key%")
        param("env.ORG_GRADLE_PROJECT_sdkmanToken", "%gradleprofiler.sdkman.token%")
        param("env.GRADLE_CACHE_REMOTE_USERNAME", "%gradle.cache.remote.username%")
        param("env.GRADLE_CACHE_REMOTE_PASSWORD", "%gradle.cache.remote.password%")
        param("env.PGP_SIGNING_KEY", "%pgpSigningKey%")
        param("env.PGP_SIGNING_KEY_PASSPHRASE", "%pgpSigningPassphrase%")
        param("env.MAVEN_CENTRAL_STAGING_REPO_USER", "%mavenCentralStagingRepoUser%")
        param("env.MAVEN_CENTRAL_STAGING_REPO_PASSWORD", "%mavenCentralStagingRepoPassword%")
    }

    steps {
        gradle {
            tasks = "clean publishToSonatype closeSonatypeStagingRepository gitPushTag releaseToSdkMan"
            gradleParams = toolchainConfiguration(Os.linux) + " -Dgradle.cache.remote.push=true"
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
            buildFile = ""
        }
    }

    agentRequirement(Os.linux)
})
