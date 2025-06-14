#!/usr/bin/env bash

clear
./gradlew clean assemble
docker build -t token-bucket .
docker service create --name token-bucket --replicas 3 --publish published=8080,target=8080 token-bucket:latest
docker ps