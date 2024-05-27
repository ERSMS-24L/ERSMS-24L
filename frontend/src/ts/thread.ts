import "../scss/styles.scss";
import { showPagination } from "./pages";

interface Post {
  postId: string,
  threadId: string,
  accountId: string,
  username: string,
  content: string,
  createdAt: string,
  votes: number,
}

interface PostList {
  posts: Post[],
  totalPages: number,
}

function createPostCard(post: Post): HTMLDivElement {
  // <div class="col"><div class="card mb-2">
  //  <div class="card-body">
  //    <div class="card-text">{{ text }}</div>
  //  </div>
  //  <div class="card-footer">
  //    <small class="text-muted">Created by {{ author }}, last modified {{ last_modified }}</small>
  //  </div>
  // </div></div>

  const text = document.createElement("div");
  text.classList.add("card-text");
  text.innerText = post.content;

  const body = document.createElement("div");
  body.classList.add("card-body");
  body.append(text);

  const footerText = document.createElement("small");
  footerText.classList.add("text-muted");
  // TODO: Change last-modified to a human-readable timestamp
  footerText.innerText = `Created by ${post.username}, created at ${post.createdAt}`;

  const footer = document.createElement("div");
  footer.classList.add("card-footer");
  footer.append(footerText)

  const card = document.createElement("div");
  card.classList.add("card", "mb-2");
  card.append(body, footer)

  const wrapper = document.createElement("div")
  wrapper.classList.add("col");
  wrapper.append(card);
  return wrapper;
}

function showPosts(posts: Post[]): void {
  const postsContainer = document.getElementById("posts") as HTMLDivElement;
  postsContainer.replaceChildren(...posts.map(createPostCard));
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

  (document.getElementById("thread_name_header") as HTMLHeadingElement).innerText = data.title;
  (document.getElementById("new_post_button") as HTMLAnchorElement).href = `new_post.html?threadId=${encodeURIComponent(threadId)}`;
}

async function init(): Promise<void> {
  const params = new URLSearchParams(window.location.search);
  let threadId = params.get("threadId") ?? "";
  let page = parseInt(params.get("page") ?? "0", 10);
  if (isNaN(page)) page = 0;

  await Promise.all([showHeader(threadId), showPage(threadId, page)]);
}

init();
