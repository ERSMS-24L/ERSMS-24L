#!/bin/bash

kubectl config get-contexts
kubectl config use-context docker-desktop

# Keycloak secret
kubectl create secret generic cert-pem-1 --from-file accounts/src/main/resources/client.pem -n ersms-forum

# use other context
kubectl apply -f k8s/init-namespace/
kubectl apply -f k8s/init-services/
kubectl apply -f k8s/init-persistent-volumes/
kubectl apply -f k8s/

kubectl get svc -n ersms-forum
kubectl get pods -n ersms-forum

kubectl port-forward svc/axonserver -n ersms-forum 8024:8024
kubectl port-forward svc/mongodb -n ersms-forum 27017:27017

# Logging
helm repo add elastic https://helm.elastic.co
helm repo update
kubectl create -f https://download.elastic.co/downloads/eck/2.12.1/crds.yaml
kubectl apply -f https://download.elastic.co/downloads/eck/2.12.1/operator.yaml
#helm install eck-stack elastic/eck-stack \
#    --values https://raw.githubusercontent.com/elastic/cloud-on-k8s/2.12/deploy/eck-stack/examples/logstash/basic-eck.yaml
kubectl apply -f k8s/init-logging/14-filebeat-config.yaml
helm upgrade --install eck-stack elastic/eck-stack --values k8s/init-logging/15-logging-config.yaml
kubectl get secret elasticsearch-es-elastic-user -o=jsonpath='{.data.elastic}' | base64 --decode; echo
# user: elastic
kubectl port-forward svc/elasticsearch-es-http 9200:9200
# address: https://localhost:9200/login
kubectl port-forward svc/eck-stack-eck-kibana-kb-http 5601:5601
# address: https://localhost:5601/login
# Hamburger -> Management -> Kibana -> Data Views ->
# https://localhost:5601/app/management/kibana/dataViews

# Monitoring
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm upgrade --install prometheus-stack prometheus-community/kube-prometheus-stack \
  --values ./k8s/init-monitoring/16-prometheus-values.yaml
kubectl port-forward svc/prometheus-stack-kube-prom-prometheus 9090:9090
kubectl port-forward svc/prometheus-stack-grafana 8080:80
# spring dashboard: 19004
#kubectl scale deployment prometheus-stack-grafana --replicas=1
#kubectl scale deployment prometheus-stack-grafana --replicas=1

# Verification
kubectl get svc
kubectl get pods
kubectl get all

# Delete Logging
helm uninstall eck-stack
helm uninstall prometheus-stack

# Delete everything else
kubectl delete all --all --all-namespaces
