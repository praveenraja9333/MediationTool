pipeline{
  agent any
  parameters {
            string(name: 'gitlabSourceBranch', defaultValue: 'master', description: 'source branch to start the pipeline')
    }
  stages{
    stage('test'){
      steps{
        echo "hello mc-pipe ${params.gitlabSourceBranch}"
      }
    }
  }
}
