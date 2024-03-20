#!/bin/bash

kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl apply -f k8s/init-namespace/
kubectl apply -f k8s/init-services/
kubectl apply -f k8s/init-persistent-volumes/
kubectl apply -f k8s/

kubectl get svc -n ersms-forum
kubectl get pods -n ersms-forum
