<!-- <script lang="ts">
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

<MyCounterButton /> -->

<!-- App.svelte -->

<script lang="ts">
    import { writable } from "svelte/store";
    import type { Conversation } from "ui";

    // const chat = api.chat["12345678901234567890"].subscribe();
    // chat.subscribe((message) => {
    //     addMessage(message.data as string);
    // });

    // Create a writable store for messages
    const conversations = writable<Conversation[]>([]);
    conversations.update((previous_conversations) => [
        ...previous_conversations,
        { name: "Convo 1", id: 123456 },
    ]);
    conversations.update((previous_conversations) => [
        ...previous_conversations,
        { name: "Convo 2", id: 234567 },
    ]);
    conversations.update((previous_conversations) => [
        ...previous_conversations,
        { name: "Convo 3", id: 345678 },
    ]);

    // Function to add a new message
    // function addMessage(text: string) {
    //     messages.update((prevMessages) => [...prevMessages, { id: new Date().getTime(), text }]);
    // }

    // let newMessage = "";

    // Function to handle sending a message
    // function sendMessage() {
    //     if (newMessage.trim() !== "") {
    //         chat.send({ username: "svelte", message: newMessage });
    //         addMessage(newMessage);
    //         newMessage = ""; // Clear the input box after sending
    //     }
    // }
</script>

<main>
    <h1>Main menu</h1>

    <section>
        <!-- Display messages -->
        <ul>
            {#each $conversations as { id, name }}
                <li><a href="Conversation/{id}">{name}</a></li>
            {/each}
        </ul>
    </section>

    <section>
        <!-- Input box for new message -->
        <!-- <input type="text" bind:value={newMessage} placeholder="Type your message..." /> -->

        <!-- Button to send message -->
        <!-- <button on:click={sendMessage}>Send</button> -->
    </section>
</main>
