def call(String serviceDir, String sonarServer = 'SonarQube-Server') {
    dir(serviceDir) {
        sh 'mvn clean validate'
        sh 'mvn compile'
        sh 'mvn test'
        withSonarQubeEnv(sonarServer) {
            sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
        }
    }
}
EOF
