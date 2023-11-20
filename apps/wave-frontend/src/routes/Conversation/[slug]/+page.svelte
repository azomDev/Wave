<script lang="ts">
    import { chat } from "$lib/api";
    import { _messages } from "$lib/api";
    import { sendMessage } from "$lib/api";

    export let data;

    let newMessage = "";

    // Function to handle sending a message
    function send() {
        if (newMessage.trim() !== "") {
            chat.send({ username: "svelte", message: newMessage });
            sendMessage(newMessage);
            newMessage = "";
        }
    }
</script>

<main>
    <h1>Convo {data.slug}</h1>

    <a href="/">Main menu</a>

    <section>
        <!-- Display messages -->
        <ul>
            {#each $_messages as { id, text }}
                <li>{text}</li>
            {/each}
        </ul>
    </section>

    <section>
        <!-- Input box for new message -->
        <input type="text" bind:value={newMessage} placeholder="Type your message..." />

        <!-- Button to send message -->
        <button on:click={send}>Send</button>
    </section>
</main>
