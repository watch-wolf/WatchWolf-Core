#!/bin/bash

# default variables
preclean=0

# parse params
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --preclean) preclean=1 ;;
        
        *) echo "[e] Unknown parameter passed: $1" >&2 ; exit 1 ;;
    esac
    shift
done

# compile latest WW-Core
echo "[v] Compiling WatchWolf Core..."
script_path=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
base_path=$(dirname "$script_path")
local_maven_repos_path="$HOME/.m2"
if [ $preclean -eq 1 ]; then
    docker run -it --rm -v "$base_path":"/compile" -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn clean --file '/compile' # clean project & launch "clean" phase (if any)
fi
docker run -it --rm -v "$base_path":"/compile" -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn compile assembly:single -Dmaven.test.skip=true --file '/compile'

if [ $? -ne 0 ]; then
    echo "[e] Exception while compiling WW-Core"
    exit 1
fi