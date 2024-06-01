import tailwindForms from "@tailwindcss/forms";
import type { Config } from "tailwindcss";

export default {
    content: ["./src/**/*.{html,js,jsx,ts,tsx}"],
    darkMode: "class",
    theme: {
        extend: {},
    },
    plugins: [tailwindForms()],
} satisfies Config;
