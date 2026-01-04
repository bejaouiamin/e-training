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

        stage('Build Common DTO') {
			steps {
				dir('services/common-dto') {
					bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Services') {
			parallel {
				stage('Discovery') {
					steps {
						dir('services/discovery') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Config Service') {
					steps {
						dir('services/config-service') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Gateway') {
					steps {
						dir('services/gateway') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Cours') {
					steps {
						dir('services/cours') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Candidat') {
					steps {
						dir('services/candidat') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Formateur') {
					steps {
						dir('services/formateur') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Salle') {
					steps {
						dir('services/salle') {
							bat 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
			steps {
				bat 'docker-compose -f docker-compose.infra.yml down'
        		bat 'docker-compose -f docker-compose.infra.yml up -d'
    		}
		}


        stage('Wait for Services') {
			steps {
				echo 'Attente du démarrage des services infrastructure...'
                bat 'timeout /t 30 /nobreak'
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
