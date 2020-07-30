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
    maven_allowed_test_tags="Unit | Component | Integration | Functional | Performance | Smoke"
    // TF STATE CONFIG"
    tf_state_rg="amido-stacks-rg-uks"
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
    container_registry_suffix=".azurecr.io"
    docker_workdir="java/"
    docker_build_additional_args="."
    docker_image_name="${self_generic_name}"
    docker_image_tag="${version_major}.${version_minor}.${version_revision}-${GIT_COMMIT}"
    docker_container_registry_name_nonprod="amidostacksnonprodeuncore"
    k8s_docker_registry_nonprod="${docker_container_registry_name_nonprod}${container_registry_suffix}"
    docker_container_registry_name_prod="amidostacksprodeuncore"
    k8s_docker_registry_prod="${docker_container_registry_name_prod}${container_registry_suffix}"
    docker_tag_latest_nonprod="false"
    docker_tag_latest_promotion="false"
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
    // AZURE_SUBSCRIPTION_ID - Please define this as a Jenkins credential.
    // AZURE_CLIENT_ID - Please define this as a Jenkins credential.
    // AZURE_CLIENT_SECRET - Please define this as a Jenkins credential.
    // AZURE_TENANT_ID - Please define this as a Jenkins credential.
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
                // TODO: move to a tag
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
              sh(
                script: """#!/bin/bash
                  bash ${build_scripts_directory}/test-terraform-fmt-check.bash
                """,
                label: "Terraform: Format Check"
              )

              sh(
                script: """#!/bin/bash
                  bash ${build_scripts_directory}/test-terraform-validate.bash
                """,
                label: "Terraform: Validate Check"
              )
            }
          }
        }

        stage('Build') {
          stages {

            stage('Java Build') {
              agent {
                docker {
                  // add additional args if you need to here
                  image "azul/zulu-openjdk-debian:11"
                }
              }

              environment {
                build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
              }

              steps {
                dir("${self_repo_src}") {

                  sh "echo ${GIT_COMMIT}"
                  sh "echo ${docker_image_tag}; exit 1"

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/build-maven-install.bash \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Install Packages"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/build-maven-compile.bash \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Compile Application"
                  )

                  sh(
                    script: """#!/bin/bash
                      rm -rf "${maven_surefire_repots_dir}"

                      bash ${build_scripts_directory}/test-maven-download-test-deps.bash \
                        -X "${maven_allowed_test_tags}" \
                        -Y "${maven_surefire_repots_dir}" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Download Test Deps"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                        -a "Unit" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Unit tests"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                        -a "Component" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Component tests"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/test-maven-tagged-test-run.bash \
                        -a "Integration" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Integration tests"
                  )

                  sh(
                    script:"""#!/bin/bash
                      bash ${build_scripts_directory}/test-maven-generate-jacoco-report.bash \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Generate Jacoco coverage reports"
                  )
                }

              }

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
                    sh(
                      script: """#!/bin/bash
                        set -exo pipefail

                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${target_branch_ref}" == ' ' ]; then
                          BASH_TARGET_BRANCH_REF=""
                        else
                          BASH_TARGET_BRANCH_REF="${target_branch_ref}"
                        fi

                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${pull_request_number}" == ' ' ]; then
                          BASH_PULL_REQUEST_NUMBER=""
                        else
                          BASH_PULL_REQUEST_NUMBER="${pull_request_number}"
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
                          -Y "\$BASH_TARGET_BRANCH_REF" \
                          -Z "\$BASH_PULL_REQUEST_NUMBER"
                      """,
                      label: "Static Analysis: SonarScanner Run"
                    )
                  }
                }

              }
            } // End of SonarScanner Stage

            stage('Docker') {
              agent {
                docker {
                  // add additional args if you need to here
                  image "amidostacks/ci-k8s:0.0.10"
                }
              }

              environment {
                build_scripts_directory="${WORKSPACE}/${self_pipeline_repo}/scripts"
              }

              steps {
                dir("${docker_workdir}") {

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/build-docker-image.bash \
                        -a "${docker_build_additional_args}" \
                        -b "${parameters.docker_image_name}" \
                        -c "${parameters.docker_image_tag}" \
                        -d "${docker_container_registry_name_nonprod}" \
                        -Z "${container_retistry_suffix}"
                    """,
                    label: "Build Container Image"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash ${build_scripts_directory}/build-docker-image-push.bash \
                        -a "${parameters.docker_image_name}" \
                        -b "${parameters.docker_image_tag}" \
                        -c "${docker_container_registry_name_nonprod}" \
                        -Y "${docker_tag_latest_nonprod}" \
                        -Z "${container_retistry_suffix}"
                    """,
                    label: "Push Container Image to Azure Container Registry"
                  )
                }

              }

            } // End of Docker Stage

          } // End of Build stages

        } // End of Build stage

      } // End of CI stages

    } // End of CI Stage

  } // End of Stages

} // End of Pipeline
