#!/bin/bash

# Variables
export PASSWORD="swarch"
server_id="swarch@xhgrid9"
path="GabrielSuarez-AlejandroVarela-CallBack"
start=40

# Server automatic configuration
# Folder Creation
echo "Create folder in server"
sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $server_id "mkdir $path"

# Sending Jar artifact to server node
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

# Client automatic configuration
echo "Send Jar Client"
IFS=',' read -ra array_client <<< "$1"
for client in "${array_client[@]}"; do
    gradleBuild $client
    client_id="swarch@xhgrid$client"
    sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $client_id "mkdir $path"
    sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./client/build/libs/client.jar $client_id:./$path
done