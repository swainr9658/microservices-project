pipeline {
    agent any

    environment {
        AWS_REGION = "us-east-1"
    }

    stages {

        stage('Stop Existing Containers') {
            steps {
                echo 'Stopping old containers (if any)...'
                bat 'docker compose -p microservices-pipeline down || exit 0'
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify AWS Credentials') {
            steps {
                bat 'aws sts get-caller-identity'
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
                bat 'docker compose -p microservices-pipeline up -d --build'
            }
        }
    }
}