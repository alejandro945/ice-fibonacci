#!/bin/bash

export PASSWORD="swarch"
server_id="swarch@xhgrid9"
path="GabrielSuarez-AlejandroVarela-CallBack"
start=40


IFS=',' read -ra array_client <<< "$1"
for client in "${array_client[@]}"; do
    client_id="swarch@xhgrid$client"
    sshpass -p ${PASSWORD} ssh -o StrictHostKeyChecking=no $client_id "mkdir $path"
    sshpass -p ${PASSWORD} scp -o StrictHostKeyChecking=no ../client/build/libs/client.jar $client_id:./$path
done