pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'github_token'
        SONAR_TOKEN = 'sonar_id'
        SONAR_PROJECT_KEY = 'projet_devops'
        SONAR_PROJECT_NAME = 'projet_devops'
        SONAR_HOST_URL = 'http://192.168.50.4:9000'
        //IA test
    }

    stages {
        stage('GIT') {
            steps {
                // Récupérer le code source depuis le dépôt Git
                git branch: 'Emna', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('MAVEN BUILD') {
            steps {
                // Nettoyer et construire le projet sans exécuter les tests
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SONARQUBE') {
            environment {
                SONAR_TOKEN = credentials('sonar_id') // Récupérer le jeton de manière sécurisée
            }
            steps {
                script {
                    // Lancer l'analyse SonarQube
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

        stage('Mockito') {
            steps {
                // Exécuter les tests avec Mockito (inclus dans les tests unitaires Maven)
                sh 'mvn test'
            }
        }
    }

    post {
        always {
            // Nettoyer le workspace après l'exécution du pipeline
            echo 'Nettoyage du workspace...'
            cleanWs()
        }
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Échec du pipeline. Consultez les logs pour plus de détails.'
        }
    }
}





/*pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/1hadil/TpFoyer.git'
        GIT_CREDENTIALS_ID = 'github_token'
        SONAR_TOKEN = 'sonar_id'
                SONAR_PROJECT_KEY = 'projet_devops'
                SONAR_PROJECT_NAME = 'projet_devops'
                SONAR_HOST_URL = 'http://192.168.50.4:9000'
                //DOCKER_HUB_CREDENTIALS_ID = 'dockerhub_credentials'
               //DOCKER_IMAGE_NAME = 'emnaesprit/monimage'
        //test
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

    stage('Build Docker Image') {
                steps {
                    script {
                        sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
                    }
                }
            }

            stage('Push Docker Image') {
                steps {
                    script {
                        withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                        }
                        sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                    }
                }
            }

            stage('docker_compose') {
                steps {
                    script {
                        // Check if containers from docker-compose.yml already exist
                        def mysqlDbExists = sh(script: "docker ps -a --filter 'name=mysql-db' --format '{{.ID}}'", returnStdout: true).trim()
                        def AppExists = sh(script: "docker ps -a --filter 'name=ski-app' --format '{{.ID}}'", returnStdout: true).trim()

                        // Run docker-compose up for the main app if containers are not found
                        if (!mysqlDbExists || !AppExists) {
                            echo 'One or more containers for main app do not exist; starting containers...'
                            sh "docker-compose -f docker-compose-ski.yml up -d"
                        } else {
                            echo 'All main app containers already exist; skipping docker-compose up for main app.'
                        }

                        // Check if containers from docker-compose-monitoring.yml already exist
                        def prometheusExists = sh(script: "docker ps -a --filter 'name=prometheus' --format '{{.ID}}'", returnStdout: true).trim()
                        def grafanaExists = sh(script: "docker ps -a --filter 'name=grafana' --format '{{.ID}}'", returnStdout: true).trim()

                        // Run docker-compose up for monitoring stack if containers are not found
                        if (!prometheusExists || !grafanaExists) {
                            echo 'One or more containers for monitoring stack do not exist; starting containers...'
                            sh "docker-compose -f docker-compose-monitoring.yml up -d"
                        } else {
                            echo 'All monitoring stack containers already exist; skipping docker-compose up for monitoring stack.'
                        }
                    }
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
}*/
