<script lang="ts">
    import { writable } from "svelte/store";
    import type { Message } from "ui";
    import api from "$lib/api";

    export let data;

    const chat = api.chat["12345678901234567890"].subscribe();
    chat.subscribe((message) => {
        addMessage(message.data as string);
    });

    // Create a writable store for messages
    const messages = writable<Message[]>([]);

    // Function to add a new message
    function addMessage(text: string) {
        messages.update((prevMessages) => [...prevMessages, { id: new Date().getTime(), text }]);
    }

    let newMessage = "";

    // Function to handle sending a message
    function sendMessage() {
        if (newMessage.trim() !== "") {
            chat.send({ username: "svelte", message: newMessage });
            addMessage(newMessage);
            newMessage = ""; // Clear the input box after sending
        }
    }
</script>

<main>
    <h1>Convo {data.slug}</h1>

    <a href="/">Main menu</a>

    <section>
        <!-- Display messages -->
        <ul>
            {#each $messages as { id, text }}
                <li>{text}</li>
            {/each}
        </ul>
    </section>

    <section>
        <!-- Input box for new message -->
        <input type="text" bind:value={newMessage} placeholder="Type your message..." />

        <!-- Button to send message -->
        <button on:click={sendMessage}>Send</button>
    </section>
</main>
