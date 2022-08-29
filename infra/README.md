
### Kubernetes:
|  Object | Name | Purpose  | Definition YAML  |
|---|---|---|---|
| Deployment | fw-boot-samples-api | To handle API requests | infra/kubernetes/deployments/base/deployments.yaml |
| Deployment | fw-boot-samples-kafka-processor | To process kafka messages | infra/kubernetes/deployments/base/deployments.yaml |
| Deployment | fw-boot-samples-sqs-processor | To process SQS messages | infra/kubernetes/deployments/base/deployments.yaml |
| Service | fw-boot-samples-service | To expose API pods | infra/kubernetes/service.yaml |
| Gateway | fw-boot-samples-gateway | To connect with Istio | infra/kubernetes/istio.yaml |
| VirtualService | fw-boot-samples-vs | To route from Istio to App containers/pods | infra/kubernetes/istio.yaml |
