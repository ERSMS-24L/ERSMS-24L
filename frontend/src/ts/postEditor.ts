import "../scss/styles.scss";
import { initLoginManager, getAuthorizationHeader } from "./login"

const urlParams = new URLSearchParams(window.location.search);
let threadId = urlParams.get("threadId") ?? null;
let postId = urlParams.get("postId") ?? null;

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

async function on_submit(): Promise<void> {
  const content = (document.getElementById("form_content") as HTMLInputElement).value;
  if (content !== "") {
    await createOrUpdatePost(content);
    // TODO: This should navigate to the last page, not first
    window.location.assign(`thread.html?threadId=${threadId}`);
  }
}

async function initThreadData(): Promise<void> {
  const response = await fetch(`/threads/api/v1/threads/${encodeURIComponent(threadId)}`);
  if (!response.ok) {
    throw `Failed to fetch thread details ${threadId}: ${response.status} ${response.statusText}`;
  }

  const data = await response.json();
  (document.getElementById("thread_name_header") as HTMLHeadingElement).innerText = postId === null ? `Adding a new post under: ${data.title}` : `Editing a post under: ${data.title}`;
}

async function initPostData(): Promise<void> {
  if (postId === null) return;

  const response = await fetch(`/posts/api/v1/posts/${encodeURIComponent(postId)}`);
  if (!response.ok) {
    throw `Failed to fetch post details ${postId}: ${response.status} ${response.statusText}`;
  }
  const data = await response.json();
  (document.getElementById("form_content") as HTMLTextAreaElement).value = data.content;
}

async function init(): Promise<void> {
  await initLoginManager(true);
  await Promise.all([initThreadData(), initPostData()]);

  const submit_button = document.getElementById("form_submit") as HTMLButtonElement
  if (postId !== null) submit_button.innerText = "Update post";
  submit_button.onclick = on_submit;
}

init();
