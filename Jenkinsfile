pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "saferealtor" // 사용할 Docker 이미지 이름
        CONTAINER_NAME = "saferealtor-container" // Docker 컨테이너 이름
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Git 리포지토리에서 소스 코드 클론
                git branch: 'main', url: 'https://github.com/syugi/safeRealtor.git'
            }
        }
        stage('Build Gradle') {
            steps {
                // Gradle 빌드
                sh './gradlew clean build'
            }
        }
        stage('Build Docker Image') {
            steps {
                // Docker 이미지 빌드
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }
        stage('Stop & Remove Old Container') {
            steps {
                // 기존 컨테이너 중지 및 삭제
                script {
                    sh """
                    if [ \$(docker ps -q -f name=${CONTAINER_NAME}) ]; then
                        docker stop ${CONTAINER_NAME}
                        docker rm ${CONTAINER_NAME}
                    fi
                    """
                }
            }
        }
        stage('Run New Container') {
            steps {
                // 새로운 Docker 컨테이너 실행
                sh 'docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${DOCKER_IMAGE}'
            }
        }
    }
}

