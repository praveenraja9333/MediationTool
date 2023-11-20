pipeline{
  agent any
  parameters {
            string(name: 'gitlabSourceBranch', defaultValue: 'master', description: 'source branch to start the pipeline')
    booleanParam(name: 'CREATE_RELEASE', defaultValue: false, description: 'Whether to create a release build, incrementing the version.  Default to not')
    }
  stages{
    stage('test'){
      when {
        $(params.CREATE_RELEASE)
      }
      steps{
        //setupGit gitUrl: 'git@github.com:praveenraja9333/MediationTool.git' gitBranch: "${params.gitlabSourceBranch}"
        echo "hello mc-pipe ${params.gitlabSourceBranch}"
      }
    }
  }
}
