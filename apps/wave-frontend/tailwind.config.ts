import type { Config } from "tailwindcss";

import tailwindForms from "@tailwindcss/forms";

const config = {
    darkMode: "class",
    content: ["./src/**/*.{html,js,svelte,ts}"],
    theme: {
        extend: {},
    },
    plugins: [tailwindForms()],
} satisfies Config;

export default config;
