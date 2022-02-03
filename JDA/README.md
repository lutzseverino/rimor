# Rimor JDA

## Introduction

Rimor JDA is an extended version of Rimor Core, it introduces new features for users using this library for Discord bots
using the [JDA library](https://github.com/DV8FromTheWorld/JDA).

## Features

Rimor JDA includes all the features provided in Rimor Core, even if these don't follow Discord's functionality.

### Run code at option selection

Options are treated as methods, which means code can be run when someone fires a command with a specific option.

```java
public class Command extends OptionSubcommand {
    private String option;

    // Command methods live here 

    @OptionDetails(
            type = OptionType.STRING,
            position = 1
    )
    public void option(OptionExecutionData optionData) {
        this.option = optionData.getOptionMapping().getAsString();
    }

}
```

### Automatic command listening

Rimor automatically listens and executes commands if the `JDAManager#startService()` method is called.

```java
import com.jasperls.rimor.jda.JDAManager;

public class Main {
    public static void main(String[] args) {

        // jdaInstance gets created here

        JDAManager jdaManager = new JDAManager(jdaInstance);
        jdaManager.startService();
    }
}
```

## Usage
TODO