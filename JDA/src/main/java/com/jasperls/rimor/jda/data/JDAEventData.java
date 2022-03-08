package com.jasperls.rimor.jda.data;

import com.jasperls.rimor.data.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.Event;

@Getter
@Setter
public class JDAEventData<T extends Event> extends Data {

    private T event;
}
