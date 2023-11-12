<script lang="ts">
    import Greet from "./lib/Greet.svelte";
    import { MyCounterButton } from "ui";

    import { edenTreaty } from "@elysiajs/eden";
    import type { App } from "wave-backend";

    const api = edenTreaty<App>("http://0.0.0.0:8080");
    const chat = api.client.subscribe();
    chat.subscribe((message) => {
        console.log("Received:", message);
    });

    async function Click() {
        chat.send({ username: "tauri", message: "Hello World" });
    }
</script>

<main class="container">
    <h1>Welcome to Tauri!</h1>

    <div class="row">
        <a href="https://vitejs.dev" target="_blank">
            <img src="/vite.svg" class="logo vite" alt="Vite Logo" />
        </a>
        <a href="https://tauri.app" target="_blank">
            <img src="/tauri.svg" class="logo tauri" alt="Tauri Logo" />
        </a>
        <a href="https://svelte.dev" target="_blank">
            <img src="/svelte.svg" class="logo svelte" alt="Svelte Logo" />
        </a>
    </div>

    <MyCounterButton />

    <p>Click on the Tauri, Vite, and Svelte logos to learn more.</p>

    <button on:click={Click}>ws</button>

    <div class="row">
        <Greet />
    </div>
</main>

<style>
    .logo.vite:hover {
        filter: drop-shadow(0 0 2em #747bff);
    }

    .logo.svelte:hover {
        filter: drop-shadow(0 0 2em #ff3e00);
    }
</style>
