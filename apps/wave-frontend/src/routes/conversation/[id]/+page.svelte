<script lang="ts">
    import { connectConversation } from "$lib/conversations.js";

    export let data;

    const { conversation, messages, send } = connectConversation(data.chatId);

    let message = "";

    function sendMessage() {
        if (message.trim() !== "") {
            send(message);
            message = "";
        }
    }
</script>

<main>
    <h1>Convo {conversation?.name}</h1>

    <a href="/">Main menu</a>

    <section>
        <ul>
            {#each $messages as { id, message }}
                <li>{message}</li>
            {/each}
        </ul>
    </section>

    <section>
        <input type="text" bind:value={message} placeholder="Type your message..." />
        <button on:click={sendMessage}>Send</button>
    </section>
</main>
