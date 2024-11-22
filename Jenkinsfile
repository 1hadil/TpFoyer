pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'GitHubToken'
        SONAR_HOST_URL = 'http://192.168.50.4:9000'
        SONAR_TOKEN = 'sonar_id'
        SONAR_PROJECT_KEY = 'projet_devops'
        SONAR_PROJECT_NAME = 'projet_devops'
        
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'OumaimaBenSaad-5infini2', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('MAVEN BUILD') {
            steps {
                script {
                    if (fileExists('target')) {
                        echo 'Nettoyage du répertoire target...'
                        sh 'rm -rf target'
                    }
                }
                sh 'mvn clean package '
            }
        }



        stage('SONARQUBE') {
            environment {
                SONAR_TOKEN = credentials('sonar_id')
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
                sh 'mvn jacoco:prepare-agent test jacoco:report'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
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
