import { edenTreaty } from "@elysiajs/eden";
import type { App } from "wave-backend";

const api = edenTreaty<App>("http://0.0.0.0:3000");

export default api;
