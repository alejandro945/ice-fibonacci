#!/bin/bash

export PASSWORD="swarch"
server_id="swarch@xhgrid2"
path="GabrielSuarez-AlejandroVarela-CallBack"


echo "Create folders"
sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $server_id "mkdir $path"

echo "Ask for client nodes"
IFS=',' read -ra array_client <<< "$1"

echo "Send scripts"
sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./deployClient.sh $server_id:./$path
sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./deployServer.sh $server_id:./$path

echo "Send Jar Server"
sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./server/build/libs/server.jar $server_id:./$path

echo "Send Jar Client"
for client in "${array_client[@]}"; do
    client_id="swarch@xhgrid$client"
    sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $client_id "mkdir $path"
    sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ./client/build/libs/client.jar $client_id:./$path
done

echo "Deploy Server"
sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $server_id "cd $path && ./deployServer.sh"

echo "Deploy Clients"
sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $server_id "cd $path && ./deployClient.sh $1"
