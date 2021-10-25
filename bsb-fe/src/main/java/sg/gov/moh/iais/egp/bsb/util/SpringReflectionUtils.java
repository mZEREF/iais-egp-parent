package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;


/** This is a very common class, may be moved to common module in the future */
@Component
public class SpringReflectionUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringReflectionUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static Object invokeBeanMethod(String componentName, String methodName, Object[] params) {
        Object bean = getBean(componentName);
        Class[] paramClass = null;
        if (params != null) {
            int len = params.length;
            paramClass = new Class[len];
            for (int i = 0; i < len; i++) {
                paramClass[i] = params[i].getClass();
            }
        }
        Method method = ReflectionUtils.findMethod(bean.getClass(), methodName, paramClass);
        Assert.notNull(method, "Can not find the method");
        return ReflectionUtils.invokeMethod(method, bean, params);
    }
}
