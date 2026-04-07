# WorldExist

Paper `1.21.11` plugin that adds `/dimension check ...`.

## What it checks

- Loaded Bukkit/Paper worlds by exact name
- Existing world folders inside the server world container (`level.dat`)
- Multiverse-Core `worlds.yml` entries if `Multiverse-Core` is installed and enabled

This means it works with normal Minecraft worlds, and it also detects Multiverse-managed worlds that may not currently be loaded.

## Command

```mcfunction
/dimension check "name"
/dimension check number
/dimension check @s
```

`@s` resolves to the selected entity's current world name.

## Return value

The command returns:

- `1` when the world exists
- `0` when the world does not exist

It also prints `1` or `0` to chat/console, so it is usable both manually and from vanilla `execute`.

Example:

```mcfunction
execute store result score @s test run dimension check "world_nether"
execute store success score @s test run dimension check "custom_world"
```

Both `result` and `success` will become `1` if the world exists, otherwise `0`.

## Build

Requires Java 21 and Gradle.
