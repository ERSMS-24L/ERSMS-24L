import "../scss/styles.scss";
import { initLoginManager, getAuthorizationHeader } from "./login"

let threadId = (new URLSearchParams(window.location.search)).get("threadId") ?? "";

async function create_post(content: string): Promise<void> {
  const response = await fetch(
    "/posts/api/v1/posts",
    {
      method: "POST",
      headers: {
        "Authorization": getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify({threadId: threadId, content: content}),
    },
  );
  if (!response.ok) throw `Failed to create post: ${response.status} ${response.statusText}`;
}

async function on_submit(): Promise<void> {
  const content = (document.getElementById("form_content") as HTMLInputElement).value;
  if (content !== "") {
    await create_post(content);
    // TODO: This should navigate to the last page, not first
    window.location.assign(`thread.html?threadId=${threadId}`);
  }
}

async function init(): Promise<void> {
  await initLoginManager(true);

  const response = await fetch(`/threads/api/v1/threads/${encodeURIComponent(threadId)}`);
  if (!response.ok) {
    console.error(`Failed to fetch thread details ${threadId}: ${response.status} ${response.statusText}`);
    return;
  }

  const data = await response.json();

  (document.getElementById("thread_name_header") as HTMLHeadingElement).innerText = `Adding a new post under: ${data.title}`;
  (document.getElementById("form_submit") as HTMLButtonElement).onclick = on_submit;
}

init()
