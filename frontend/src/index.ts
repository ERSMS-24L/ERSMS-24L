import "./scss/styles.scss";
import * as DOMPurify from "dompurify";

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
  //    <h5 class="card-title">{{ title }}</h5>
  //    <div class="card-text">{{ text }}</div>
  //  </div>
  //  <div class="card-footer">
  //    <small class="text-muted">Created by {{ author }}, last modified {{ last_modified }}</small>
  //  </div>
  // </div></div>

  const title = document.createElement("h5");
  title.classList.add("card-title");
  title.innerText = thread.title;

  const text = document.createElement("div");
  text.classList.add("card-text");
  text.innerHTML = DOMPurify.sanitize(thread.post);

  const body = document.createElement("div");
  body.classList.add("card-body");
  body.append(title, text);

  const footerText = document.createElement("small");
  footerText.classList.add("text-muted");
  // TODO: Change last-modified to a human-readable timestamp
  footerText.innerText = `Created by ${DOMPurify.sanitize(thread.username)}, last modified ${DOMPurify.sanitize(thread.lastModified)}`;

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

function createPageItem(label: string, target: number, status?: "active" | "disabled"): HTMLLIElement {
  const li = document.createElement("li");
  li.classList.add("page-item");

  if (status === undefined) {
    const href = new URL(window.location.href);
    href.searchParams.set("page", target.toString());

    const inner = document.createElement("a");
    inner.classList.add("page-link");
    inner.href = href.toString();
    inner.innerText = label;
    li.append(inner);
  } else {
    const inner = document.createElement("span");
    inner.classList.add("page-link", status);
    inner.innerText = label;
    li.append(inner);
  }

  return li;
}

function showPagination(page: number, totalPages: number): void {
  const pageItems: HTMLLIElement[] = [];
  pageItems.push(createPageItem("Previous", page - 1, page === 0 ? "disabled" : undefined));
  for (let i = 0; i < totalPages; ++i)
    pageItems.push(createPageItem((i + 1).toString(), i, i === page ? "active" : undefined));
  pageItems.push(createPageItem("Next", page + 1, page === totalPages - 1 ? "disabled" : undefined));

  const pagination = document.getElementById("pagination") as HTMLUListElement;
  pagination.replaceChildren(...pageItems);
}

async function loadThreadsPage(page = 0, pageSize = 20): Promise<ThreadList> {
    const request = await fetch(`/threads/api/v1/threads?page=${encodeURIComponent(page)}&size=${encodeURIComponent(pageSize)}&sort=lastModified,desc`);
    if (!request.ok) {
      console.error(`Failed to fetch posts page ${page}: ${request.status} ${request.statusText}`);
      return { threads: [], totalPages: 1 };
    }
    const data = await request.json();
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
  showPage(page);
}

init();
