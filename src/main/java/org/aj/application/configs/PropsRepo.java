package org.aj.application.configs;

import org.aj.core.propertiesHandler.Mandatory;

public class PropsRepo {

    public class Android {

        @Mandatory
        protected String appName;

        @Mandatory
        protected String automationType;
    }
}
