node("master") {

 stage("checkout scm"){
    checkout scm
 }

  stage("build & test") {
    bat "mvn clean install"
  }

   stage("package") {
         bat "mvn clean package"
   }

    stage('Scan Quality code'){
         withSonarQubeEnv(installationName:'sonar'){
              bat 'mvn clean install -DskipTests org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.java.binaries=target/classes'
         }
     }

     stage('Docker Build & push') {
             if(env.BRANCH_NAME=='main'){
               bat 'mvn clean compile jib:build'
             }
             if(env.BRANCH_NAME != 'main'){
               echo 'do nothing here'
             }
          }
}