pipeline {
  agent none

  options {
    preserveStashes()
  }
  environment {
    // WORKSPACE=sh(returnStdout: true, script: 'pwd').trim()
    company="amido"
    project="stacks"
    domain="java-jenkins"
    role="frontend"
    // SelfConfig"
    source_branch_ref="${CHANGE_BRANCH ?: BRANCH_NAME}"
    target_branch_ref="${CHANGE_TARGET ?: " "}" // Set to a space as Jenkins doesn't support blank vars
    pull_request_number="${CHANGE_ID ?: " "}" // Set to a space as Jenkins doesn't support blank vars
    self_repo_src="java"
    self_repo_tf_src="deploy/azure/app/kube"
    self_repo_k8s_src="deploy/k8s"
    self_generic_name="stacks-java-jenkins"
    self_pipeline_repo="stacks-pipeline-templates"
    // Maven
    maven_cache_directory="./.m2"
    maven_surefire_repots_dir="./target/surefire-reports"
    // TF STATE CONFIG"
    tf_state_rg="amido-stacks-rg-eun"
    tf_state_storage="amidostackstfstategbl"
    tf_state_container="tfstate"
    // Stacks operates Terraform states based on workspaces **IT IS VERY IMPORTANT** that you ensure a unique name for each application definition"
    // Furthermore **IT IS VERY IMPORTANT** that you change the name of a workspace for each deployment stage"
    // there are some best practices around this if you are going for feature based environments"
    // - we suggest you create a runtime variable that is dynamically set based on a branch currently running"
    // **`terraform_state_workspace="`** "
    // avoid running anything past dev that is not on master"
    // sample value="company-webapp"
    tf_state_key="java-jenkins"
    // Versioning
    version_major="0"
    version_minor="0"
    version_revision="${BUILD_NUMBER}"
    // Docker Config
    docker_dockerfile_path="java/"
    docker_image_name="${self_generic_name}"
    docker_image_tag="${version_major}.${version_minor}.${version_revision}-${GIT_COMMIT}"
    docker_container_registry_name="eu.gcr.io/${gcp_project_id}"
    build_artifact_deploy_name="${self_generic_name}"
    // SonarScanner variables
    static_code_analysis="true"
    sonar_host_url="https://sonarcloud.io"
    sonar_project_name="stacks-java-jenkins"
    sonar_project_key="stacks-java-jenkins"
    // SONAR_TOKEN - Please define this as a Jenkins credential.
    sonar_organisation="amido"
    sonar_command="sonar-scanner"
    sonar_remote_repo="amido/stacks-java"
    sonar_pullrequest_provider="github"
    // AKS/AZURE
    // This will always be predictably named by setting your company - project - INFRAstage - location - compnonent names in the infra-pipeline"
    gcp_region="europe-west2"
    gcp_project_name="amido-stacks"
    gcp_project_id="amido-stacks"
    gcp_cluster_name="${company}-${project}-nonprod-gke-infra"
    // Infra
    base_domain_nonprod="nonprod.amidostacks.com"
    base_domain_prod="prod.amidostacks.com"
  }

  stages {
    stage('CI') {
      stages {
        stage('Checkout Dependencies') {
          agent {
            docker {
              // add additional args if you need to here
              // e.g.:
              // args '-v /var/run/docker.sock:/var/run/docker.sock -u 1000:999'
              // Please check with your admin on any required steps you need to take to ensure a SUDOers access inside the containers
              image "azul/zulu-openjdk-debian:11"
            }
          }

          environment {
            build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
          }

          steps {
            dir("${self_pipeline_repo}") {
              checkout([
                $class: 'GitSCM',
                branches: [[name: 'refs/heads/feature/cycle4']],
                userRemoteConfigs: [[url: "https://github.com/amido/${self_pipeline_repo}"]]
              ])
            }
          }
        }

        stage('Terraform Validation') {
          agent {
            docker {
              // add additional args if you need to here
              image "amidostacks/ci-tf:0.0.4"
            }
          }

          environment {
            build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
          }

          steps {
            dir("${self_repo_tf_src}") {
              sh """#!/bin/bash
                bash ${build_scripts_directory}/test-terraform-fmt-check.bash
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/test-terraform-validate.bash
              """
            }
          }
        }

        stage('Build') {
          agent {
            docker {
              // add additional args if you need to here
              image "azul/zulu-openjdk-debian:11"
            }
          }

          environment {
            build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
          }

          stages {

            stage('Java Build') {

              steps {
                dir("${self_repo_src}") {
                  sh """#!/bin/bash
                    echo "${source_branch_ref}"
                    echo "${target_branch_ref}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/build-maven-install.bash \
                      -Z "${maven_cache_directory}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/build-maven-compile.bash \
                      -Z "${maven_cache_directory}"
                  """

                  // TODO: hardcoded vars to vars.
                  sh """#!/bin/bash
                    rm -rf "${maven_surefire_repots_dir}"

                    bash ${build_scripts_directory}/test-maven-download-test-deps.bash \
                      -X "Unit | Component | Integration | Functional | Performance | Smoke" \
                      -Y "${maven_surefire_repots_dir}" \
                      -Z "${maven_cache_directory}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                      -a "Unit" \
                      -Z "${maven_cache_directory}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                      -a "Component" \
                      -Z "${maven_cache_directory}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                      -a "Integration" \
                      -Z "${maven_cache_directory}"
                  """

                  sh """#!/bin/bash
                    bash ${build_scripts_directory}/test-maven-generate-jacoco-report.bash \
                      -Z "${maven_cache_directory}"
                  """
                }

              }

            } // End of Java Build Stage

            stage('SonarScanner') {
              when {
                expression {
                  "${static_code_analysis}" == "true"
                }
              }

              agent {
                docker {
                  // add additional args if you need to here
                  image "amidostacks/ci-sonarscanner:0.0.1"
                }
              }

              environment {
                build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
              }

                steps {
                  dir("${self_repo_src}") {
                    withCredentials([
                      string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')
                    ]) {
                      sh """#!/bin/bash
                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${target_branch_ref}" == ' ' ]; then
                          TARGET_BRANCH_REF=""
                        else
                          TARGET_BRANCH_REF="${target_branch_ref}"
                        fi

                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${pullrequest_number}" == ' ' ]; then
                          PULL_REQUEST_NUMBER=""
                        else
                          PULL_REQUEST_NUMBER="${pullrequest_number}"
                        fi

                        bash ${build_scripts_directory}/test-sonar-scanner.bash \
                          -a "${sonar_host_url}" \
                          -b "${sonar_project_name}" \
                          -c "${sonar_project_key}" \
                          -d "${SONAR_TOKEN}" \
                          -e "${sonar_organisation}" \
                          -f "${BUILD_NUMBER}" \
                          -g "${source_branch_ref}" \
                          -V "${sonar_command}" \
                          -W "${sonar_remote_repo}" \
                          -X "${sonar_pullrequest_provider}" \
                          -Y "$TARGET_BRANCH_REF" \
                          -Z "$PULL_REQUEST_NUMBER"
                      """
                    }
                  }

                }
            } // End of SonarScanner Stage

          } // End of Build Stages

          post {
            always {
              dir("${self_repo_src}") {
                junit 'target/**/*.xml'

                // See:
                // https://www.jenkins.io/doc/pipeline/steps/jacoco/
                // For Code Coverage gates for Jenkins JaCoCo.
                jacoco(
                  execPattern: 'target/*.exec',
                  classPattern: 'target/classes',
                  sourcePattern: 'src/main/java',
                  exclusionPattern: 'src/test*'
                )
              }
            }
          } // post end

        } // End of Build stage

// vvvvv Ignore

    //     stage('Test') {
    //       // when {
    //       //     branch 'master'
    //       // }
    //       failFast true
    //       parallel {
    //         stage('unit-test') {
    //             steps {
    //               dir("${self_repo_src}") {
    //                 unstash 'node_modules'
    //                 sh '''
    //                   npm run test
    //                 '''
    //               }
    //             }
    //             post {
    //               always {
    //                 junit '**/jest-junit-test-report.xml'
    //               }
    //             }
    //         }
    //         stage('cypress-test') {
    //           when {
    //             expression { "${cypress_e2e_test}" == "true" }
    //           }
    //           environment {
    //             PORT="3000"
    //             APP_BASE_URL="http://localhost"
    //             MENU_API_URL="https://api.demo.nonprod.amidostacks.com/api/menu"
    //             APP_BASE_PATH=""
    //           }
    //           steps {
    //             dir("${self_repo_src}") {
    //               unstash 'node_modules'
    //               sh '''
    //                 npm run test:cypress
    //               '''
    //             }
    //           }
    //         }
    //         stage('sonar-scanner') {
    //           when {
    //             expression {
    //               "${static_code_analysis}" == "true"
    //             }
    //           }
    //           agent {
    //             // We only overwrite defaul CI container runner
    //             docker {
    //               image 'amidostacks/ci-sonarscanner:0.0.1'
    //             }
    //           }
    //           environment {
    //             SONAR_HOST_URL="https://sonarcloud.io"
    //             SONAR_PROJECT_KEY="stacks-webapp-template"
    //             BUILD_NUMBER="${docker_image_tag}"
    //           }
    //           steps {
    //             dir("${self_repo_src}") {
    //               withCredentials([
    //                 string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN'),
    //                 string(credentialsId: 'SONAR_ORGANIZATION', variable: 'SONAR_ORGANIZATION')
    //               ]) {
    //                 unstash 'node_modules'
    //                 sh '''
    //                   sonar-scanner -v
    //                   sonar-scanner
    //                 '''
    //               }
    //             }
    //           }
    //         }
    //       }
    //     }
    //     stage('ArtifactUpload') {
    //       environment {
    //         NODE_ENV="production"
    //       }
    //       steps {
    //         dir("${self_repo_src}") {
    //           withCredentials([
    //             file(credentialsId: 'gcp-key', variable: 'GCP_KEY'),
    //             string(credentialsId: 'next_access_token', variable: 'NEXT_PUBLIC_CONTENTFUL_ACCESS_TOKEN'),
    //             string(credentialsId: 'next_preview_token', variable: 'NEXT_PUBLIC_CONTENTFUL_PREVIEW_ACCESS_TOKEN'),
    //             string(credentialsId: 'next_space_id', variable: 'NEXT_PUBLIC_CONTENTFUL_SPACE_ID'),
    //           ]) {
    //             sh '''
    //               gcloud auth activate-service-account --key-file=${GCP_KEY}
    //               gcloud container clusters get-credentials ${gcp_cluster_name} --region ${gcp_region} --project ${gcp_project_name}
    //               docker-credential-gcr configure-docker
    //               gcloud auth configure-docker "eu.gcr.io" --quiet
    //               docker build --build-arg NEXT_PUBLIC_CONTENTFUL_SPACE_ID=${NEXT_PUBLIC_CONTENTFUL_SPACE_ID} \\
    //               --build-arg NEXT_PUBLIC_CONTENTFUL_ACCESS_TOKEN=${NEXT_PUBLIC_CONTENTFUL_ACCESS_TOKEN} \\
    //               --build-arg NEXT_PUBLIC_CONTENTFUL_PREVIEW_ACCESS_TOKEN=${NEXT_PUBLIC_CONTENTFUL_PREVIEW_ACCESS_TOKEN} \\
    //               --build-arg APP_BASE_PATH="" \\
    //               -t ${docker_container_registry_name}/${docker_image_name}:${docker_image_tag} \\
    //               -t ${docker_container_registry_name}/${docker_image_name}:latest .
    //               docker push ${docker_container_registry_name}/${docker_image_name}
    //             '''
    //           }
    //         }
    //       }
    //     }
      }
    }
  }
}
