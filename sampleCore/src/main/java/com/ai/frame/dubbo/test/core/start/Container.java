package com.ai.frame.dubbo.test.core.start;

import java.io.File;

public interface Container {
	String NB_FRAME_CONFIG = "config/system.properties";
	String NB_FRAME_LIB    = "lib";
    /**
     * start.
     */
    void start();
    
    /**
     * stop.
     */
    void stop();
    
    /**取得应用的Home目录*/
    File getHome();
}
