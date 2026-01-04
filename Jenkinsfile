pipeline {
	agent any

    tools {
		jdk 'JDK21'
        maven 'Maven3'
    }

    stages {
		stage('Checkout') {
			steps {
				git branch: 'master',
                    url: 'https://github.com/bejaouiamin/e-training.git'
            }
        }


        stage('Build Services') {
			parallel {
                stage('Gateway') {
					steps {
						dir('services/gateway') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }




    }

    post {
		success {
			echo 'Pipeline exécuté avec succès!'
        }
        failure {
			echo 'Le pipeline a échoué.'
        }
    }
}
