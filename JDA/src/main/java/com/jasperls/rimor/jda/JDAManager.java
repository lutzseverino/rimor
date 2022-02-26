package com.jasperls.rimor.jda;

import com.jasperls.rimor.jda.event.SlashCommandListener;
import net.dv8tion.jda.api.JDA;

public class JDAManager {

    private final JDA jdaInstance;

    public JDAManager(JDA jdaInstance) {
        this.jdaInstance = jdaInstance;
    }

    public void startService() {
        this.jdaInstance.addEventListener(
                new SlashCommandListener()
        );
    }
}
