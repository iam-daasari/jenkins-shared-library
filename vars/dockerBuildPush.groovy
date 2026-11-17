def call(Map config) {
    dir(config.serviceDir) {
        sh "docker build -t ${config.imageName}:${config.imageTag} ."
    }

    withCredentials([
        string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {
        sh """
            aws ecr get-login-password --region ${config.awsRegion} | docker login --username AWS --password-stdin ${config.ecrUrl}
            docker tag ${config.imageName}:${config.imageTag} ${config.ecrUrl}/${config.imageName}:${config.imageTag}
            docker tag ${config.imageName}:${config.imageTag} ${config.ecrUrl}/${config.imageName}:latest
            docker push ${config.ecrUrl}/${config.imageName}:${config.imageTag}
            docker push ${config.ecrUrl}/${config.imageName}:latest
            docker rmi ${config.imageName}:${config.imageTag} || true
            docker rmi ${config.ecrUrl}/${config.imageName}:${config.imageTag} || true
            docker rmi ${config.ecrUrl}/${config.imageName}:latest || true
        """
    }
}
EOF
