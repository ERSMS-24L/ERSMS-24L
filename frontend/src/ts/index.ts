import "../scss/styles.scss";
import { showPagination } from "./pages";
import { initLoginManager } from "./login";

interface Thread {
  threadId: string,
  accountId: string,
  username: string,
  title: string,
  post: string,
  lastModified: string,
}

interface ThreadList {
  threads: Thread[],
  totalPages: number,
}

function createThreadCard(thread: Thread): HTMLDivElement {
  // <div class="col"><div class="card mb-2">
  //  <div class="card-body">
  //    <a href="thread.html?threadId=XXX" class="h5 card-title">{{ title }}</h5>
  //    <div class="card-text">{{ text }}</div>
  //  </div>
  //  <div class="card-footer">
  //    <small class="text-muted">Created by {{ author }}, last modified {{ last_modified }}</small>
  //  </div>
  // </div></div>

  const title = document.createElement("a");
  title.href = `thread.html?threadId=${encodeURIComponent(thread.threadId)}`;
  title.classList.add("card-title");
  title.classList.add("h5");
  title.innerText = thread.title;

  const text = document.createElement("div");
  text.classList.add("card-text");
  text.innerText = thread.post;

  const body = document.createElement("div");
  body.classList.add("card-body");
  body.append(title, text);

  const footerText = document.createElement("small");
  footerText.classList.add("text-muted");
  // TODO: Change last-modified to a human-readable timestamp
  footerText.innerText = `Created by ${thread.username}, last modified ${thread.lastModified}`;

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

function showThreads(threads: Thread[]): void {
  const postsContainer = document.getElementById("posts") as HTMLDivElement;
  postsContainer.replaceChildren(...threads.map(createThreadCard));
}

async function loadThreadsPage(page = 0, pageSize = 20): Promise<ThreadList> {
    const response = await fetch(`/threads/api/v1/threads?page=${encodeURIComponent(page)}&size=${encodeURIComponent(pageSize)}&sort=lastModified,desc`);
    if (!response.ok) {
      console.error(`Failed to fetch posts page ${page}: ${response.status} ${response.statusText}`);
      return { threads: [], totalPages: 1 };
    }
    const data = await response.json();
    return { threads: data.content, totalPages: data.totalPages };
}

async function showPage(page = 0): Promise<void> {
  const { threads, totalPages } = await loadThreadsPage(page);
  showThreads(threads);
  showPagination(page, totalPages);
}

async function init(): Promise<void> {
  const params = new URLSearchParams(window.location.search);
  let page = parseInt(params.get("page") ?? "0", 10);
  if (isNaN(page)) page = 0;
  await showPage(page);
}

init();
initLoginManager();
