import { A } from "@solidjs/router";

export default function Nav() {
    return (
        <nav>
            <A href="/">Home</A>
            <A href="/signup">Create an account</A>
        </nav>
    );
}
