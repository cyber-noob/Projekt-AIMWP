package org.aj.core.preTestInits;

import org.aj.core.Exceptions.DtypeException;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyDesiredCapabilities {

    public static DesiredCapabilities setCapability(DesiredCapabilities capabilities, Enum<? extends Capabilities> capability, Object value) throws DtypeException {
        if(Capabilities.getDtype(capability).equals(value.getClass()))
            capabilities.setCapability(capability.toString(), value);
        else
            throw new DtypeException("Provided capability doesn't match the expected dtype: Provided capability -> " + capability + " expected dtype -> " + Capabilities.getDtype(capability));
        return capabilities;
    }
}
