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

export function createPageItems(page: number, totalPages: number): HTMLLIElement[] {
  const pageItems: HTMLLIElement[] = [];
  pageItems.push(createPageItem("Previous", page - 1, page === 0 ? "disabled" : undefined));
  for (let i = 0; i < totalPages; ++i)
    pageItems.push(createPageItem((i + 1).toString(), i, i === page ? "active" : undefined));
  pageItems.push(createPageItem("Next", page + 1, page === totalPages - 1 ? "disabled" : undefined));
  return pageItems;
}

export function showPagination(page: number, totalPages: number, paginationUlId = "pagination"): void {
  const pagination = document.getElementById(paginationUlId) as HTMLUListElement;
  pagination.replaceChildren(...createPageItems(page, totalPages));
}
