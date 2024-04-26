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


# Prometheus v2
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm upgrade --install prometheus-stack prometheus-community/kube-prometheus-stack \
  --values ./k8s/init-monitoring-2/16-prometheus-values.yaml
kubectl port-forward svc/prometheus-stack-kube-prom-prometheus 9090:9090
kubectl port-forward svc/prometheus-stack-grafana 8080:80

## Prometheus
## https://grafana.com/docs/grafana-cloud/monitor-infrastructure/kubernetes-monitoring/configuration/configure-infrastructure-manually/prometheus/prometheus-operator/
#kubectl create -f https://raw.githubusercontent.com/prometheus-operator/prometheus-operator/master/bundle.yaml
#kubectl get deploy
#kubectl apply -f k8s/init-monitoring/
#kubectl apply -f k8s/init-monitoring/16-prometheus-roles.yaml
#kubectl apply -f k8s/init-monitoring/17-prometheus.yaml
#kubectl get prometheus
#kubectl apply -f k8s/init-monitoring/18-prometheus-service.yaml
#kubectl port-forward svc/prometheus 9090
#kubectl apply -f init-monitoring/19-prometheus-monitor-service.yaml
#
## OLD Monitoring
#kubectl apply -f k8s/init-monitoring/
#
## OLD Prometheus
#helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
#helm repo update
#helm install prometheus prometheus-community/prometheus \
#  --set server.persistentVolume.enabled=true \
#  --set server.persistentVolume.storageClass=local-storage \
#  --set server.persistentVolume.existingClaim=prometheus-pvc
#export POD_NAME=$(kubectl get pods --namespace default -l "app=prometheus-pushgateway,component=pushgateway" -o jsonpath="{.items[0].metadata.name}")
#kubectl --namespace default port-forward $POD_NAME 9091
#kubectl expose service prometheus-server --type=NodePort --target-port=9090 --name=prometheus-server-ext
#
## OLD Grafana
#helm repo add grafana https://grafana.github.io/helm-charts
#helm repo update
#helm install grafana grafana/grafana \
#  --set persistence.enabled=true \
#  --set persistence.storageClassName="local-storage" \
#  --set persistence.existingClaim="grafana-pvc" \
#  --set "affinity.nodeAffinity.requiredDuringSchedulingIgnoredDuringExecution.nodeSelectorTerms[0].matchExpressions[0].key=run" \
#  --set "affinity.nodeAffinity.requiredDuringSchedulingIgnoredDuringExecution.nodeSelectorTerms[0].matchExpressions[0].operator=In" \
#  --set "affinity.nodeAffinity.requiredDuringSchedulingIgnoredDuringExecution.nodeSelectorTerms[0].matchExpressions[0].values[0]=monitoring"
#kubectl expose service grafana --type=NodePort --target-port=3000 --name=grafana-ext

# Verification
kubectl get svc
kubectl get pods
kubectl get all

# Delete Logging
helm uninstall eck-stack

# Delete everything else
kubectl delete all --all --all-namespaces
