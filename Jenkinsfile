pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'github_token'
        SONAR_TOKEN = 'sonar_id'
        SONAR_PROJECT_KEY = 'azizdev'
        SONAR_PROJECT_NAME = 'azizdev'
        SONAR_HOST_URL = 'http://192.168.50.4:9000'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'Aziz', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
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
                sh 'mvn clean package'
            }
        }
        stage('MOCKITO') {
                    steps {
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
    }
}
