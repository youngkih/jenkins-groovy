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

// Properties step to set the Active choice parameters via
// Declarative Scripting
properties([
        parameters([
                [$class: 'ChoiceParameter', choiceType: 'PT_CHECKBOX', filterable: true, name: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["ERROR"]'], script: [classpath: [], sandbox: false, script:  locustFiles]]],
                [$class: 'DynamicReferenceParameter', choiceType: 'ET_FORMATTED_HTML',name: 'USER_COUNT', referencedParameters: 'LOCUST_FILE', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return ["error"]'], script: [classpath: [], sandbox: false, script: userCountScript()]]]
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
          python ./parse_vars.py
        '''
      }
    }
  }
}