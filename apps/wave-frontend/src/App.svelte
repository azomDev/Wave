<script lang="ts">
    import Greet from "./lib/Greet.svelte";
    import { MyCounterButton } from "ui";
    import WebSocket from "tauri-plugin-websocket-api";

    const backendws = WebSocket.connect("ws://localhost:3000");
    backendws.then((ws) =>
        ws.addListener((message) => {
            console.log("🚀 ~ message:", message.data);
        })
    );

    async function Click() {
        const ws = await backendws;
        console.log("🚀 ~ file: App.svelte:15 ~ Click ~ ws:", ws);
        await ws.send("Hello World");
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
