#!/bin/bash

# default variables
unit=0
integration=0
test_match=""

# parse params
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --unit) unit=1 ;;
        --integration) integration=1 ;;
        --tests) test_match="$2" ; shift ;;
        
        *) echo "[e] Unknown parameter passed: $1" >&2 ; exit 1 ;;
    esac
    shift
done

if [ $integration -eq 0 ] && [ $unit -eq 0 ]; then
    echo "[e] You must specify at least one type of test to run!"
    exit 1
fi

# util variables
script_path=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
base_path=$(dirname "$script_path")
local_maven_repos_path="$HOME/.m2"

# clear
docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn clean --file '/compile'

# run the tests
if [ $unit -eq 1 ]; then
    unit_tests_report_path="$base_path/target/surefire-reports"
    mkdir -p "$unit_tests_report_path"

    # run unit tests
    if [ ! -z "$test_match" ]; then
        echo "[v] Running filtered tests: $test_match"
        docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8               \
                        mvn test -DskipTests=false -DskipUTs=false -DskipITs=true                                               \
                        -Dmaven.test.redirectTestOutputToFile=true -Dtest="$test_match" -X --file '/compile'                    \
                2>&1 | tee "$unit_tests_report_path/docker-log.txt" # forward to file
        result=$?
    else
        docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8               \
                        mvn test -DskipTests=false -DskipUTs=false -DskipITs=true                                               \
                        -Dmaven.test.redirectTestOutputToFile=true -X --file '/compile'                                         \
                2>&1 | tee "$unit_tests_report_path/docker-log.txt" # forward to file
        result=$?
    fi

    # Convert xml reports into html
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.3-openjdk-17 mvn surefire-report:report-only --file '/compile'
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.3-openjdk-17 mvn site -DgenerateReports=false --file '/compile'

    if [ $result -ne 0 ]; then
        echo "[e] Unit tests failed"
    fi
fi 

if [ $integration -eq 1 ]; then
    integration_tests_report_path="$base_path/target/failsafe-reports"
    mkdir -p "$integration_tests_report_path"

    # run integration tests
    if [ ! -z "$test_match" ]; then
        echo "[v] Running filtered tests: $test_match"
        docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8                               \
                                mvn test failsafe:integration-test failsafe:verify                                                              \
                                -P integration-test -Dmaven.test.redirectTestOutputToFile=true -D it.test="$test_match" -X --file '/compile'    \
                2>&1 | tee "$integration_tests_report_path/docker-log.txt" # forward to file
        result=$?
    else
        docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8                               \
                                mvn test failsafe:integration-test failsafe:verify                                                              \
                                -P integration-test -Dmaven.test.redirectTestOutputToFile=true -X --file '/compile'                             \
                2>&1 | tee "$integration_tests_report_path/docker-log.txt" # forward to file
        result=$?
    fi

    # Convert xml reports into html
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.3-openjdk-17 mvn surefire-report:failsafe-report-only --file '/compile'
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.3-openjdk-17 mvn site -DgenerateReports=false --file '/compile'

    if [ $result -ne 0 ]; then
        echo "[e] Integration tests failed"
    fi
fi

echo "[i] Done"