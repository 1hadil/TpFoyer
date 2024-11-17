pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'github_token'
        SONAR_TOKEN = 'sonar_id'
                SONAR_PROJECT_KEY = 'projet_devops'
                SONAR_PROJECT_NAME = 'projet_devops'
                SONAR_HOST_URL = 'http://192.168.50.4:9000'
        
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Emna', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('Clean') {
            steps {
                script {
                    if (fileExists('target')) {
                        echo 'Cleaning target directory...'
                        sh 'rm -rf target'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

       stage('Test') {
                   steps {
                       // Exécuter les tests unitaires
                       sh 'mvn test'
                   }
               }
               stage('SonarQube Analysis') {
                           environment {
                               SONAR_TOKEN = credentials('sonar_id') // Fetch securely from Jenkins credentials
                           }
                           steps {
                               script {
                                   sh """
                                       mvn clean verify sonar:sonar \
                                           -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                           -Dsonar.projectName='${SONAR_PROJECT_NAME}' \
                                           -Dsonar.host.url=${SONAR_HOST_URL} \
                                           -Dsonar.login=${SONAR_TOKEN} \
                                           -Dsonar.sources=src/main/java \
                                           -Dsonar.tests=src/test/java \
                                           -Dsonar.java.binaries=target/classes
                                   """
                               }
                           }
                       }
                        stage('JaCoCo Report') {
                                   steps {
                                       sh 'mvn jacoco:prepare-agent test jacoco:report'
                                   }
                               }

        

      

        stage('Archive Artifacts') {
            steps {
                // Archive the jar files created during the build
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        // Optional: Handle build outcomes
        always {
            echo 'Cleaning up workspace...'
            cleanWs() // Clean workspace after build
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed. Check the logs for details.'
        }
    }
}
