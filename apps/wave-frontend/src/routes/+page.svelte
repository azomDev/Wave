<script lang="ts">
    import { conversations } from "$lib/conversations";
</script>

<main>
    <h1>Main menu</h1>

    <section>
        <ul>
            {#each $conversations as conversation}
                <li><a href="/conversation/{conversation.id}">{conversation.name}</a></li>
            {/each}
        </ul>
    </section>
</main>
