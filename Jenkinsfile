pipeline{
  agent any
  parameters {
            string(name: 'gitlabSourceBranch', defaultValue: 'master', description: 'source branch to start the pipeline')
    }
  stages{
    stage('test'){
      steps{
        setupGit gitUrl: 'git@github.com:praveenraja9333/MediationTool.git' gitBranch: "${params.gitlabSourceBranch}"
        echo "hello mc-pipe ${params.gitlabSourceBranch}"
      }
    }
  }
}
