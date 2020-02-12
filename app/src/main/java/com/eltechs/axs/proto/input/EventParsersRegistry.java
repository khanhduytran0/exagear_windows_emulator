package com.eltechs.axs.proto.input;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.annotations.impl.EventParser;

public class EventParsersRegistry {
    private EventParser[] eventParsers = new EventParser[0];

    public void installEventParser(int i, EventParser eventParser) {
        if (this.eventParsers.length <= i) {
            EventParser[] eventParserArr = new EventParser[(i + 1)];
            System.arraycopy(this.eventParsers, 0, eventParserArr, 0, this.eventParsers.length);
            this.eventParsers = eventParserArr;
        }
        Assert.state(this.eventParsers[i] == null, String.format("A handler for the opcode %d is already registered.", new Object[]{Integer.valueOf(i)}));
        this.eventParsers[i] = eventParser;
    }

    public EventParser getParser(int i) {
        if (i < this.eventParsers.length) {
            return this.eventParsers[i];
        }
        return null;
    }
}
