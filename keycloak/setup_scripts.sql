-- Execute this script on Keycloak DB
-- but first, set `targetUri` - url of POST /accounts

INSERT INTO realm_attribute (name, value, realm_id)
VALUES ('_providerConfig.ext-event-http.0',
		'{"targetUri": "https://accounts-service.ersms-forum.svc.cluster.local:8081/accounts/api/v1/accounts"}',
		(SELECT id
		 FROM realm
		 WHERE name = 'master'));