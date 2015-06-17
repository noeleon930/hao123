package kddjavatoolchain.DataFormat;

import java.io.Serializable;
import java.util.function.UnaryOperator;

/**
 *
 * @author Noel
 */
public class Feature implements Serializable
{

    final private float RawValue;
    final private String Meaning;
    private float NormedValue;

    public Feature(float RawValue, String Meaning)
    {
        this.RawValue = RawValue;
        this.Meaning = Meaning;
        this.NormedValue = RawValue;
    }

    public void NormalizeValue(UnaryOperator<Float> fun)
    {
        this.NormedValue = fun.apply(this.RawValue);
    }

    public float getRawValue()
    {
        return RawValue;
    }

    public String getMeaning()
    {
        return Meaning;
    }

    public float getNormedValue()
    {
        return NormedValue;
    }

    public void setNormedValue(float NormedValue)
    {
        this.NormedValue = NormedValue;
    }

}
