package kddjavatoolchain.DataFormat;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author Noel
 */
public class Event implements Serializable
{

    private final Instant time;
    private final String source;
    private final String event;
    private final String object;

    public Event(String log)
    {
        this.time = Instant.parse(log.split(",")[0] + "Z");
        this.source = log.split(",")[1];
        this.event = log.split(",")[2];
        this.object = log.split(",")[3];
    }

    public Instant getTime()
    {
        return time;
    }

    public String getSource()
    {
        return source;
    }

    public String getEvent()
    {
        return event;
    }

    public String getObject()
    {
        return object;
    }

}
