<script lang="ts">
    import { api } from "$lib/api";
    import { EncryptionAES } from "$lib/crypto/aes";
    import { KeyWrapperAES } from "$lib/crypto/userKey";
    import { hashSync } from "bcryptjs";
    import { Kyber1024 } from "crystals-kyber-js";

    const username = "guibi";
    const displayName = "guibi";
    const password = "pass";

    async function signup() {
        const [pubKyber, privKyber] = await new Kyber1024().generateKeyPair();

        const keyWrapper = new KeyWrapperAES(password, "dummy-user-id");
        const masterKey = new EncryptionAES(null);
        const wrappedKyberPriv = await masterKey.encrypt(privKyber);

        const res = await api.auth.signup.post({
            username,
            displayName,
            hash: hashSync(password),
            masterKey: await Array.fromAsync(await keyWrapper.wrap(masterKey)),
            publicKey: await Array.fromAsync(pubKyber),
            privateKey: await Array.fromAsync(wrappedKyberPriv.data),
        });
    }

    async function login() {
        const res = await api.auth.login.post({
            username,
            hash: hashSync(password),
        });
    }
</script>

<h1>Auth</h1>

<button on:click={signup}>Signup</button>
<button on:click={login}>Login</button>
