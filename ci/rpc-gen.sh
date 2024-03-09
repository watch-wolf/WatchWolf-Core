#!/bin/bash

script_path=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
base_path=$(dirname "$script_path")
jar_name=$(ls "$base_path/target" | grep '\.jar$')

docker run -i --rm -v "$base_path":"/compile" maven:3.8.4-openjdk-8 /bin/sh <<<"cd /compile && java -cp \"target/$jar_name\" dev/watchwolf/rpc/RPCComponentsCreator"