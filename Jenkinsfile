pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'github_token'
        
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
                sh 'mvn clean package '
            }
        }
