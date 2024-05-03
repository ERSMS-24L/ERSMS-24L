import Keycloak from 'keycloak-js';

// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'

// TODO: configurable keycloak url - maybe use 'keycloak.json'
const keycloak = new Keycloak({
  url: 'http://localhost:8888/',
  realm: 'ersms',
  clientId: 'erms_client',
});

export default keycloak;
