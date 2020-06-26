pipeline {
    agent any

    stages {
        stage('Remove deployment') {
            steps {
                sh 'cp /home/pi/librin/apilibrin.jar /home/pi/librin/backups/ || true'
                sh 'sh /home/pi/librin/apilibrin-stop.sh &'
                sh 'rm -f /home/pi/librin/apilibrin.jar || true'
            }
        }

        stage('Build backend') {
            steps {
                sh 'mvn clean install -Pstage'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Deploy backend') {
            steps {
                sh 'cp ./target/apilibrin.jar /home/pi/librin'
                withEnv(['JENKINS_NODE_COOKIE=do_not_kill']) {
                    sh 'sh /home/pi/librin/apilibrin-start.sh'
                }
            }
        }
		
    }
}
