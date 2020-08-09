package com.itba.edu.ar;

import com.itba.edu.ar.config.StaticConfig;

public interface Configurable {

    public default StaticConfig getStaticConfig() {
        return App.getStaticConfig();
    }
}
