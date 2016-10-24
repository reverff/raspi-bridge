package com.reverff.hap.util;

import com.reverff.hap.model.HapBridgeInfoBean;
import com.reverff.hap.model.WindowCoveringInfoBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by mgavlovy on 24.10.2016.
 *
 * @author Maksym Gavlovych (reverff@gmail.com)
 */
public class SpringBeanUtils {
    private static SpringBeanUtils instance;
    private ApplicationContext context;

    private SpringBeanUtils() {
        this.context = new ClassPathXmlApplicationContext("spring-config.xml");
    }

    public static SpringBeanUtils getInstance() {
        instance = instance == null ? new SpringBeanUtils() : instance;
        return instance;
    }

    public HapBridgeInfoBean getHapBridgeBean() {
        return context.getBean("homeKitBridge", HapBridgeInfoBean.class);
    }

    public WindowCoveringInfoBean getWindowCoveringInfoBean() {
        return context.getBean("windowCovering", WindowCoveringInfoBean.class);
    }

}
