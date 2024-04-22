#!/bin/bash

kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl apply -f k8s/init-namespace/
kubectl apply -f k8s/init-services/
kubectl apply -f k8s/init-persistent-volumes/
kubectl apply -f k8s/

# Logging
kubectl create -f https://download.elastic.co/downloads/eck/2.12.1/crds.yaml
kubectl apply -f https://download.elastic.co/downloads/eck/2.12.1/operator.yaml
#helm install eck-stack elastic/eck-stack \
#    --values https://raw.githubusercontent.com/elastic/cloud-on-k8s/2.12/deploy/eck-stack/examples/logstash/basic-eck.yaml
kubectl apply -f k8s/init-logging/14-filebeat-config.yaml
helm install eck-stack elastic/eck-stack --values ./15-logging-config.yaml
kubectl get secret elasticsearch-es-elastic-user -o=jsonpath='{.data.elastic}' | base64 --decode; echo
kubectl port-forward svc/eck-stack-eck-kibana-kb-http 5601:5601

kubectl get svc
kubectl get pods

# Delete Logging
helm uninstall eck-stack

# Delete everything else
kubectl delete all --all --all-namespaces
