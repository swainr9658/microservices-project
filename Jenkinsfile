pipeline {
    agent any

    environment {
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Gradle Build') {
            steps {
                echo 'Building all services using Gradle...'
                bat '''
                cd config-server
                gradlew.bat clean build -x test
                cd ..

                cd eureka-server
                gradlew.bat clean build -x test
                cd ..

                cd api-gateway
                gradlew.bat clean build -x test
                cd ..

                cd order-service
                gradlew.bat clean build -x test
                cd ..

                cd payment-service
                gradlew.bat clean build -x test
                cd ..
                '''
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker images...'
                bat 'docker compose build'
            }
        }

        stage('Docker Compose Up') {
            steps {
                echo 'Starting containers using docker-compose...'
                bat 'docker compose up -d'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully'
        }
        failure {
            echo '❌ Pipeline failed'
        }
    }
}