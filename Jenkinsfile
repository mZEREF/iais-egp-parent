// List of email recipients.
EMAILS_TO_NOTIFY = 'tanweijie@toppanecquaria.com, zamri@toppanecquaria.com'
// Specify the connection URL to the Docker daemon.
DOCKER_URL = 'tcp://hub.ecquaria.com:2375'
// Destination directory when we do `git clone`.
CHECKOUT_DIRECTORY="checkouts"

// Output of clair would be grouped up here
CLAIR_OUTPUT_DIRECTORY = "clair"

// Specify the IP address of hub.ecquaria.com. Without this, containers are not able to resolve
// this address -- unless, network=host is used.
HUB_IP = '192.168.0.228'

// Gitlab requires authentication, specify the credential ID here.
GITLAB_CREDENTIALS_ID = 'moh-iais-gitlab'
GITLAB_CREDENTIALS = [usernamePassword(
        credentialsId: GITLAB_CREDENTIALS_ID,
        passwordVariable: 'GIT_PASSWORD',
        usernameVariable: 'GIT_USERNAME')
]

// Specify the Gitlab URL used by the project -- note the use of single quote.
PROJECT_GITLAB_URL =
    'https://${GIT_USERNAME}:${GIT_PASSWORD}@hub.ecquaria.com/gitlab/moh-iais/iais-egp.git'

SONARQUBE_CREDENTIALS_ID = 'moh-iais-sonartoken'

MAVEN_LOCAL_REPO = '/var/jenkins_home/.m2_for_moh_iais'

//Repos for pushing docker images
DOCKER_BUILD_REPO = 'hub.ecquaria.com:8251'
DOCKER_RELEASE_REPO = 'hub.ecquaria.com:8253'
//All of their docker images have this prefix
DOCKER_IMAGENAME_PREFIX = 'docker-mohiais-egp'

// List of images in code_directory|image_name format
DOCKER_IMAGES = [
    'be-main|main-web',
    'fe-main|fe-main',
    'hcsa-licence-be|hcsa-licence',
    'hcsa-licence-fe|hcsa-application',
    'payment|payment-web',
    'systemadmin|systemadmin-web',
    'web|web'
]
// Stores meta information about images that have been built in this pipeline (i.e docker build)
DOCKER_IMAGES_META = []

// Nexus requires authentication, specify the credential ID here.
NEXUS_CREDENTIALS_ID = 'moh-iais-jenkins'
NEXUS_CREDENTIALS = [usernamePassword(
        credentialsId: NEXUS_CREDENTIALS_ID,
        passwordVariable: 'NEXUS_PASSWORD',
        usernameVariable: 'NEXUS_USERNAME')
]

// Specify the project ID for the project (this value is retrieved from Dependency Track).
DEPENDENCY_TRACK_PROJECT = '47741070-b027-4430-acc7-1e04adba7780'

AD_TOOLS_VERSION = "7361af0"

// Working directory for when we're handling the cicd folder.
AUTO_DEPLOYMENT_DIRECTORY="auto-deployment"

//EDS
EDS_CREDENTIALS_ID = 'moh-iais-eds'
EDS_CREDENTIALS = [usernamePassword(
        credentialsId: EDS_CREDENTIALS_ID,
        passwordVariable: 'EDS_PASSWORD',
        usernameVariable: 'EDS_ID')
]
EDS_URL = 'http://192.168.1.119:8080'

// Working directory for when we are preparing file for verification and transfer.
TX_BUILD_FOLDER = "transfer-build"

// This directory contains the files we want to transfer over (e.g docker images).
PAYLOAD_FOLDER = "${TX_BUILD_FOLDER}/payload"

// The signing algorithm to use (via OpenSSL).
SIGN_DIGEST_ALGO = "-sha256"

// Basically an archive of the payload folder (nothing special)
ARCHIVE_1 = "${TX_BUILD_FOLDER}/project-files.tar"
// This is what is signed -- the resulting signature will be verified by the verifier.
ARCHIVE_1_HASH = "${ARCHIVE_1}.hash"

// This is a new archive consisting of ARCHIVE_1 + the verifier's signature.
ARCHIVE_2 = "${TX_BUILD_FOLDER}/project-files-with-approval.tar"

// Basically and encrypted version of ARCHIVE_2.
ARCHIVE_3 = "${TX_BUILD_FOLDER}/project-files-with-approval.bin"

// This is a new archive consisting of ARCHIVE_3 + encrypted symmetric key + initialization vector (IV)
ARCHIVE_4 = "${TX_BUILD_FOLDER}/project-payload.tar"

// This file contains the signature that the verifier will have to verify.
SIGNATURE_FROM_JENKINS="${ARCHIVE_1_HASH}.jenkins"

// This archive contains the hash and signature file for the verifier.
VERIFICATION_PACKAGE="${TX_BUILD_FOLDER}/verification-package.zip"

// The encryption algorithm to use (via OpenSSL).
SYMMETRIC_KEY_ALGO = '-AES-256-CBC'
SYMMETRIC_KEY_SIZE = 32
SYMMETRIC_KEY = "${TX_BUILD_FOLDER}/key"
SYMMETRIC_KEY_ENCRYPTED = "${SYMMETRIC_KEY}.bin"
INITIALIZATION_VECTOR_SIZE = 16
INITIALIZATION_VECTOR = "${TX_BUILD_FOLDER}/iv"

// jenkins keys used for signing the package
JENKINS_PRIVATE_KEY_CREDENTIALS = [file(
        credentialsId: 'jenkins-hub-ci-key',
        variable: 'JENKINS_PRIVATE_KEY')
]

// remote SFTP uses 2 authentication (private key, and password -- maybe not in that order.)
//TODO change from mock sftp to actual sftp server
SFTP_CREDENTIALS = [
        sshUserPrivateKey(
                credentialsId: 'moh-iais-mock-sftp-key',
                keyFileVariable: 'SFTP_PRIVATE_KEY'),
        usernamePassword(
                credentialsId: 'moh-iais-mock-sftp-userpw',
                passwordVariable: 'SSHPASS',
                usernameVariable: 'SFTP_USER_ID')
]

// used by Katalon Runtime Engine
KATALON_OFFLINE_LICENSE_CREDENTIALS = [file(
    credentialsId: '0bfbaa23-a895-48ce-b367-8a91cad331ec',
    variable: 'KATALON_OFFLINE_LICENSE')
]

// Destination directory when we do `git clone`.
CHECKOUT_DIRECTORY_AUTOMATED_TESTING="checkouts-at"

//Specify the Gitlab URL used by the project -- note the use of single quote.
PROJECT_GITLAB_URL_AUTOMATED_TESTING =
    'https://${GIT_USERNAME}:${GIT_PASSWORD}@hub.ecquaria.com/gitlab/moh-iais/iais-qa.git'

configurePipeline()

try{
    node{
        // need root to clear the ${CHECKOUT_DIRECTORY_AUTOMATED_TESTING} directory -- container 
        // keeps creating files with root owner, even after specifying KATALON_USER_ID environment 
        // variable.
        def dockerArgs = [
            "--entrypoint=''",
            "-u 0:0"
        ]

        docker
            .image('hub.ecquaria.com/alpine/git:1.0.7')
            .inside(dockerArgs.join(" ")){
                sh """
                    rm -rf ${CHECKOUT_DIRECTORY_AUTOMATED_TESTING}
                """
            }

        // clean the workspace
        cleanWs()

        docker.withServer(DOCKER_URL) {
            checkout()
            buildAndDeploy()
            sonarqube()
        }
    }

    qualityGate()

    node{
        docker.withServer(DOCKER_URL) {
            dependencyCheck()
            dependencyTrack()

            withEnv(['DOCKER_CERT_PATH=/var/jenkins_home/.docker']) {
                buildDockerImages()
                scanDockerImages()
            }
            deploySIT()
        }
    }

    // block progression of pipeline until deployer has test environment ready.
    if(doAutomatedTestGate()){
        node{
            currentBuild.result = 'ABORTED'
        }
        return
    }

    node{
        docker.withServer(DOCKER_URL) {
            runAutomatedTests()
        }
    }

    // block progression of pipeline until QA makes a decision.
    if(doQAGate()){
        node{
            currentBuild.result = 'ABORTED'
        }
        return
    }

    currentBuild.result = 'SUCCESS'
}
catch(err){
    println "Build caught an exception: ${err.getMessage()}"

    currentBuild.result = 'FAILURE'
}
finally{
    sendEmailNotification()
}

/**
 * Programatically configures the build.
 *
 * Curent list of items configured:
 *  (1) Concurrent builds are disabled.
 *  (2) Project should accept these parameters:
 *      (a) TAG_TO_BUILD
 */
def configurePipeline(){
    properties(
        [
            [$class: 'JiraProjectProperty'],
            disableConcurrentBuilds(),
            parameters(
                [string(
                    defaultValue: '',
                    description: '''\
                    The value specified here (referencing a git branch or git tag) would be passed
                    along to the `--branch <name> / -b <name>` option during git clone operation.
                    '''.stripMargin().stripIndent(),
                    name: 'TAG_TO_BUILD',
                    trim: true),
                string(
                    defaultValue: '',
                    description: '''\
                    Since we will need to transport commits over to the other side, this tag 
                    provides some sort of a baseline (or a hint) that would be given to Git in 
                    order for it to decide which commits to include (in an attempt to save the 
                    payload size).

                    Please note that this field is optional.

                    If you do not specify any value for this field, the build will still proceed.
                    '''.stripMargin().stripIndent(),
                    name: 'BASELINE_TAG',
                    trim: true)]
            )
        ])
}

def sendEmailNotification(){
    def subjectHeader = ""
    def emailBody = ""

    if (currentBuild.result == 'SUCCESS'){
        subjectHeader = "Build succeeded: ${currentBuild.fullDisplayName}"
        emailBody = "Build succeeded, see ${env.BUILD_URL}"
    }
    else if (currentBuild.result == 'FAILURE'){
        subjectHeader = "Build failed: ${currentBuild.fullDisplayName}"
        emailBody = "Something went wrong with ${env.BUILD_URL}"
    }
    else if (currentBuild.result == 'ABORTED')
    {
        subjectHeader = "Build aborted: ${currentBuild.fullDisplayName}"
        emailBody = "Build aborted, see ${env.BUILD_URL}"
    }
    else if (currentBuild.result == 'UNSTABLE')
    {
        subjectHeader = "Build unstable: ${currentBuild.fullDisplayName}"
        emailBody = "Build unstable, see ${env.BUILD_URL}"
    }
    else{
        println "Email for build result -> [${currentBuild.result}], not handled yet."
    }


    // send email
    mail to: EMAILS_TO_NOTIFY, subject: subjectHeader, body: emailBody
}

// setup parameters to use when calling docker
// use host network to call nexus directly instead of going through nginx. Pushing images through nginx results in http error response code 413
def getDockerArgs(){
    def dockerArgs = [
        "--entrypoint=''",
        "--add-host=\"hub.ecquaria.com:${HUB_IP}\"",
        "-e TZ=Asia/Singapore"
    ]
    dockerArgs.join(" ")
}

def checkout(){
    stage('Checkout'){
        withCredentials(GITLAB_CREDENTIALS) {
            docker
                .image('hub.ecquaria.com/alpine/git:1.0.7')
                .inside(getDockerArgs()){
                    sh """
                    git clone \
                        --quiet \
                        -c advice.detachedHead=false \
                        -b ${TAG_TO_BUILD} \
                        ${PROJECT_GITLAB_URL} \
                        ${CHECKOUT_DIRECTORY}
                    """
                }
        }
    }
}

def buildAndDeploy(){
    stage('Build & Deploy'){
        docker
            .image('hub.ecquaria.com/maven:3.6.3-jdk-8')
            .inside(getDockerArgs()){
                sh """
                cd ${CHECKOUT_DIRECTORY}

                mvn \
                    --no-transfer-progress \
                    -Dmaven.repo.local=${MAVEN_LOCAL_REPO} \
                    -P sg-nexus \
                    -s settings.xml \
                    clean deploy
                """
            }
    }
}

/**
 * Perform static code analysis via SonarQube.
 */
def sonarqube(){
    stage('SonarQube Analysis'){
        withSonarQubeEnv(credentialsId: SONARQUBE_CREDENTIALS_ID){
            docker
                .image('hub.ecquaria.com/maven:3.6.3-jdk-8')
                .inside(getDockerArgs()){
                    sh """
                        cd ${CHECKOUT_DIRECTORY}

                        mvn \
                            --no-transfer-progress \
                            -Dmaven.repo.local=${MAVEN_LOCAL_REPO} \
                            -Dsonar.projectKey=iais-iais-egp-sit \
                            -Dsonar.projectName=iais-iais-egp-sit \
                            -P sg-nexus \
                            -s settings.xml \
                            sonar:sonar
                    """
                }
        }
    }
}

/**
 * Refer to https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-jenkins/#header-6
 * for more details (i.e prerequisite, implementation details).
 *
 * The "sleep" is there to work around an issue (probabyl for a race condition). Refer to
 * https://community.sonarsource.com/t/need-a-sleep-between-withsonarqubeenv-and-waitforqualitygate-or-it-spins-in-in-progress/2265
 * for more details.
 */
def qualityGate(){
    stage("Quality Gate"){
        sleep(10)

        timeout(time: 1, unit: 'HOURS') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
}

def buildDockerImages(){
    stage('Build Docker Images'){
        def pom = readMavenPom file: "${CHECKOUT_DIRECTORY}/pom.xml"
        // def imageTag = "${pom.properties.revision}-b${BUILD_NUMBER}"
        def imageTag = "${TAG_TO_BUILD}-b${BUILD_NUMBER}"

        docker.withRegistry("https://${DOCKER_BUILD_REPO}", NEXUS_CREDENTIALS_ID){
            for (image in DOCKER_IMAGES) {
                def codeDir = image.split("\\|")[0]
                def imageName = DOCKER_IMAGENAME_PREFIX + '/' + image.split("\\|")[1]
                def dockerImage = docker.build("${imageName}","--force-rm --quiet -f ${CHECKOUT_DIRECTORY}/${codeDir}/Dockerfile_SG ${CHECKOUT_DIRECTORY}/${codeDir}")

                println "Image Name: $dockerImage"

                /*
                * Keeps track of the different labels based on the same image.
                *
                * Meta information is kept to ease future operations (with label manipulation).
                *
                * For example:
                * [
                *   "basename": "iais-organization-fe-eservice",
                *   "basename-with-tag": "iais-organization-fe-eservice:cicd-BN",
                *   "basename-with-tag-for-release": "iais-organization-fe-eservice:cicd-BN-REL",
                *   "with-repo-latest": "hub.ecquaria.com:8151/iais-organization-fe-eservice",
                *   "with-repo-tagged": "hub.ecquaria.com:8151/iais-organization-fe-eservice:cicd-BN"
                *   "with-release-repo-tagged": "hub.ecquaria.com:8151/iais-organization-fe-eservice:cicd-BN-REL"
                * ]
                */
                def imgMeta = [:]
                imgMeta.put('basename', dockerImage.id)
                imgMeta.put('basename-with-tag', "${dockerImage.id}:${imageTag}")
                imgMeta.put('basename-with-tag-for-release', "${dockerImage.id}:${imageTag}-REL")
                imgMeta.put('with-repo-latest', dockerImage.imageName())

                // do a re-tag
                def newImageName = dockerImage.tag(imageTag)

                // retrieve the docker image instance
                dockerImage = docker.image(newImageName)

                // update meta information
                imgMeta.put('with-repo-tagged', dockerImage.imageName())

                // add to the list of docker images built
                DOCKER_IMAGES_META.push(imgMeta)

                // push docker image to repository
                dockerImage.push()
            }
        }
    }
}

/**
 * Perform static analysis of vulnerabilities in appc and docker containers via Clair.
 *
 * Before scanning can begin, we'll need to start up 3 containers:
 *  - (1) clair db (think of it as vulnerability definitions)
 *  - (2) clair server
 *  - (3) clair client
 *
 * These 3 containers are linked via the 'clair' network.
 */
def scanDockerImages(){
    stage("Scan Docker Images"){

        //give the containers & network unique names in case other build jobs are running clair
        def suffix = "ecq-iais-egp-b${BUILD_NUMBER}"
        def clairNetworkName = "clair_${suffix}"
        def clairDbName = "postgres_${suffix}"
        def clairSvrName = "clairSvr_${suffix}"

        // recreate the clair output directory
        sh "rm -rf ${CLAIR_OUTPUT_DIRECTORY}; mkdir -p ${CLAIR_OUTPUT_DIRECTORY};"

        // declare clair images (yes, we are going bleeding edge!)
        def clairDB = docker.image('hub.ecquaria.com/arminc/clair-db:latest')
        def clairServer = docker.image('hub.ecquaria.com/arminc/clair-local-scan:v2.1.4_a536c757b48ebca4b7b8bffca794d52b42a3de9d')
        def clairClient = docker.image('hub.ecquaria.com/objectiflibre/clair-scanner:latest')

        //to force grab the latest
        clairDB.pull()

        def clairDBContainer
        def clairServerContainer

        try{

            sh "docker network create ${clairNetworkName}"

            def dockerArgs = [
                    "--network=${clairNetworkName}",
                    "--name=${clairDbName}"
            ]

            // (1/3) start clair database
            clairDBContainer = clairDB.run(dockerArgs.join(" "))
            sleep 15 // this is a hack -- give time for DB to be ready.

            // (2/3) start clair server
            dockerArgs = [
                    "--network=${clairNetworkName}",
                    "--name=${clairSvrName}",
                    "--link=${clairDbName}:postgres"
            ]
            clairServerContainer = clairServer.run(dockerArgs.join(" "))
            sleep 15 // this is a hack -- give time for server to be ready.

            // (3/3) start clair scanner
            dockerArgs = [
                    "--network=${clairNetworkName}",
                    "--entrypoint=''",
                    "--add-host=\"hub.ecquaria.com:${HUB_IP}\"",
                    "-e=\"DOCKER_HOST=${DOCKER_URL}\""
            ]
            docker.withRegistry("https://${DOCKER_BUILD_REPO}", NEXUS_CREDENTIALS_ID){
                clairClientContainer = clairClient.inside(dockerArgs.join(" ")){
                    DOCKER_IMAGES_META.each{imgMeta ->
                        // only used to name the log file generated.
                        def fileName = "${imgMeta['with-repo-tagged'].replace('/', '_')}"
                        // commands to run in the container
                        // reference to severity here: https://github.com/quay/clair/blob/master/database/severity.go
                        def dockerCmds = [
                                'clair',
                                '--ip="$(hostname -i)"',
                                "--log=${CLAIR_OUTPUT_DIRECTORY}/${fileName}.log",
                                "--report=${CLAIR_OUTPUT_DIRECTORY}/${fileName}.json",
                                '--threshold="Defcon1"',
                                "--clair=http://${clairSvrName}:6060",
                                "${imgMeta['with-repo-tagged']}"
                        ]
                        sh "${dockerCmds.join(' ')} &> /dev/null"
                    }
                }
            }
        }
        catch(err){
            println "Caught ${err}"
            throw err
        }
        finally{
            archiveArtifacts allowEmptyArchive: true, artifacts: "$CLAIR_OUTPUT_DIRECTORY/**"
            if(clairDBContainer){
                println "Manually stopping Clair DB container."
                clairDBContainer.stop()
            }
            if(clairServerContainer){
                println "Manually stopping Clair Server container."
                clairServerContainer.stop()
            }
            sh "docker network rm ${clairNetworkName}"
        }
    }
}

def dependencyCheck() {
    stage("Dependency Check") {
        def cliArgs = [
                "--scan ${CHECKOUT_DIRECTORY}/**/*.jar",
                "--format HTML",
                "--format XML",
                "--disableNodeJS",
                "--disableNodeAudit",
                "--disableNodeAuditCache"
        ]

        // Note that we using the installed tool instead of going the Docker container route.
        // This is because, integration to Jenkins would be handled by the plugin.
        dependencycheck additionalArguments: cliArgs.join(" "), odcInstallation: '5.3.2'
        dependencyCheckPublisher pattern: 'dependency-check-report.xml', unstableTotalCritical: 13, unstableTotalHigh: 34, unstableTotalMedium: 119, unstableTotalLow: 10

        archiveArtifacts 'dependency-check-report.html'
    }
}

def dependencyTrack() {
    stage("Dependency Track") {
        docker
            .image('hub.ecquaria.com/maven:3.6.3-jdk-8')
            .inside(getDockerArgs()){
                sh """
                    cd ${CHECKOUT_DIRECTORY}

                    mvn \
                        --no-transfer-progress \
                        -Dmaven.repo.local=${MAVEN_LOCAL_REPO} \
                        -P sg-nexus \
                        -s settings.xml \
                        org.cyclonedx:cyclonedx-maven-plugin:1.6.4:makeAggregateBom
                """

                dependencyTrackPublisher projectId: DEPENDENCY_TRACK_PROJECT,
                    artifact: "${CHECKOUT_DIRECTORY}/target/bom.xml", artifactType: 'bom', synchronous: false
            }
    }
}

def deploySIT() {
    stage("Push SIT Deployment Package to EDS") {
        docker
            .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
            .inside(getDockerArgs()){
                sh """
                    rm -rf "$AUTO_DEPLOYMENT_DIRECTORY"
                    mkdir -p "$AUTO_DEPLOYMENT_DIRECTORY"

                    CICD_HOME_FOLDER="$CHECKOUT_DIRECTORY/cicd" \
                    TAG_NAME="$TAG_TO_BUILD" \
                    BUILD_NUMBER="b$BUILD_NUMBER" \
                    BUILD_HOME="$AUTO_DEPLOYMENT_DIRECTORY/\$TAG_NAME-\$BUILD_NUMBER" \
                    DEFINED_ENVIRONMENTS="SIT_INTERNET SIT_INTRANET UAT_INTERNET UAT_INTRANET PROD_INTERNET PROD_INTRANET" \
                    /scripts/process-deployment-folder.sh
                """

                withCredentials(EDS_CREDENTIALS) {
                    sh """
                        if [[ -d $AUTO_DEPLOYMENT_DIRECTORY/*/deploy/SIT_INTRANET ]]; then

                            (cd $AUTO_DEPLOYMENT_DIRECTORY/*/deploy/SIT_INTRANET; zip -r /tmp/archive-iais-intranet.zip *)

                            EDS_URL="$EDS_URL" \\
                            FILE_TO_UPLOAD=/tmp/archive-iais-intranet.zip \\
                            /scripts/deploy-to-eds.sh
                            
                        fi
                    """

                    sh """
                        if [[ -d $AUTO_DEPLOYMENT_DIRECTORY/*/deploy/SIT_INTERNET ]]; then

                            (cd $AUTO_DEPLOYMENT_DIRECTORY/*/deploy/SIT_INTERNET; zip -r /tmp/archive-iais-internet.zip *)

                            EDS_URL="$EDS_URL" \\
                            FILE_TO_UPLOAD=/tmp/archive-iais-internet.zip \\
                            /scripts/deploy-to-eds.sh
                            
                        fi
                        
                    """
                }
            }
    }
}

def releaseDockerImages(){
    stage('Release Docker Images'){
        //login to build repo to pull the image
        docker.withRegistry("https://${DOCKER_BUILD_REPO}", NEXUS_CREDENTIALS_ID){
            DOCKER_IMAGES_META.each{imgMeta ->
                def buildImage = imgMeta['with-repo-tagged']
                def releaseImage = "${DOCKER_RELEASE_REPO}/${imgMeta['basename-with-tag']}-REL"

                imgMeta.put('with-release-repo-tagged', releaseImage)
                sh """\
                    docker pull ${buildImage}
                    docker tag ${buildImage} ${releaseImage}
                    docker tag ${releaseImage} ${imgMeta['basename-with-tag-for-release']}
                """.stripIndent()

                //login to release repo to push the image
                docker.withRegistry("https://${DOCKER_RELEASE_REPO}", NEXUS_CREDENTIALS_ID){
                    docker.image(imgMeta['basename-with-tag-for-release']).push()
                }
            }
        }
    }
}

def createAndUploadVerificationPackage() {
    stage('Create & Upload Verification Package') {
        createVerificationPackage()
        uploadVerificationPackage()
    }
}

/**
 * Creates the verification package.
 *
 * A verification package contains the hash of the file to be transferred over
 * plus the signature file that would be verfied. The signature file (currently)
 * is created via Jenkins private key.
 */
def createVerificationPackage(){

    sh """
        rm -rf "$TX_BUILD_FOLDER"
        mkdir -p "$PAYLOAD_FOLDER"
    """

    def dockerArgs = [
            "--entrypoint=''",
            "--add-host=\"hub.ecquaria.com:${HUB_IP}\"",
            "-e TZ=Asia/Singapore",
            "-u 0"
    ]
    //Create bundles for each repo
    docker
        .image('hub.ecquaria.com/alpine/git:1.0.7')
        .inside(dockerArgs.join(" ")){
            sh """
                git config --global user.email "mohiais@nowhere.com"
                git config --global user.name "moh-iais"

                cd ${CHECKOUT_DIRECTORY}

                if [[ -z ${BASELINE_TAG} ]]; then 
                    git bundle create iais-egp.bundle ${TAG_TO_BUILD}
                    
                else
                    git bundle create iais-egp.bundle ${BASELINE_TAG}..${TAG_TO_BUILD}
                fi

                mv iais-egp.bundle ${env.WORKSPACE}/${PAYLOAD_FOLDER}
            """
        }

    //TODO Add .m2 delta rules here
    sh """
        cd ${env.WORKSPACE}
        tar -zcf ${PAYLOAD_FOLDER}/m2.tar ${MAVEN_LOCAL_REPO}
    """

    // store TAG_TO_BUILD variable
    sh """
        cd ${env.WORKSPACE}/${PAYLOAD_FOLDER}

        echo "${TAG_TO_BUILD}" > tag-to-build.txt

        cat tag-to-build.txt
    """

    sh """
        # add the "cicd" folder
        tar -cf "$PAYLOAD_FOLDER/auto-deployment.tar" -C "$AUTO_DEPLOYMENT_DIRECTORY" .
    """

    // concatenate all the files
    sh """
        tar -cf "$ARCHIVE_1" -C "$PAYLOAD_FOLDER" .
    """

    docker
        .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
        .inside(getDockerArgs()){

            // hash the concatenated file
            sh """
                md5sum "$ARCHIVE_1" | awk '{ print \$1 }' > "$ARCHIVE_1_HASH"
            """

            // sign the hash
            withCredentials(JENKINS_PRIVATE_KEY_CREDENTIALS) {
                sh """
                    openssl dgst -sign \
                        "$JENKINS_PRIVATE_KEY" \
                        "$SIGN_DIGEST_ALGO" \
                        -out "$SIGNATURE_FROM_JENKINS" \
                        "$ARCHIVE_1_HASH"
                """
            }
        }
}

/**
 * Transfer the verification package to Nexus.
 */
def uploadVerificationPackage(){
    docker
        .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
        .inside(getDockerArgs()){
            // -j == junk directory names (store just file names)
            sh """
                zip -j \
                    "$VERIFICATION_PACKAGE" \
                    "$ARCHIVE_1_HASH" \
                    "$SIGNATURE_FROM_JENKINS"
            """

            withCredentials(NEXUS_CREDENTIALS) {
                sh """
                    curl \
                        --insecure \
                        --user '${NEXUS_USERNAME}:${NEXUS_PASSWORD}' \
                        --upload-file "$VERIFICATION_PACKAGE" \
                        https://hub.ecquaria.com/nexus/repository/moh-iais-ecq-raw-hosted/moh-iais-egp-verification-${BUILD_NUMBER}.zip
                """
            }
        }
}

/**
 * Pipeline will wait for verifier to respond.
 *
 * To continue the pipeline, the verifier should upload a signature file (i.e re-signed the hash file).
 *
 * Pipeline will validate the signature agains the public key of the logged in Jenkins user.
 *
 * Will only proceed if signature checks out successfully.
 */
def waitForVerifier(){
    def stageName = 'Release Gate'
    stage(stageName){
        def CHOICES_NONE = '- Please Select -'
        def CHOICES_YES = 'Approve this build'
        def CHOICES_NO = 'Reject this build'

        def PARAMETER_SIGNATURE = 'Signature-File'
        def PARAMETER_CHOICE = 'Decision'
        def PARAMETER_COMMENTS = 'Comments'

        def signatureDescription = """\
            Download the endorsement package over at https://hub.ecquaria.com/nexus/repository/moh-iais-ecq-raw-hosted/moh-iais-egp-verification-${BUILD_NUMBER}.zip

            Once you've gotten the package, extract and verify the signature against the hash file.

            To continue, you'll need to choose \"${CHOICES_YES}\" from the drop down, counter-sign the hash file with your own private key and upload the signature file using the file input.
            """.stripMargin().stripIndent()

        def choiceDescription = '''\
                To continue this build, choose approve and click on the submit button.

                To reject this build, choose reject and click on the submit button.
            '''.stripMargin().stripIndent()

        def commentDescription = '''\
                Comments submitted here would be recorded (unless "Abort" was used).
            '''.stripMargin().stripIndent()

        while(true){
            _gate = input id: 'UserInput',
                    message: 'Create & Upload Deployment Package?',
                    ok: 'Yes',
                    parameters: [
                            file(description: signatureDescription,
                                    name: PARAMETER_SIGNATURE),
                            choice(description: choiceDescription,
                                    choices: [CHOICES_NONE, CHOICES_YES, CHOICES_NO],
                                    name: PARAMETER_CHOICE),
                            string(description: commentDescription,
                                    defaultValue: '',
                                    name: PARAMETER_COMMENTS,
                                    trim: true)
                    ],
                    submitter: 'admin',
                    submitterParameter: 'approvedByUserId'

            def _choice = _gate[PARAMETER_CHOICE]
            def _comments = _gate[PARAMETER_COMMENTS]
            def _signature = _gate[PARAMETER_SIGNATURE]
            def _approvedByUserId = _gate.approvedByUserId.split("@")[0]

            if(_choice.equals(CHOICES_YES)){
                if(_signature.length() == 0){
                    // ensure that signature file uploaded is not empty
                    echo "*** Empty file was uploaded, please try again. ***"
                    continue
                }

                try{
                    validateSignature(_approvedByUserId, _signature)

                    // copy the 'Signature-File' from the "jobs" folder to our TX_BUILD_FOLDER folder
                    SIGNATURE_FROM_VERIFIER="$TX_BUILD_FOLDER/${_approvedByUserId}.signature"

                    // move the uploaded signature file from "jobs" to "workspace"
                    sh """
                        mv "$_signature" "$SIGNATURE_FROM_VERIFIER"
                    """

                    println "Decision made at ${stageName} stage: ${_choice}"
                    println "Comments given ${stageName} stage: ${_comments}"

                    return true // if program reaches here, validation is successful (i.e because openssl exit code !> 0)
                }
                catch(err){
                    println "*** Unable to validate signature, please try again. (err: $err) ***"
                    continue
                }
            }
            else if(_choice.equals(CHOICES_NO)){
                println "Decision made at ${stageName} stage: ${_choice}"
                println "Comments given ${stageName} stage: ${_comments}"

                return false
            }
            else{
                println 'Please make a decision before clicking on the "Yes" button.'
                continue
            }
        }

        println "Decision made at ${stageName} stage: ${_choice}"
        println "Comments given ${stageName} stage: ${_comments}"

        return false
    }
}

def validateSignature(def _approvedByUserId, def _signature){
    def credA = file(credentialsId: "${_approvedByUserId}.public", variable: 'SIGNERS_PUBLIC_KEY')

    withCredentials([credA]) {
        docker
            .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
            .inside(getDockerArgs()){
                sh """
                    openssl dgst "${SIGN_DIGEST_ALGO}" -verify "${SIGNERS_PUBLIC_KEY}" -signature "${_signature}" "${ARCHIVE_1_HASH}"
                """
            }
    }
}

/**
 * Wrap these 3 into 1 package.
 */
def createAndUploadDeploymentPackage(){
    stage('Create & Upload Deployment Package'){
        createTransferPackage()
        uploadTransferPackage()
        uploadTransferPackageToSFTP()
    }
}

/**
 * Create the transfer package.
 *
 * This package, although readable by all, hides the actual payload behind a symmetric key that only
 * the intended receiver is able to open. Also, no files modified can remain undetected.
 */
def createTransferPackage(){
    def dockerArgs = [
        "--entrypoint=''",
        "-e RANDFILE=/tmp/rnd", // to prevent "unable to write 'random state'" issues.
        "-e TZ=Asia/Singapore"
    ]

    def credA = file(credentialsId: "moh-iais-jenkins-remote.public", variable: 'RECEIVER_PUBLIC_KEY')
    withCredentials([credA]) {
        docker
            .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
            .inside(dockerArgs.join(" ")){
                sh """
                    tar -cf "$ARCHIVE_2" -C "\$(dirname "$ARCHIVE_1")" "\$(basename "$ARCHIVE_1")"
                    tar -rf "$ARCHIVE_2" -C "\$(dirname "$SIGNATURE_FROM_VERIFIER")" "\$(basename "$SIGNATURE_FROM_VERIFIER")"
    
                    openssl rand -hex "$SYMMETRIC_KEY_SIZE" > "$SYMMETRIC_KEY"
                    openssl rand -hex "$INITIALIZATION_VECTOR_SIZE" > "$INITIALIZATION_VECTOR"
    
                    openssl enc -e "$SYMMETRIC_KEY_ALGO" -K "\$(cat $SYMMETRIC_KEY)" -iv "\$(cat $INITIALIZATION_VECTOR)" -in "$ARCHIVE_2" -out "$ARCHIVE_3"
    
                    openssl rsautl -encrypt -inkey "$RECEIVER_PUBLIC_KEY" -pubin -in "$SYMMETRIC_KEY" -out "$SYMMETRIC_KEY_ENCRYPTED"
    
                    tar -cf "$ARCHIVE_4" -C "\$(dirname "$ARCHIVE_3")" "\$(basename "$ARCHIVE_3")"
                    tar -rf "$ARCHIVE_4" -C "\$(dirname "$SYMMETRIC_KEY_ENCRYPTED")" "\$(basename "$SYMMETRIC_KEY_ENCRYPTED")"
                    tar -rf "$ARCHIVE_4" -C "\$(dirname "$INITIALIZATION_VECTOR")" "\$(basename "$INITIALIZATION_VECTOR")"
                """
            }
    }
}

/**
 * Transfer the verification package to Nexus.
 */
def uploadTransferPackage(){
    docker
        .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
        .inside(getDockerArgs()){
            withCredentials(NEXUS_CREDENTIALS) {
                sh """
                    curl \
                    --insecure \
                    --user '${NEXUS_USERNAME}:${NEXUS_PASSWORD}' \
                    --upload-file "$ARCHIVE_4" \
                    https://hub.ecquaria.com/nexus/repository/moh-iais-ecq-raw-hosted/moh-iais-egp-transfer-${BUILD_NUMBER}.tar
                """
            }
        }
}

/**
 * Transfer the verification package to SFTP.
 */
def uploadTransferPackageToSFTP(){
    withCredentials(SFTP_CREDENTIALS) {
        // setup parameters to use when calling docker
        def dockerArgs = [
                "--entrypoint=''",
                "-u 0:0", // prevents ssh from throwing "No user exists for uid xxx"
                "-e TZ=Asia/Singapore"
        ]

        // this is where the file will end up on the remote
        def REMOTE_PATH="/upload/moh-iais-egp-transfer-${BUILD_NUMBER}.tar"

        // sftp will be running in "batch" mode
        def BATCH_FILE_CONTENTS = """\
            put \"$ARCHIVE_4\" \"$REMOTE_PATH\"
        """.stripIndent().stripMargin()

        def BATCH_FILE='sftp-batch-file'
        // TODO Change the address here
        def SFTP_ADDRESS ='192.168.1.119'
        def SFTP_PORT ='4568'

        docker
            .image("hub.ecquaria.com/ecq/ad-tools:$AD_TOOLS_VERSION")
            .inside(dockerArgs.join(" ")){
                sh """
                    echo \"$BATCH_FILE_CONTENTS\" > \"$BATCH_FILE\"
    
                    cat $BATCH_FILE
    
                    cat \"$BATCH_FILE\" | sshpass -e sftp -o StrictHostKeyChecking=no -i \"$SFTP_PRIVATE_KEY\" -P $SFTP_PORT ${SFTP_USER_ID}@${SFTP_ADDRESS}
                """
            }
    }
}

def runAutomatedTests(){
    stage('Run Automated Tests'){
        def dockerArgs

        // do a checkout
        withCredentials(GITLAB_CREDENTIALS) {
            dockerArgs = [
                "--add-host=\"hub.ecquaria.com:${HUB_IP}\"",
                "--entrypoint=''",
                "-e TZ=Asia/Singapore",
            ]

            docker
                .image('hub.ecquaria.com/alpine/git:1.0.7')
                .inside(getDockerArgs()){
                    sh """
                    git clone \
                        --quiet \
                        -c advice.detachedHead=false \
                        -b ${TAG_TO_BUILD} \
                        ${PROJECT_GITLAB_URL_AUTOMATED_TESTING} \
                        ${CHECKOUT_DIRECTORY_AUTOMATED_TESTING}
                    """
                }
        }

        try{
            withCredentials(KATALON_OFFLINE_LICENSE_CREDENTIALS) {
                // invoke katalon
                dockerArgs = [
                    "--entrypoint=''",
                    "-e KATALON_USER_ID=1000",
                    "-e TZ=Asia/Singapore",
                    "-u root",
                    "--add-host=egp.sit.inter.iais.com:192.168.1.229",
                    "--add-host=nas.sit.inter.iais.com:192.168.1.229",
                    "--add-host=egp.sit.intra.iais.com:192.168.0.222"
                ]

                docker
                    .image('hub.ecquaria.com/ecq/katalonstudio/katalon:7.8.2-ffmpeg')
                    .inside(dockerArgs.join(" ")){
                        def browserType = 'Chrome'
                        sh """
                            mkdir -p ~/.katalon/license/ && \
                                ln -s "$KATALON_OFFLINE_LICENSE" ~/.katalon/license/offline.lic || \
                                exit \$?

                            xvfb-run -s '-screen 0 1920x1080x24' \
                            katalonc \
                                -noSplash \
                                -runMode=console \
                                -projectPath="\$(pwd)/$CHECKOUT_DIRECTORY_AUTOMATED_TESTING/moh_iais.prj" \
                                -retry=0 \
                                -testSuitePath="Test Suites/Automation Test Suite - CICD" \
                                -executionProfile="SG_SIT" \
                                -browserType="$browserType" \
                                -remoteWebDriverUrl="http://192.168.0.228:4444/wd/hub" \
                                -remoteWebDriverType="Selenium"
                        """
                    }
            }
        }
        catch(e){
            throw e
        }
        finally {
            sh """
                rm -f katalon-reports.zip
            """
			zip archive: true, dir: 'checkouts-at/Reports', glob: '', zipFile: 'katalon-reports.zip'
		}
    }
}

def doAutomatedTestGate(){
    def stageName = 'Proceed Automation Test?'
    def inputId = 'atGate'
    def message = 'Proceed with automated testing?'
    def yesText = 'Proceed - Environment is ready'
    def noText = 'Reject this build'
    def choiceDescription = '''\
                            To continue with automated testing, choose to proceed and click on the submit button.
                            To reject this build, choose reject and click on the submit button.
                        '''.stripIndent()
    def commentDescription = '''\
                            Comments submitted here would be recorded (unless "Abort" was used).
                        '''.stripIndent()

    doGate(stageName, inputId, message,
        yesText, noText,
        choiceDescription, commentDescription)
}

def doQAGate(){
    def stageName = 'QA Gate'
    def inputId = 'qaGate'
    def message = 'QA Gate'
    def yesText = 'Endorse this build'
    def noText = 'Reject this build'
    def choiceDescription = '''\
                            To continue this build, choose endorse and click on the submit button.
                            To reject this build, choose reject and click on the submit button.
                        '''.stripIndent()
    def commentDescription = '''\
                            Comments submitted here would be recorded (unless "Abort" was used).
                        '''.stripIndent()

    doGate(stageName, inputId, message,
        yesText, noText,
        choiceDescription, commentDescription)
}

/**
 * Pipeline should only proceed if value returned is false.
 *
 * This method will return false only if CHOICES_YES and 'Submit' is used.
 *
 * @param stageName - Specify the name of the stage to be displayed.
 * @param inputId - Specify the input id for the dialog - (for now) only used internally by plugin.
 * @param message - Specify the message that would be displayed.
 */
def doGate(String stageName, String inputId, String message,
            String yesText, String noText, String choiceDescription, String commentDescription){
    def denied = false
    stage(stageName){
        def CHOICES_NONE = '- Please Select -'
        def CHOICES_YES = yesText
        def CHOICES_NO = noText

        def PARAMETER_CHOICE = 'Decision'
        def PARAMETER_COMMENTS = 'Comments'

        def _gate
        def _choice
        def _comments

        def getAnswer = true
        while(getAnswer){
            _gate = input id: inputId,
                    message: message,
                    ok: 'Submit',
                    parameters: [
                            choice(choices: [CHOICES_NONE, CHOICES_YES, CHOICES_NO],
                                    description: choiceDescription,
                                    name: PARAMETER_CHOICE),
                            string(defaultValue: '',
                                    description: commentDescription,
                                    name: PARAMETER_COMMENTS, trim: true)
                    ],
                    submitterParameter: 'submitter'

            // will only continue if 'Abort' was not used.
            _choice = _gate[PARAMETER_CHOICE]
            _comments = _gate[PARAMETER_COMMENTS]

            getAnswer = _choice.equals(CHOICES_NONE)
            if(getAnswer){
                println "Please make a decision before clicking on the submit button."
            }
        }

        println "Decision made at ${stageName} stage: ${_choice}"
        println "Comments given ${stageName} stage: ${_comments}"

        if(_choice.equals(CHOICES_NO)){
            denied = true
        }
    }
    return denied
}
