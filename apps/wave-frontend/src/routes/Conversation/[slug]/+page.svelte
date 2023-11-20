<script lang="ts">
    import { _messages } from "$lib/api";
    import { send_message_server } from "$lib/api";

    export let data;

    let newMessage = "";

    function send_message() {
        if (newMessage.trim() !== "") {
            send_message_server(newMessage);
            newMessage = "";
        }
    }
</script>

<main>
    <h1>Convo {data.slug}</h1>

    <a href="/">Main menu</a>

    <section>
        <ul>
            {#each $_messages as { id, text }}
                <li>{text}</li>
            {/each}
        </ul>
    </section>

    <section>
        <input type="text" bind:value={newMessage} placeholder="Type your message..." />
        <button on:click={send_message}>Send</button>
    </section>
</main>
