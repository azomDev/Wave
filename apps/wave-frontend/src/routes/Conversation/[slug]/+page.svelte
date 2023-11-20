<script lang="ts">
    import { _messages } from "$lib/api";
    import { send_message_server } from "$lib/api";

    export let data;

    let new_message = "";

    function send_message() {
        if (new_message.trim() !== "") {
            send_message_server(new_message);
            new_message = "";
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
        <input type="text" bind:value={new_message} placeholder="Type your message..." />
        <button on:click={send_message}>Send</button>
    </section>
</main>
