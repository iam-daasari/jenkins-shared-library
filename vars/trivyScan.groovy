def fsScan(String serviceDir) {
    dir(serviceDir) {
        sh """
            docker run --rm \
              -v \$(pwd):/workspace \
              -v \$HOME/.m2:/root/.m2 \
              aquasec/trivy:latest fs \
              --scanners vuln \
              --severity HIGH,CRITICAL \
              --format table /workspace
        """
    }
}

def imageScan(String imageName, String imageTag) {
    sh """
        docker run --rm \
          -v /var/run/docker.sock:/var/run/docker.sock \
          aquasec/trivy:latest image \
          --severity HIGH,CRITICAL \
          --format table \
          ${imageName}:${imageTag}
    """
}
EOF
