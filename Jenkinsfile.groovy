#!/usr/bin/env groovy
// Define variables
//List locust_list = ["\"enduser_login.py\"","\"agent_login.py\"","\"a.py\""]
List locust_list = ["\"demo\"",
                    "\"admin_dashboard_watch\"",
                    "\"agent_login\"",
                    "\"agent_transfer\"",
                    "\"end_user_get_menus\"",
                    "\"end_user_login\"",
                    "\"public_get_agent_activity_logs\"",
                    "\"public_get_calls\"",
                    "\"public_get_chats\""]
String locustFiles = buildScript(locust_list)

String userCountScript(){
  return '''
  String locustFiles = "${LOCUST_FILE}";

  html =
"""
<!DOCTYPE html>
<div>User count for below scripts</div>
"""

  def times = locustFiles.count(",") as int;
  def files = locustFiles.split(",");
  int i=0;
  for(i =0;i<=times;i++){
    html = html +
"""
<div>${files[i]}</div>
<input name="value" class="setting-input" type="text">
"""
  }

  return html
'''
}

// Methods to build groovy scripts to populate data
String buildScript(List values){
  return "return $values"
}

// WORKING SOLUTION
// Properties step to set the Active choice parameters via
// Declarative Scripting
//properties([
//        parameters([
//                [$class: 'ChoiceParameter', choiceType: 'PT_CHECKBOX', filterable: true, name: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["ERROR"]'], script: [classpath: [], sandbox: false, script:  locustFiles]]],
//                [$class: 'DynamicReferenceParameter', choiceType: 'ET_FORMATTED_HTML',name: 'USER_COUNT', referencedParameters: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["error"]'], script: [classpath: [], sandbox: false, script: userCountScript()]]]
//        ])
//])
//pipeline {
//  agent any
//  stages {
//    stage('Build'){
//      steps {
//        sh '''
//          echo "Building.."
//          echo "Locust files: $LOCUST_FILE"
//          echo "User count: $USER_COUNT"
//          python ./parse_vars.py
//        '''
//      }
//    }
//  }
//}

properties([
        parameters([
                [$class: 'ChoiceParameter', choiceType: 'PT_CHECKBOX', filterable: true, name: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["ERROR"]'], script: [classpath: [], sandbox: false, script:  locustFiles]]],
                [$class: 'DynamicReferenceParameter', choiceType: 'ET_FORMATTED_HTML',name: 'USER_COUNT', referencedParameters: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["error"]'], script: [classpath: [], sandbox: false, script: userCountScript()]]],
                string(name: 'IMAGE_TAG', defaultValue: 'latest', description: 'Which tag would you like to run?')
        ])
])
pipeline {
  agent any
  stages {
    stage('Build'){
      steps {
        sh '''
          echo "Building.."
          echo "Locust files: $LOCUST_FILE"
          echo "User count: $USER_COUNT"
          echo "IMAGE_TAG: $IMAGE_TAG"
          python ./parse_vars.py
        '''
      }
    }
  }
}

//pipeline {
//  agent any
//
//  environment {
//    TEST_TYPE = 'Locust'
//    QUEUE_URL = 'internal-automation-controller-1411232222.us-west-2.elb.amazonaws.com'
//    RP_ENDPOINT = 'http://report-portal-alb-pub-1494733986.us-west-2.elb.amazonaws.com:8080/api/v1'
//    RP_UUID = 'f94e965e-7e12-4973-ae44-4d242807fc8c'
//    LOCUST_URL = 'http://a43238a2173eb11ea92f90264cacffe3-1349624597.us-west-2.elb.amazonaws.com:8089'
//    CLUSTER_NAME = 'automation'
//  }
//
//  parameters {
//
//
//    choice(name: 'LOCUST_FILE', choices: [
//            'UJET_21490',
//            'demo',
//            'agent_login',
//            'agent_transfer',
//            'admin_dashboard_watch',
//            'end_user_login',
//            'end_user_get_menus',
//            'public_get_calls',
//            'public_get_chats',
//            'public_get_agent_activity_logs'
//    ], description: 'Scripts to run')
//
//    // ---------------------------------- MISCELLANEOUS -----------------------------------------
//    choice(name: 'DRY_RUN', choices: ['No', 'Yes'], description: "Is this a dry-run to refresh params?")
//
//    string(name: 'UJET_ENG_NAME', defaultValue: 'Locust_User', description: 'Sets who ran the job in Report Portal')
//
//    choice(name: 'LOG_LEVEL', choices: ['ERROR', 'INFO', 'DEBUG'], description: "How much log do you want to see?")
//  }
//
//  stages {
//    stage('Wait for Lock') {
//      options {
//        lock(resource: "Locust", quantity: 1)
//      }
//
//
//      stages {
//        stage('Checkout') {
//          steps {
//            checkout scm
//          }
//        }
//
//        stage("Parameterizing") {
//          steps {
//            script {
//              if ("${params.DRY_RUN}" == "Yes") {
//                currentBuild.result = 'ABORTED'
//                error('DRY RUN COMPLETED. JOB PARAMETERIZED.')
//              }
//            }
//          }
//        }
//
//        stage('Setup') {
//          steps {
//            sh '''
//                        ./_ci/set_env.sh
//                        AGENT_COUNT=$USERS_COUNT ADMIN_COUNT=$USERS_COUNT MANAGER_COUNT=$USERS_COUNT ./bin/aws_util.sh upload_users
//                    '''
//          }
//        }
//
//        stage('Deploy') {
//          steps {
//            script {
//              def credentialId = "kubeconfig-${CLUSTER_NAME}"
//              withKubeConfig(credentialsId: "${credentialId}") {
//                sh '_ci/deploy.sh'
//              }
//            }
//          }
//        }
//
//        stage('Swarm') {
//          steps {
//            sh '''
//                        ./bin/locust/swarm.sh
//                    '''
//          }
//        }
//
//        stage('Run tests') {
//          steps {
//            sh '''
//                        set +x
//
//                        echo "You can check Locust dashboard from $LOCUST_URL. \n Running tests.."
//                        sleep $((${DURATION} * 60))
//                    '''
//          }
//        }
//
//      }
//
//      post {
//        always {
//          sh '''#!/bin/bash
//                        ./bin/locust/stop.sh
//                        curl -f -X DELETE ${QUEUE_URL}/qa_automation_controller/queue_groups/${UJET_ENG_NAME}_${TEST_COMPANY}_${TEST_ENV}
//                    '''
//        }
//
//        success {
//          echo 'Build succeeded!'
//        }
//        failure {
//          echo 'Build failed!'
//        }
//      }
//    }
//  }
//}