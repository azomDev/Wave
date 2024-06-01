// @refresh reload
import { StartClient, mount } from "@solidjs/start/client";

// b iome-ignore lint/style/noNonNullAssertion: Can't be null
// mount(() => <StartClient />, document.getElementById("app")!);
mount(() => <StartClient />, document.body);
