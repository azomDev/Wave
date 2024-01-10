<script lang="ts">
    import { MyCounterButton } from "ui";
    import type { Conversation } from "ui";
    import api from "$lib/api";

    const chat = api.chat["12345678901234567890"].subscribe();
    chat.subscribe((message) => {
        console.log("Received:", message);
    });

    async function click() {
        chat.send({ username: "tauri", message: "Hello World" });
    }
</script>

<h1>Welcome to SvelteKit</h1>
<p>Visit <a href="https://kit.svelte.dev">kit.svelte.dev</a> to read the documentation</p>

<button on:click={click}>WS</button>

<MyCounterButton />
