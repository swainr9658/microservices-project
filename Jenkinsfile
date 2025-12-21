pipeline {
    agent any

    environment {
        AWS_REGION = "us-east-1"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Gradle Build') {
            steps {
                bat '''
                cd api-gateway && gradlew clean build -x test
                cd ../config-server && gradlew clean build -x test
                cd ../eureka-server && gradlew clean build -x test
                cd ../order-service && gradlew clean build -x test
                cd ../payment-service && gradlew clean build -x test
                '''
            }
        }

        stage('Docker Compose Up') {
            steps {
                bat 'docker compose up -d --build'
            }
        }
    }
}