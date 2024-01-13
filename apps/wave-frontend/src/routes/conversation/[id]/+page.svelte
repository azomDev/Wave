<script lang="ts">
    import { connectConversation } from "$lib/conversations.js";
    import { Label, Input, Button, ButtonGroup, Avatar, Listgroup } from 'flowbite-svelte';
    import type { Message } from "wave-types";

    export let data;

    const { conversation, messages, send } = connectConversation(data.chatId);

    let message: string = "";

    function sendMessage() {
        if (message.trim() !== "") {
            let messageToBeSent: Message = {
                message: message, 
                sender: "guibi", 
                time_sent: Date.now(), 
                sent: false, 
                modified: false, 
                id: crypto.randomUUID(),
            }
            send(messageToBeSent);
            message = "";
        }
    }

    function handleSubmit(event: { preventDefault: () => void; }) {
        event.preventDefault();
        sendMessage();
    }
</script>

<main class="flex flex-col h-screen p-4 bg-gray-100">
    <header class="bg-white p-4 mb-4">
        <h1 class="text-2xl font-semibold">Convo {conversation?.name}</h1>
    </header>

    <nav class="mb-4">
        <a href="/" class="text-blue-500">Main menu</a>
    </nav>
    
    <section class="flex-1 overflow-y-auto">
        <ul class="space-y-4">
            {#each $messages as { message, sender, time_sent, id }}
                <li class="flex items-start space-x-4 rtl:space-x-reverse">
                    <Avatar/>
                    <div class="space-y-1 font-medium">
                        <span class="text-gray-600">{sender}</span> 
                        <span class="text-xs text-gray-400">
                            {(new Date(time_sent)).toLocaleString('en-GB')}
                        </span>
                        <div class={`text-sm ${id === null ? 'text-gray-400' : 'text-gray-700'}`}>{message}</div>
                    </div>
                </li>                      
            {/each}
        </ul>
    </section>

    <form on:submit={handleSubmit} autocomplete="off">
        <ButtonGroup class="w-full">
          <Input id="input-addon" placeholder="Type your message..." type="text" bind:value={message} />
          <Button color="primary" type="submit">Send</Button>
        </ButtonGroup>
    </form>
</main>
