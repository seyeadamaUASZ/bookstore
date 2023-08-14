node("master") {
  stage("Clone the project") {
    git branch: 'main', url: 'https://github.com/seyeadamaUASZ/bookstore.git'
  }

  stage("build") {
    bat "mvn clean install -DskipTests"
  }

  stage("run test") {
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
        bat 'mvn clean compile jib:build'
     }

}