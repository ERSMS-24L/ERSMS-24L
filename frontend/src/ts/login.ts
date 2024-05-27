import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: new URL("/keycloak", window.location.toString()).toString(),
  realm: 'ersms',
  clientId: 'erms_client',
});

export interface UserDetails {
  accountId: string,
  name: string,
  email: string,
}

export function getAuthorizationHeader(): string {
  if (keycloak.authenticated !== true) return "";
  return `Bearer ${keycloak.token}`;
}

export async function getUserDetails(): Promise<UserDetails> {
  const response = await fetch(
    `/accounts/api/v1/accounts/me`,
    {headers: {Authorization: getAuthorizationHeader()}},
  );
  if (!response.ok) {
    console.error(`Failed to fetch user details: ${response.status} ${response.statusText}`);
    return { accountId: "", name: "", email: ""};
  }
  return await response.json();
}

function createLoginButton() {
  const anchor = document.createElement("a");
  anchor.href = keycloak.createLoginUrl({
    scope: "openid",
    redirectUri: window.location.href,
    locale: "en",
  });
  anchor.classList.add("btn", "btn-primary");
  anchor.role = "button";
  anchor.rel = "noreferrer";
  anchor.innerText = "Login";

  (document.getElementById("login_space") as HTMLSpanElement).replaceChildren(anchor);
}

async function createLogoutButton() {
  const username = (await getUserDetails()).name;

  const usernameSpan = document.createElement("span");
  usernameSpan.classList.add("me-2");
  usernameSpan.innerText = `Logged in as: ${username}`;

  const anchor = document.createElement("a");
  anchor.href = keycloak.createLogoutUrl({redirectUri: window.location.href});
  anchor.classList.add("btn", "btn-secondary");
  anchor.role = "button";
  anchor.rel = "noreferrer";
  anchor.innerText = "Logout";

  (document.getElementById("login_space") as HTMLSpanElement).replaceChildren(usernameSpan, anchor);
}

export async function initLoginManager(requireLogin: boolean = false) {
  await keycloak.init({
    onLoad: requireLogin ? "login-required" : "check-sso",
    silentCheckSsoRedirectUri: new URL("silent_sso_check.html", location.href).toString(),
  });

  if (!keycloak.authenticated) {
    createLoginButton();
  } else {
    await createLogoutButton();
  }
}
