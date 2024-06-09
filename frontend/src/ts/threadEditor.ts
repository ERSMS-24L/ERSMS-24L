import "../scss/styles.scss";
import { initLoginManager, getAuthorizationHeader } from "./login"

const urlParams = new URLSearchParams(window.location.search);
let threadId = urlParams.get("threadId") ?? null;
let postId: string | null = null;

async function createOrUpdateThread(title: string): Promise<void> {
  const response = await fetch(
    "/threads/api/v1/threads",
    {
      method: threadId !== null ? "PUT": "POST",
      headers: {
        "Authorization": getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify(threadId !== null ? { threadId, title } : { title }),
    },
  );
  if (!response.ok) throw `Failed to create thread: ${response.status} ${response.statusText}`;
  threadId = (await response.json()).id;
}

async function createOrUpdatePost(content: string): Promise<void> {
  const response = await fetch(
    "/posts/api/v1/posts",
    {
      method: postId !== null ? "PUT" : "POST",
      headers: {
        "Authorization": getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postId !== null ? {postId, content} : {threadId, content}),
    },
  );
  if (!response.ok) throw `Failed to create post: ${response.status} ${response.statusText}`;
}

async function loadData(): Promise<void> {
  if (threadId === null) return;

  const response = await fetch(`/threads/api/v1/threads/${encodeURIComponent(threadId)}`);
  if (!response.ok) {
    throw `Failed to fetch thread details ${threadId}: ${response.status} ${response.statusText}`;
  }

  const data = await response.json();
  postId = data.postId;
  (document.getElementById("form_title") as HTMLInputElement).value = data.title;
  (document.getElementById("form_content") as HTMLInputElement).value = data.post;
}

async function on_submit(): Promise<void> {
  const title = (document.getElementById("form_title") as HTMLInputElement).value;
  if (title === "") throw `Thread title can't be empty`;  // TODO: Show this error to the user
  const content = (document.getElementById("form_content") as HTMLInputElement).value;
  await createOrUpdateThread(title);
  await createOrUpdatePost(content);
  window.location.assign(`thread.html?threadId=${encodeURIComponent(threadId)}`);
}

async function init() {
  initLoginManager(true);
  await loadData();

  const submit_button = document.getElementById("form_submit") as HTMLButtonElement;
  if (threadId !== null) submit_button.innerText = "Update thread";
  submit_button.onclick = on_submit;
}

init();
