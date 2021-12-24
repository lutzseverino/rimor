# Rimor
> ### /ˈriː.mor/
> to examine, explore, investigate, root up.

Rimor is a tool that uses paths to find Methods.

Primarily made to create command-like interfaces, Rimor bases itself on Discord's [slash command](https://discord.com/developers/docs/interactions/slash-commands) design, which means that its limits are defined by how Discord designs the behavior of this feature.
### Command design options
As stated before, this is also the design of Discord's slash commands. 
1. `Command` with subcommand groups (packages) each of them containing one or more `ChildCommand`s.
```
COMMAND
├── SUBCOMMANDGROUP
│   └── CHILDCOMMAND
│       └── options
├── SUBCOMMANDGROUP
│   └── CHILDCOMMAND
│       └── options
└── ...
```
2. `Command` with one or more `ChildCommand`s.
```
COMMAND
├── CHILDCOMMAND
│   └── options
├── CHILDCOMMAND
│   └── options
└── ...

```
3. A singular `Command`
```
COMMAND
└── options
```

### Usage
to-do