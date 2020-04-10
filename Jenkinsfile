pipeline {
    agent any

    stages {
        stage('Remove deployment') {
            steps {
                sh 'cp /home/pi/nikowiscom/ksiazkofilia/apiksiazkofilia.jar /home/pi/nikowiscom/ksiazkofilia/backups/ || true'
                sh 'sh /home/pi/nikowiscom/ksiazkofilia/apiksiazkofilia-stop.sh &'
                sh 'rm -f /home/pi/nikowiscom/ksiazkofilia/apiksiazkofilia.jar || true'
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
                sh 'cp ./target/apiksiazkofilia.jar /home/pi/nikowiscom/ksiazkofilia'
                withEnv(['JENKINS_NODE_COOKIE=do_not_kill']) {
                    sh 'sh /home/pi/nikowiscom/ksiazkofilia/apiksiazkofilia-start.sh'
                }
            }
        }
		
    }
}
