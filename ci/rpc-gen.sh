#!/bin/bash

script_path=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
base_path=$(dirname "$script_path")
local_maven_repos_path="$HOME/.m2"
jar_name=$(ls "$base_path/target" | grep '\.jar$')

# clean project & launch "clean" phase (if any)
rm "$base_path/src/main/java/dev/watchwolf/core/rpc/stubs/serversmanager/ServersManagerLocalStub.java" 2>/dev/null
docker run -it --rm -v "$base_path":"/compile" -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn clean --file '/compile'

# compile TODO set "compile scripts" mode when we add that opcion in the pom
docker run -it --rm -v "$base_path":"/compile" -v "$local_maven_repos_path":/root/.m2 maven:3.8.4-openjdk-8 mvn compile assembly:single -Dmaven.test.skip=true --file '/compile'

# launch the rpc generator tool
docker run -i --rm -v "$base_path":"/compile" maven:3.8.4-openjdk-8 /bin/sh <<<"cd /compile && java -cp \"target/$jar_name\" dev/watchwolf/rpc/RPCComponentsCreator"