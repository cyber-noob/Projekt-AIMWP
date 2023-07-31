package org.aj.application.configs;

import org.aj.core.propertiesHandler.Mandatory;

public class PropsRepo {

    public static class Common {
        @Mandatory
        public String automationType = "android";

        @Mandatory
        public String groups;
    }

    public static class Android {

        @Mandatory
        public String appName;
    }
}
