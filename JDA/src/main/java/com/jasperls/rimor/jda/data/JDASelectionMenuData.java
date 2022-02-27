package com.jasperls.rimor.jda.data;

import com.jasperls.rimor.data.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

@Getter
@Setter
public class JDASelectionMenuData extends Data {

    private SelectionMenuEvent event;
}
