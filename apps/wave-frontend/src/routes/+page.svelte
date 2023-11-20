<script lang="ts">
    import { _conversations } from "$lib/api";
</script>

<main>
    <h1>Main menu</h1>

    <section>
        <ul>
            {#each $_conversations as conversation}
                <li><a href="Conversation/{conversation.id}">{conversation.name}</a></li>
            {/each}
        </ul>
    </section>
</main>
