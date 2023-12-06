import { edenTreaty } from "@elysiajs/eden";
import type { App } from "wave-backend";

export const api = edenTreaty<App>("http://localhost:3000");
