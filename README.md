# EcoWelcome Plugin

EcoWelcome is a simple, lightweight plugin for welcoming new and returning players on your Minecraft server. It supports integration with PlaceholderAPI for personalized messages.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Commands](#commands)
- [Permissions](#permissions)
- [Configuration](#configuration)
- [License](#license)
- [Contact](#contact)

## Features

- Send customizable join messages for first-time and returning players.
- Support for PlaceholderAPI for advanced placeholder replacements.
- Easy-to-use command to reload configuration on the fly.

## Installation

1. **Ensure your server is running a compatible version of Bukkit/Spigot with API version 1.21 or later.**
2. **Download EcoWelcome.jar and place it in your server's `plugins` directory.**
3. **(Optional) Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for placeholder support.**
4. **Restart your server to enable the plugin.**

## Commands

- `/ecowelcomereload`
    - **Description**: Reloads the EcoWelcome configuration from the file.
    - **Aliases**: `ewrl`
    - **Usage**: `/ecowelcomereload`

## Permissions

- `ew.reload`
    - **Default**: OP
    - **Description**: Allows the user to reload the EcoWelcome configuration.

## Configuration

EcoWelcome supports message customization via its `config.yml`:

```yaml
# All Messages Support Placeholder API Placeholders

first-time-join: "&b%player%, Joined for the first time!"
non-first-time-join: "&bWelcome back, %player%!"
```

Edit these messages to change how greetings appear to players. Ensure PlaceholderAPI is installed if using placeholders.

## License

EcoWelcome is licensed under the [Space_Mex Plugins License](LICENSE.md).
