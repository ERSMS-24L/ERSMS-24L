import "../scss/styles.scss";
import * as login from "./login"
import { showPagination } from "./pages";

let firstPostId: string | null = null;
let userId: string = "";
let isModerator: boolean = false;

interface Post {
  postId: string,
  threadId: string,
  accountId: string,
  username: string,
  content: string,
  createdAt: number,
  votes: number,
}

type Vote = "UP_VOTE" | "DOWN_VOTE" | "NO_VOTE";

interface PostList {
  posts: Post[],
  totalPages: number,
}

async function sendVote(postId: string, vote: Vote): Promise<void> {
  const response = await fetch(
    "/posts/api/v1/votes",
    {
      method: "POST",
      headers: {
        "Authorization": login.getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify({postId: postId, vote: vote}),
    },
  );
  if (!response.ok) {
    throw `Failed to send vote ${vote} under ${postId}: ${response.status} ${response.statusText}`;
  }

  const votesParagraph = document.getElementById(`votes_${postId}`) as (HTMLParagraphElement | null);
  if (votesParagraph !== null) {
    votesParagraph.replaceWith(await createVotesSpan(postId));
  }
}

async function deletePost(postId: string): Promise<void> {
  const response = await fetch(
    "/posts/api/v1/posts",
    {
      method: "DELETE",
      headers: {
        "Authorization": login.getAuthorizationHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ postId: postId }),
    },
  );
  if (!response.ok) {
    throw `Failed to delete post ${postId}: ${response.status} ${response.statusText}`;
  }
  window.location.reload();
}

async function postVoteCount(postId: string): Promise<number> {
  const response = await fetch(`/posts/api/v1/posts/${encodeURIComponent(postId)}`);
  if (!response.ok) {
    throw `Failed to fetch post details for ${postId}: ${response.status} ${response.statusText}`;
  }
  const data = await response.json() as Post;
  return data.votes;
}

async function fetchUserId(): Promise<void> {
  userId = (await login.getUserDetails()).accountId;
}

async function fetchIsModerator(threadId: string): Promise<void> {
  isModerator = await login.isModeratorUnder(threadId);
}

async function currentUserVote(postId: string): Promise<Vote | "UNAUTHORIZED"> {
  const response = await fetch(
    `/posts/api/v1/votes/?postId=${encodeURIComponent(postId)}`,
    {
      headers: {"Authorization": login.getAuthorizationHeader()},
    },
  );
  if (response.status === 403) {
    return "UNAUTHORIZED";
  } else if (response.status === 404) {
    return "NO_VOTE";
  } else if (!response.ok) {
    throw `Failed to fetch vote under ${postId}: ${response.status} ${response.statusText}`;
  } else {
    const data = await response.json();
    return data.vote;
  }
}

async function createVotesSpan(postId: string, voteCount?: number): Promise<HTMLSpanElement> {
  const currentVote = await currentUserVote(postId);
  voteCount = voteCount ?? await postVoteCount(postId);

  // <span class="mb-0" id="votes_${postId}">
  //  Votes:
  //  <button type="button" class="btn btn-sm mx-1" onclick=...>-</button>
  //  <span>42</span>
  //  <button type="button" class="btn btn-" onclick=...>+</button>
  // </span>
  const span = document.createElement("span");
  span.id = `votes_${postId}`;
  span.innerText = "Votes:";

  if (currentVote !== "UNAUTHORIZED") {
    const minus = document.createElement("button");
    minus.type = "button";
    minus.classList.add("btn", "btn-sm", currentVote === "DOWN_VOTE" ? "btn-danger" : "btn-outline-danger", "mx-1");
    minus.innerText = "-";
    minus.onclick = () => sendVote(postId, currentVote === "DOWN_VOTE" ? "NO_VOTE" : "DOWN_VOTE");
    span.append(minus);
  }

  const count = document.createElement("span");
  count.innerText = voteCount.toFixed(0);
  span.append(count);

  if (currentVote !== "UNAUTHORIZED") {
    const plus = document.createElement("button");
    plus.type = "button";
    plus.classList.add("btn", "btn-sm", currentVote === "UP_VOTE" ? "btn-success" : "btn-outline-success", "ms-1");
    plus.innerText = "+";
    plus.onclick = () => sendVote(postId, currentVote === "UP_VOTE" ? "NO_VOTE" : "UP_VOTE");
    span.append(plus);
  }

  return span;
}

async function createButtonsSpan(post: Post): Promise<HTMLSpanElement> {
  if (userId === "") return null;
  const isAuthor = userId === post.accountId;
  const isFirstPost = post.postId === firstPostId;

  const buttons: (HTMLAnchorElement | HTMLButtonElement)[] = [];

  if (isAuthor) {
    const b = document.createElement("a");
    b.role = "button";
    b.classList.add("btn", "btn-sm", "btn-primary");
    b.innerHTML = '<i class="bi-pencil-square"></i>';
    if (isFirstPost) {
      b.href = `thread_editor.html?threadId=${encodeURIComponent(post.threadId)}&postId=${encodeURIComponent(post.postId)}`;
    } else {
      b.href = `post_editor.html?threadId=${encodeURIComponent(post.threadId)}&postId=${encodeURIComponent(post.postId)}`;
    }
    buttons.push(b);
  }

  if ((isAuthor || isModerator) && !isFirstPost) {
    const b = document.createElement("button");
    b.type = "button";
    b.role = "button";
    b.classList.add("btn", "btn-sm", "btn-primary");
    if (buttons.length > 0) b.classList.add("ms-1");
    b.innerHTML = '<i class="bi-trash"></i>';
    b.onclick = () => deletePost(post.postId);
    buttons.push(b);
  }

  if (isModerator) {
    const b = document.createElement("button");
    b.type = "button";
    b.role = "button";
    b.classList.add("btn", "btn-sm", "btn-primary");
    if (buttons.length > 0) b.classList.add("ms-1");
    b.innerHTML = '<i class="bi-shield"></i>';
    buttons.push(b);
  }

  // TODO: ifAdmin, add a site-wide ban button (btn-danger + bi-shield)

  const span = document.createElement("span");
  span.append(...buttons);
  return span;
}

async function createPostCard(post: Post): Promise<HTMLDivElement> {
  // <div class="col"><div class="card mb-2">
  //  <div class="card-body">
  //    <div class="card-text">{{ text }}</div>
  //  </div>
  //  <div class="card-footer">
  //    <div class="d-flex justify-content-between mb-0">votesSpan buttonsSpan</div>
  //    <small class="text-muted">Created by {{ author }}, last modified {{ last_modified }}</small>
  //  </div>
  // </div></div>

  const text = document.createElement("div");
  text.classList.add("card-text");
  text.innerText = post.content;

  const body = document.createElement("div");
  body.classList.add("card-body");
  body.append(text);

  const [footerVotes, footerButtons] = await Promise.all([
    createVotesSpan(post.postId, post.votes),
    createButtonsSpan(post),
  ]);
  const buttonsContainer = document.createElement("div");
  buttonsContainer.classList.add("d-flex", "justify-content-between", "mb-0");
  buttonsContainer.append(footerVotes, footerButtons);

  const createdAt = new Date(post.createdAt * 1000).toLocaleString();
  const footerText = document.createElement("small");
  footerText.classList.add("text-muted");
  footerText.innerText = `Created by ${post.username}, created at ${createdAt}`;

  const footer = document.createElement("div");
  footer.classList.add("card-footer");
  footer.append(buttonsContainer, footerText);

  const card = document.createElement("div");
  card.classList.add("card", "mb-2");
  card.append(body, footer)

  const wrapper = document.createElement("div")
  wrapper.classList.add("col");
  wrapper.append(card);
  return wrapper;
}

async function showPosts(posts: Post[]): Promise<void> {
  const postsContainer = document.getElementById("posts") as HTMLDivElement;
  postsContainer.replaceChildren(...await Promise.all(posts.map(createPostCard)));
}

async function loadPostsPage(threadId: string, page = 0, pageSize = 20): Promise<PostList> {
  const response = await fetch(`/posts/api/v1/posts?threadId=${encodeURIComponent(threadId)}&page=${encodeURIComponent(page)}&size=${encodeURIComponent(pageSize)}&sort=createdAt,asc`);
  if (!response.ok) {
    console.error(`Failed to fetch posts page ${page}: ${response.status} ${response.statusText}`);
    return { posts: [], totalPages: 1 };
  }
  const data = await response.json();
  return { posts: data.content, totalPages: data.totalPages };
}

async function showPage(threadId: string, page = 0): Promise<void> {
  const { posts: threads, totalPages } = await loadPostsPage(threadId, page);
  showPosts(threads);
  showPagination(page, totalPages);
}

async function showHeader(threadId: string): Promise<void> {
  const response = await fetch(`/threads/api/v1/threads/${encodeURIComponent(threadId)}`);
  if (!response.ok) {
    console.error(`Failed to fetch thread details ${threadId}: ${response.status} ${response.statusText}`);
    return;
  }

  const data = await response.json();

  firstPostId = data.postId;
  (document.getElementById("thread_name_header") as HTMLHeadingElement).innerText = data.title;
  (document.getElementById("new_post_button") as HTMLAnchorElement).href = `post_editor.html?threadId=${encodeURIComponent(threadId)}`;
}

async function init(): Promise<void> {
  await login.initLoginManager();

  const params = new URLSearchParams(window.location.search);
  let threadId = params.get("threadId") ?? "";
  let page = parseInt(params.get("page") ?? "0", 10);
  if (isNaN(page)) page = 0;

  await Promise.all([showHeader(threadId), fetchUserId(), fetchIsModerator(threadId)]);
  await showPage(threadId, page);
}

init();
