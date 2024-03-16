#!/bin/bash

# default variables
unit=0

# parse params
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --unit) unit=1 ;;
        
        *) echo "[e] Unknown parameter passed: $1" >&2 ; exit 1 ;;
    esac
    shift
done

unit=1
#if [ $unit -eq 0 ]; then
#    echo "[e] You must specify at least one type of test to run!"
#    exit 1
#fi

# some utils
script_path=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
base_path=$(dirname "$script_path")
local_maven_repos_path="$HOME/.m2"

# clear
docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn clean --file '/compile'

# run the tests
if [ $unit -eq 1 ]; then
    # run unit tests
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn test -DskipTests=false -DskipUTs=false -Dmaven.test.redirectTestOutputToFile=true --file '/compile'
    result=$?

    # Convert xml reports into html
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn surefire-report:report-only --file '/compile'
    docker run -it --rm -v "$base_path":/compile -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn site -DgenerateReports=false --file '/compile'

    if [ $result -ne 0 ]; then
        echo "[e] Unit tests failed"
    fi
fi 

echo "[i] Done"