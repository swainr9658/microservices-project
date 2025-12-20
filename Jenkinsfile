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
                cd api-gateway && gradlew clean build -x test
                cd ../config-server && gradlew clean build -x test
                cd ../eureka-server && gradlew clean build -x test
                cd ../order-service && gradlew clean build -x test
                cd ../payment-service && gradlew clean build -x test
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