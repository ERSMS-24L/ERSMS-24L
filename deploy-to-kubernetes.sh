#!/bin/bash

kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl apply -f k8s/init-namespace/
kubectl apply -f k8s/init-services/
kubectl apply -f k8s/init-persistent-volumes/
kubectl apply -f k8s/

kubectl get svc -n ersms-forum
kubectl get pods -n ersms-forum

# Logging
helm repo add elastic https://helm.elastic.co
helm repo update
kubectl create -f https://download.elastic.co/downloads/eck/2.12.1/crds.yaml
kubectl apply -f https://download.elastic.co/downloads/eck/2.12.1/operator.yaml
#helm install eck-stack elastic/eck-stack \
#    --values https://raw.githubusercontent.com/elastic/cloud-on-k8s/2.12/deploy/eck-stack/examples/logstash/basic-eck.yaml
kubectl apply -f k8s/init-logging/14-filebeat-config.yaml
helm install eck-stack elastic/eck-stack --values k8s/init-logging/15-logging-config.yaml
kubectl get secret elasticsearch-es-elastic-user -o=jsonpath='{.data.elastic}' | base64 --decode; echo
kubectl port-forward svc/eck-stack-eck-kibana-kb-http 5601:5601

# Monitoring
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm upgrade --install prometheus-stack prometheus-community/kube-prometheus-stack \
  --values ./k8s/init-monitoring-2/16-prometheus-values.yaml
kubectl port-forward svc/prometheus-stack-kube-prom-prometheus 9090:9090
kubectl port-forward svc/prometheus-stack-grafana 8080:80

# Verification
kubectl get svc
kubectl get pods
kubectl get all

# Delete Logging
helm uninstall eck-stack

# Delete everything else
kubectl delete all --all --all-namespaces
