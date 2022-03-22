package com.jasperls.rimor.jda.data;

import com.jasperls.rimor.data.Data;
import lombok.Getter;
import net.dv8tion.jda.api.events.Event;

@Getter
public class JDAEventData<T extends Event> extends Data {

    private final T event;

    public JDAEventData(T event) {
        this.event = event;
    }
}
