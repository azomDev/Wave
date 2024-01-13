<script lang="ts">
    import { conversations } from "$lib/conversations";
</script>

<main class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-3xl font-semibold mb-4">Main Menu</h1>

    <section class="bg-white rounded-lg p-6 shadow-md w-full max-w-md">
        <ul class="space-y-4">
            {#each $conversations as { id, name }}
                <li>
                    <a href={`/conversation/${id}`} class="block p-2 rounded-md hover:bg-gray-200 transition">{name}</a>
                </li>
            {/each}
        </ul>

        <div class="mt-6">
            <button class="bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition">
                Add Conversation
            </button>
        </div>
    </section>
</main>
