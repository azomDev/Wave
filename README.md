# Wave - All-in-One Messaging App
## Important: this project is on hold for some time until we can get a better idea of how to approach this.

Wave is an open source, comprehensive messaging solution aiming to revolutionize the way we communicate. By centralizing SMS, MMS, server messaging, email, and other messaging types into one application, Wave intends to provide an integrated and seamless communication experience.

## Technologies

-   Tauri for creating cross-platform desktop (and soon mobile) applications
-   Sveltekit for creating the user interface
-   Typescript for frontend and server development
-   Bun for building and optimizing frontend and server code
-   Vite for efficient development and building
-   postgreSQL for local and server-side database storage

## Features

### Comprehensive Messaging

Wave aims to support a broad range of messaging types including:

-   SMS/MMS
-   Server Messaging
-   Email
-   Phone calls
-   Voice and video calls
-   File sharing
-   And more

This wide array of messaging options allows users to consolidate their communications into one convenient platform.

## Cross-Platform Synchronization

Wave will support cross-platform synchronization. This allows users to seamlessly switch between devices (PC, mobile, tablet) and even send SMS from a computer when a phone is connected.

## Customization

One of the key aspects of Wave is its high level of customization. Wave plans to offer a wide range of options for modifying the user interface and the functionality of the app to suit individual needs and preferences.

## Multilingual Support and Chat Translation

To make Wave accessible to users around the world, we intend to offer multilingual support and chat translation.

## Security

In this digital era, security is paramount. Wave is designed with this principle in mind, intending to offer end-to-end encryption and local encryption options for every type of messaging. We are even considering the potential of implementing post quantum cryptography to stay one step ahead of potential threats.

## Current Progress

Please bear in mind that Wave is still in the early stages of development. The features described above are the goals we're working towards. We are making steady progress, and we're excited about the potential of this project.

Stay tuned to this space for updates on our progress and ways you can get involved. If you're excited about Wave and want to contribute, feel free to reach out or start contributing on our issues page.

* 6th of November 2023: Restarting from scratch

## Getting Involved

As an open source project, Wave welcomes contributions from the community. Whether you're a seasoned developer or a user with great ideas, there is a place for you here. We believe that collective intelligence is key to creating an app that is robust, user-friendly, and innovative. Any help is always appreciated, even a small suggestion.

## Contributing

If you see an issue that you want to help implementing:

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

You can also contribute just by creating an issue or participating in discussions to help with ideas and decisions.

## Initial configuration

Because Bun isn't currently working on Windows, WSL is needed to run the project.  
You can skip step 1 if you are on a Unix machine.

1. Install Ubuntu with WSL2.
2. Install [Bun](https://bun.sh) with the install script.
3. Install a recent version of [Node.js](https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-ubuntu-20-04#option-3-installing-node-using-the-node-version-manager).
4. Install the [linux requirements of Tauri](https://tauri.app/v1/guides/getting-started/prerequisites#setting-up-linux).
5. Run `bun install` followed by `bun dev` on the root folder.

## License

Distributed under the GNU General Public License v3.0. See `LICENSE` for more information.

## Contact

Email - azom.developer@gmail.com <br>
Discord - azom#6496 <br>

Join us in creating a future where communication is more integrated, customizable, and secure. Let's ride the Wave together.
