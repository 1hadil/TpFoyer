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
        stage('GIT') {
            steps {
                git branch: 'Emna', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
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
         stage('MOCKITO') {
                    steps {
                        sh 'mvn test'
                    }
                }
	    stage('Upload to Nexus') {
            steps {
                script {
                    def nexusUrl = "http://192.168.50.4:8081/repository/"
                    def artifactId = "tp-foyer"
                    def version = "5.0.0"
                    def packaging = "jar"
                    def nexusUser = "admin"
                    def nexusPassword = "Nexus"
                    def repository = "maven-releases"

                    sh """
                    mvn deploy:deploy-file \
                        -DgroupId=tn.esprit \
                        -DartifactId=${artifactId} \
                        -Dversion=${version} \
                        -Dpackaging=${packaging} \
                        -Dfile=target/${artifactId}-${version}.${packaging} \
                        -DrepositoryId=deploymentRepo \
                        -Durl=${nexusUrl}${repository}/ \
                        -DpomFile=pom.xml \
                        -Dusername=${nexusUser} \
                        -Dpassword=${nexusPassword}
                    """
                }
            }
        }
        stage('DOCKER IMAGE') {
            steps {
                sh 'docker build -t emnaesprit/emna .'
            }
        }
        stage('Docker Hub') {
            steps {
                script {
                    echo 'Logging in to Docker Hub...'
                }
                sh 'docker login -u emnaesprit -p 120220emna'
                sh 'docker push emnaesprit/emna'
            }
        }
	    
        stage('Docker-Compose') {
            steps {
                sh 'docker ps'
                sh 'docker compose logs'
		sh 'docker compose up -d'
	        sh 'docker compose -f docker-compose.yml up -d mysql'
		
            }
        }
	    stage('GRAFANA') {
    steps {
        script {
            // Vérification de l'existence des conteneurs Prometheus et Grafana
            def prometheusExists = sh(script: "docker ps -a --filter 'name=prometheus' --format '{{.ID}}'", returnStdout: true).trim()
            def grafanaExists = sh(script: "docker ps -a --filter 'name=grafana' --format '{{.ID}}'", returnStdout: true).trim()

            // Exécution de docker-compose pour démarrer les conteneurs si nécessaire
            if (!prometheusExists || !grafanaExists) {
                echo 'Un ou plusieurs conteneurs pour Grafana et Prometheus sont manquants; démarrage des conteneurs...'
                sh "docker compose -f docker-compose-monotoring.yml up -d"
            } else {
                echo 'Tous les conteneurs pour Grafana et Prometheus existent déjà; aucune action nécessaire.'
            }

            // Affichage des logs pour vérifier si tout s'est bien passé
            sh "docker compose -f docker-compose-monotoring.yml logs"
        }
    }
}

	    
    stage('MAIL') {
            steps {
                script {
                    if (currentBuild.currentResult == 'SUCCESS') {
                        echo 'Le pipeline a réussi !'
                        emailext(
                            to: 'emnamahfoudhi02@gmail.com',
                            subject: "✅ Succès : Pipeline ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                            body: """
                                <html>
                                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                    <h2 style="color: #28a745;">✔️ Le pipeline a réussi !</h2>
                                    <p>Le job <strong>${env.JOB_NAME}</strong> s'est terminé avec succès.</p>
                                    <table style="border-collapse: collapse; width: 100%; margin-top: 10px;">
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Job Name</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;">${env.JOB_NAME}</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Number</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;">${env.BUILD_NUMBER}</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Status</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px; color: #28a745;">Succès</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Voir les détails</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><a href="${env.BUILD_URL}" style="color: #007bff;">Consultez les détails du build</a></td>
                                        </tr>
                                    </table>
                                    <p>Cordialement,<br>L'équipe DevOps</p>
                                </body>
                                </html>
                            """,
                            mimeType: 'text/html',
                            attachLog: true
                        )
                    } else {
                        echo 'Le pipeline a échoué.'
                        emailext(
                            to: 'emnamahfoudhi02@gmail.com',
                            subject: "❌ Échec : Pipeline ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                            body: """
                                <html>
                                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                    <h2 style="color: #dc3545;">❌ Le pipeline a échoué</h2>
                                    <p>Le job <strong>${env.JOB_NAME}</strong> n'a pas réussi à se terminer correctement.</p>
                                    <table style="border-collapse: collapse; width: 100%; margin-top: 10px;">
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Job Name</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;">${env.JOB_NAME}</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Number</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;">${env.BUILD_NUMBER}</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Build Status</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px; color: #dc3545;">Échec</td>
                                        </tr>
                                        <tr>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><strong>Voir les détails</strong></td>
                                            <td style="border: 1px solid #ddd; padding: 8px;"><a href="${env.BUILD_URL}" style="color: #007bff;">Consultez les détails du build</a></td>
                                        </tr>
                                    </table>
                                    <p>Cordialement,<br>L'équipe DevOps</p>
                                </body>
                                </html>
                            """,
                            mimeType: 'text/html',
                            attachLog: true
                        )
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            echo 'Pipeline terminé.'
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
