import "../scss/styles.scss";
import { initLoginManager, getAuthorizationHeader } from "./login"

async function create_thread(title: string): Promise<string> {
  const response = await fetch(
    "/threads/api/v1/threads",
    {
      method: "POST",
      headers: {
        "Authorization": getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify({title: title}),
    },
  );
  if (!response.ok) throw `Failed to create thread: ${response.status} ${response.statusText}`;
  return (await response.json()).id;
}

async function create_post(threadId: string, content: string): Promise<void> {
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
  const title = (document.getElementById("form_title") as HTMLInputElement).value;
  if (title === "") throw `Thread title can't be empty`;  // TODO: Show this error to the user
  const content = (document.getElementById("form_content") as HTMLInputElement).value;
  const threadId = await create_thread(title);
  if (content !== "") await create_post(threadId, content);
  window.location.assign(`thread.html?threadId=${encodeURIComponent(threadId)}`);
}

(document.getElementById("form_submit") as HTMLButtonElement).onclick = on_submit;
initLoginManager(true);
