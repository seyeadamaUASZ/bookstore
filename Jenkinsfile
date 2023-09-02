node("master") {

 stage("checkout scm"){
    checkout scm
 }

  stage("build") {
    bat "mvn clean install -DskipTests"
  }

  stage("test"){
  bat "mvn test"
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
        if(env.branch=='main'){
            steps {
               bat 'mvn clean compile jib:build'
             }
        }
     }

}