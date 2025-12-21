pipeline {
    agent any

    environment {
        AWS_REGION = "us-east-1"
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Verify AWS Credentials') {
            steps {
                echo 'Verifying AWS access from Jenkins agent...'
                bat '''
                aws --version
                aws sts get-caller-identity
                '''
            }
        }

        stage('Gradle Build') {
            steps {
                echo 'Building all microservices...'
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
                echo 'Building images and starting containers...'
                bat 'docker compose up -d --build'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}