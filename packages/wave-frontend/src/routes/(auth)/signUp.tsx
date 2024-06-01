import { action } from "@solidjs/router";
import { Kyber1024 } from "crystals-kyber-js";
import { createSignal } from "solid-js";
import { Button } from "~/components/ui/Button";
import { TextField } from "~/components/ui/TextField";
import { api } from "~/lib/api";
import { EncryptionAES } from "~/lib/crypto/aes";
import { KeyWrapperAES } from "~/lib/crypto/userKey";
import { atosDeep } from "~/lib/utils";

export default function SignInPage() {
    const [error, setError] = createSignal<string | null>(null);

    const submit = action(async (formData: FormData) => {
        const username = formData.get("username")?.toString();
        const password = formData.get("password")?.toString();
        const displayName = formData.get("name")?.toString();

        // TODO: Validate
        if (!username || !password || !displayName) {
            setError("Invalid form data");
            return;
        }

        const $masterKey = new EncryptionAES();
        const $userPasskey = new KeyWrapperAES(password, username);
        const wrappedMasterKey = $userPasskey.wrap($masterKey);

        const kyber = new Kyber1024();
        const [publicKey, $privateKey] = await kyber.generateKeyPair();

        const res = await api.auth.signup.post(
            atosDeep({
                username,
                displayName,
                masterKey: await wrappedMasterKey,
                privateKey: await $masterKey.encrypt($privateKey),
                publicKey: publicKey,
            }),
        );

        console.log(res);
    }, "signin-form-submit");

    return (
        <main class="container mx-auto">
            <h1 class="text-2xl mb-2">Sign In</h1>

            <form action={submit} method="post">
                <TextField name="name">
                    <TextField.Label>Name</TextField.Label>
                    <TextField.Input type="text" />
                </TextField>

                <TextField name="username">
                    <TextField.Label>Handle</TextField.Label>
                    <TextField.Input type="text" />
                </TextField>

                <TextField name="password">
                    <TextField.Label>Name</TextField.Label>
                    <TextField.Input type="password" />
                </TextField>

                <div class="pt-4">
                    <Button variant="default" type="submit">
                        Create an account
                    </Button>
                </div>
            </form>
        </main>
    );
}
