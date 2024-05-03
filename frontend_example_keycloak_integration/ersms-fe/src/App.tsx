import { useKeycloak } from '@react-keycloak/web';
import './App.css';

function App() {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) {
    return <></>;
  }

  function sendRequest() {
    const raw = JSON.stringify({
      name: 'name',
      email: 'email@email.com',
    });

    console.log(keycloak.token);

    fetch('/v1/accounts', {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + keycloak.token,
      },
      body: raw,
    })
      .then((response) => response.text())
      .then((result) => console.log(result))
      .catch((error) => console.error(error));
  }

  if (initialized && keycloak.tokenParsed) {
    console.log(keycloak);
    console.log(keycloak.tokenParsed);

    var logoutUrl = keycloak.createLogoutUrl({
      redirectUri: window.location.href,
    });

    return (
      <div className='App'>
        <div className='App-header'>
          <h1>ERSMS example - logged in</h1>
          <a
            className='App-link'
            href={logoutUrl}
            rel='noreferrer'
            target='_blank'
          >
            Keycloak logout
          </a>
          <br />
          <button onClick={sendRequest}>
            Send request to protected service endpoint
          </button>
        </div>
      </div>
    );
  }

  var loginUrl = keycloak.createLoginUrl({
    scope: 'openid',
    redirectUri: window.location.href,
    locale: 'en',
  });

  return (
    <div className='App'>
      <div className='App-header'>
        <h1>ERSMS example - logged out</h1>
        <a
          className='App-link'
          href={loginUrl}
          rel='noreferrer'
          target='_blank'
        >
          Keycloak login
        </a>
        <br />
        <button onClick={sendRequest}>
          Send request to protected service endpoint
        </button>
      </div>
    </div>
  );
}

export default App;
