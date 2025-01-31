pipeline {
    agent any
   tools {
        maven 'MyMaven'  // Name of the configured tool
    }
 
    stages {
        stage('Checkout') {
            steps {
               git branch: 'main', url: 'https://github.com/Vinnu12699/wipromart.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install' // Use 'mvn clean install' if using Maven
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test' // Use 'mvn test' if using Maven
            }
        }
        stage('Package') {
            steps {
                bat 'mvn package' // Use 'mvn package' if using Maven
            }
        }       
    }
 
    post {
        success {
            echo 'Build and Deploy succeeded!'
        }
        failure {
            echo 'Build or Deploy failed!'
        }
    }
}