pipeline {
    agent any

    stages {
        stage('Remove deployment') {
            steps {
                sh 'cp /home/pi/nikowiscom/librin/apilibrin.jar /home/pi/nikowiscom/librin/backups/ || true'
                sh 'sh /home/pi/nikowiscom/librin/apilibrin-stop.sh &'
                sh 'rm -f /home/pi/nikowiscom/librin/apilibrin.jar || true'
            }
        }

        stage('Build backend') {
            steps {
                sh 'mvn clean install -Pprod'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Deploy backend') {
            steps {
                sh 'cp ./target/apilibrin.jar /home/pi/nikowiscom/librin'
                withEnv(['JENKINS_NODE_COOKIE=do_not_kill']) {
                    sh 'sh /home/pi/nikowiscom/librin/apilibrin-start.sh'
                }
            }
        }
		
    }
}
