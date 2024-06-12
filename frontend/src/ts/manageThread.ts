import "../scss/styles.scss";
import * as login from "./login";

const params = new URLSearchParams(window.location.search);
const threadId = params.get("threadId") ?? "";

async function init() {
    await login.initLoginManager(true);
}

init();
