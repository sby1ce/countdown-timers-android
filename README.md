<!--
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Countdown

Android port of [countdowns](https://github.com/sby1ce/countdown-timers)
web app.

# Build

There aren't any real alternatives to Android Studio. Should just work.

Note: You need Rust installed with
`aarch64-linux-android` (for release) and
`x86_64-linux-android` (for Android Studio)
as well as Android Studio NDK.

At the time of writing, see if you have NDK installed in Android Studio
File -> Settings -> Languages & Frameworks ->
Android SDK -> SDK Tools -> NDK (Side by side)

>[!NOTE]
> library-wrapper in NDK uses Python `pipes` library which was removed in Python 3.13
> At the time of writing in order to build Rust from Gradle you need to downgrade global Python <3.13

# Licences

This project is licensed under AGPL-3.0-or-later \
See LICENCE.txt for details

This project uses the following third-party packages

- Jetpack Compose
- and other libraries needed for Android
- [Icons](https://fonts.google.com/icons) - licensed under `Apache-2.0`
- `jna` - licensed under `Apache-2.0 OR LGPL-2.1`
- [`rust-android-gradle`](https://github.com/mozilla/rust-android-gradle) -
  licensed under `Apache-2.0`
- [`uniffi`](https://mozilla.github.io/uniffi-rs/latest/) - 
  licensed under `MPL-2.0`
