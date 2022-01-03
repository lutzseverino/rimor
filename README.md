![rimor banner](https://user-images.githubusercontent.com/28309837/147892881-707cf205-0ff7-41e5-ada5-a2638e51e6f2.png)
> #### /ˈriː.mor/
> to examine, explore, investigate, root up.

Rimor is a multipurpose command library that takes simple paths as input.

## Design

**Commands** in Rimor may contain an indefinite amount of **subcommands** and **subcommand groups** (which are, in
practice, more commands).

This means this system has no real limit, more **subcommand groups** can be added infinitely without significant
performance hits on execution.
### Examples
#### 1 level deep command example

```
Command
├── CommandMethod
├── SubcommandMethod #1
├── SubcommandMethod #2
├── SubcommandGroup #1 
│   ├── CommandMethod
│   ├── SubcommandMethod #1
│   └── SubcommandMethod #2
└── SubcommandGroup #2
    ├── CommandMethod
    ├── SubcommandMethod #1
    └── SubcommandMethod #2
```

#### Simple (single) command

```
Command
└── CommandMethod
```

#### Discord-like (slash) command example

```
Command
├── SubcommandGroup #1
│   ├── SubcommandMethod #1
│   └── SubcommandMethod #2
└── SubcommandGroup #2
    ├── SubcommandMethod #1
    └── SubcommandMethod #2
```

## Features

### Customized data pass through

By extending the provided `ExecutionData` class, you can pass any object to your commands:

```java

@Getter
public class CommandData extends ExecutionData {
    private final Guild guild;
    private final SlashCommandEvent event;

    public CommandData(Guild guild, SlashCommandEvent event) {
        this.guild = guild;
        this.event = event;
    }
}
```

```java
rimor.execute(path, new CommandData(guild,event));
```

### Aliases

Aliases can be added to any Type via the use of the `CommandNames` annotation:

```java

@CommandNames({"alias", "alias"})
public class ExampleCommand extends Command {

    public ExampleCommand() {
        addSubcommandGroup(new ExampleCommandGroup());
    }

    @MethodCommand // MethodCommand will not 
    public void run(CommandData data) {...}

    @MethodSubcommand
    @CommandNames({"alias", "alias"})
    public void subcommand(CommandData data) {...}
}

@CommandNames({"alias", "alias"})
public class ExampleCommandGroup extends SubcommandGroup {...
}
```

**Note**: If no aliases are provided, the name of the Class/Method will be used as the name.

### Parameter detection

The before-mentioned `ExecutionData` class also holds extra parameters if a command is found before the path ends.

```
C = Command
P = Parameters

your/amazing/command/here
     ^       ->
     C       P
```

This is performed automatically, all extra parameters will be added to a `List<String>` and be feed to
an `ExecutionData`.

## Usage

This library is not hosted in any repository as of right now, I will update this readme as soon as that changes.
Clone the repository and `mvn clean install` it using your IDE:
```shell
git clone https://github.com/Frequential/rimor.git
```

Let's set Rimor up, first, we instance it:

```java
public class Main {
    public static void main(String[] args) {
        Rimor rimor = new Rimor();
    }
}
```

Now, optionally, we can extend `ExecutionData`'s functionalities with extra information we want our commands to have:

```java
@Getter
public class CommandData extends ExecutionData {
    private final ImportantInformation info;

    public CommandData(ImportantInformation info) {
        this.info = info;
    }
}
```

Commands will now have access to any `ImportantInformation` you may want them to have.

Next up is creating our first command, we'll use every feature that Rimor offers to clear up all possible usages. We do
this by extending the `Command` included in the library.

Repeat this process indefinitely for subcommand groups too:

```java
@CommandNames({"alias", "alias"})
public class ExampleCommand extends Command {
    public ExampleCommand() {
        addSubcommandGroups(
                new ExampleSubcommandGroup(),    // "examplecommand/examplesubcommandgroup"
                new AnotherSubcommandGroup()    // "examplecommand/anothersubcommandgroup"
                // More subcommand groups go here
        );
    }

    @MethodCommand // "examplecommand"
    public void run(CommandData data) {
        // Your code goes here
    }

    @MethodSubcommand // "examplecommand/subcommand"
    @CommandNames({"alias", "alias"})
    public void subcommand(CommandData data) {
        // Your code goes here
    }

    @MethodSubcommand // "examplecommand/anothersubcommand"
    @CommandNames({"alias", "alias"})
    public void anotherSubcommand(CommandData data) {
        // Your code goes here
    }
}
```

Now we can register this command, let's go back to our Main class:

```java
public class Main {
    public static void main(String[] args) {
        Rimor rimor = new Rimor();
        rimor.registerCommands(
                new ExampleCommand()
                // More commands would go here
        );
    }
}
```

Lastly, we can execute our commands by using a `String[]`:

```java
public class Main {
    public static void main(String[] args) {
        Rimor rimor = new Rimor();
        rimor.registerCommands(
                new ExampleCommand()
                // More commands go here
        );

        // This will point to "examplecommand/anothersubcommand"
        String[] path = {"examplecommand", "anothersubcommand"};
        rimor.execute(path, new CommandData(new ImportantInformation()));
    }
}
```

## Credits

This project was made, imagined and documented by me. Supervision and additional help
by [Alberto Mimbrero Sánchez](github.com/inetAddress), many thanks to him.
