pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Build maven') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Diego9028/Ev1']])
                dir("kartingrm-backend"){
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                dir("kartingrm-backend"){
                    script {
                        withDockerRegistry(credentialsId: "docker-credentials"){
                            bat 'docker build -t kartingrm-backend .'
                        }
                        bat 'docker build -t kartingrm-backend .'
                        bat 'docker push diego9028/kartingrm-backend'
                    }
                }
            }
        }
    }
}
