<h1 align="center">Ping Plugin</h1>

A lightweight and highly configurable plugin designed to check player ping with ease. Perfect for any server where knowing the player's connection latency is crucial!

## Commands

| Command           | Action                                         |
|-------------------|:-----------------------------------------------|
| `/ping`           | Displays your ping (Permission: `zoid.ping`)   |
| `/ping reload`    | Reloads the plugin configuration (Permission: Operator) |

<hr>

## Instalation

1. Download the plugin from [Releases](https://github.com/zoiddie/Ping/releases/)
2. Drop the plugin into the plugins folder in your server.
3. Restart or start the server.

## Configuration

Easily customize how ping information is displayed, whether in the action bar, chat, or with sounds.

```yaml
actionbar: true
chat: true
sound: true

message-1-argument: "<green>Your ping is <#0ecc1e>%ping%ms</green>"
message-2-argument: "<green>%player%'s ping is <#0ecc1e>%ping%ms</green>"
player-not-found-message: "<red>The player does not exist on the server</red>"
reload-message: "<green>Configuration reloaded.</green>"

sounds:
  ping-check: "ENTITY_EXPERIENCE_ORB_PICKUP"
  player-not-found: "entity.villager.trade"
  config-reload: "BLOCK_NOTE_BLOCK_PLING"

right-click-ping:
  enabled: true
  message: "<green>%player%'s ping is %ping%ms"
