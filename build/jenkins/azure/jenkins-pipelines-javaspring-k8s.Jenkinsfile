pipeline {
  agent none

  // parameters {
  // }

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
    self_repo_src="java"
    self_repo_tf_src="deploy/azure/app/kube"
    self_repo_k8s_src="deploy/k8s"
    self_generic_name="stacks-java-jenkins"
    self_pipeline_repo="stacks-pipeline-templates"
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
    // AKS/AZURE
    // This will always be predictably named by setting your company - project - INFRAstage - location - compnonent names in the infra-pipeline"
    gcp_region="europe-west2"
    gcp_project_name="amido-stacks"
    gcp_project_id="amido-stacks"
    gcp_cluster_name="${company}-${project}-nonprod-gke-infra"
    // Infra
    base_domain_nonprod="nonprod.amidostacks.com"
    base_domain_prod="prod.amidostacks.com"
    // GLOBAL GCP vars
    CLOUDSDK_COMPUTE_REGION="${gcp_region}"
    CLOUDSDK_CONTAINER_CLUSTER="${company}-${project}-nonprod-gke-infra"
    CLOUDSDK_CORE_PROJECT="${gcp_project_id}"
    CLOUDSDK_CORE_DISABLE_PROMPTS="True"
  }

  stages {
    stage('CI') {
      stages {
        // stage('Set `WORKSPACE` var') {
        //   agent {

        //   }

        //   steps {

        //   }
        // }

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
                bash ${build_scripts_directory}/build-terraform-fmt-check.bash
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-terraform-validate.bash
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

          steps {
            dir("${self_repo_src}") {
              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-install.bash
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-compile.bash
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-download-test-deps.bash
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-tagged-test-run.bash "Unit"
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-tagged-test-run.bash "Component"
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-tagged-test-run.bash "Integration"
              """

              sh """#!/bin/bash
                bash ${build_scripts_directory}/build-maven-generate-jacoco-report.bash
              """

              // Sonar Cloud Goes 'ere.
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
          }
        }
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
