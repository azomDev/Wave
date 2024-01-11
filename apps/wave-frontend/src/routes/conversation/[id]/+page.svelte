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
                type: "text", 
                id: null
            }
            send(messageToBeSent);
            message = "";
        }
    }
</script>

<main class="flex flex-col h-screen">
    <h1>Convo {conversation?.name}</h1>

    <a href="/">Main menu</a>
    
    <section class="flex-1 overflow-y-auto">
        <ul class="space-y-4">
            {#each $messages as { message, sender, time_sent }}
                <li class="flex items-center space-x-4 rtl:space-x-reverse">
                    <Avatar/>
                    <div class="space-y-1 font-medium dark:text-white">
                        {sender} <span>{(new Date(time_sent)).toLocaleString('en-GB')}</span>
                        <div class="text-sm text-gray-500 dark:text-gray-400">{message}</div>
                    </div>
                </li>                      
            {/each}
        </ul>
    </section>
    
    
    <div>
        <ButtonGroup class="w-full">
          <Input id="input-addon"  placeholder="Type your message..." type="text" bind:value={message} />
          <Button color="primary" on:click={sendMessage}>Send</Button>
        </ButtonGroup>
    </div>

</main>
