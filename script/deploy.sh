#!/bin/bash

export PASSWORD="swarch"
server_id="swarch@xhgrid9"
path="GabrielSuarez-AlejandroVarela-CallBack"
start=40

echo "Create folders"
sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $server_id "mkdir $path"

echo "Send Jar Server"
sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ../server/build/libs/server.jar $server_id:./$path

cd ../
function gradleBuild {
    id=$1
    cd ./client/src/main/resources
    newClientId=$((start+id))
    sed -i "s/^Callback.Client.Endpoints=default -h 192.168.131.*/Callback.Client.Endpoints=default -h 192.168.131.$newClientId/" config.client
    cd ../../../../
    gradle build &
    wait
}

echo "Send Jar Client"
IFS=',' read -ra array_client <<< "$1"
for client in "${array_client[@]}"; do
    gradleBuild $client
    client_id="swarch@xhgrid$client"
    sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $client_id "mkdir $path"
    sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./client/build/libs/client.jar $client_id:./$path
done