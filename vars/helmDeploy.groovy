def call(Map config) {
    withCredentials([
        string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
        string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')
    ]) {
        sh """
            export AWS_DEFAULT_REGION=${config.awsRegion}
            aws eks update-kubeconfig --region ${config.awsRegion} --name ${config.clusterName}
            helm upgrade --install ${config.releaseName} ${config.chartPath} \
              --namespace ${config.namespace} \
              --create-namespace \
              --set imageTag=${config.imageTag} \
              --atomic \
              --timeout 5m
            helm status ${config.releaseName} -n ${config.namespace}
        """
    }
}
