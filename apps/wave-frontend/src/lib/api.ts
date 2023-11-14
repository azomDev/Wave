import { edenTreaty } from "@elysiajs/eden";
import type { App } from "wave-backend";

const api = edenTreaty<App>("http://localhost:3000");

export default api;
