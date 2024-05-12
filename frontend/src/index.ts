import "./scss/styles.scss";
import { Popover } from "bootstrap";

document.querySelectorAll('[data-bs-toggle="popover"]').forEach(p => new Popover(p));
