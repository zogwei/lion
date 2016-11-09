/**
 * 版权声明：lion 版权所有 违者必究 2016
 * Copyright: Copyright (c) 2016 
 * 
 * @project_name: lion-rpc
 * @Title: AbstractConfig.java
 * @Package com.alacoder.lion.config
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:05
 * @version V1.0
 */

package com.alacoder.lion.config;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alacoder.common.exception.LionErrorMsgConstant;
import com.alacoder.common.exception.LionFrameworkException;
import com.alacoder.lion.common.LionConstants;
import com.alacoder.lion.common.utils.LoggerUtil;

/**
 * @ClassName: AbstractConfig
 * @Description: 
 * @author jimmy.zhong
 * @date 2016年10月19日 下午8:10:05
 *
 */

public abstract class AbstractConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * 按顺序进行config 参数append and override，按照configs出现的顺序，后面的会覆盖前面的相同名称的参数
     * 
     * @param parameters
     * @param configs
     */
    protected static void collectConfigParams(Map<String, String> parameters, AbstractConfig... configs) {
        for (AbstractConfig config : configs) {
            if (config != null) {
                config.appendConfigParams(parameters);
            }
        }
    }

    protected static void collectMethodConfigParams(Map<String, String> parameters, List<MethodConfig> methods) {
        if (methods == null || methods.isEmpty()) {
            return;
        }
        for (MethodConfig mc : methods) {
            if (mc != null) {
                mc.appendConfigParams(parameters, LionConstants.METHOD_CONFIG_PREFIX + mc.getName() + "(" + mc.getArgumentTypes() + ")");
            }
        }
    }

    protected void appendConfigParams(Map<String, String> parameters) {
        appendConfigParams(parameters, null);
    }
    
    /**
     * 将config 参数录入Map中
     * 
     * @param parameters
     */
    @SuppressWarnings("unchecked")
    protected void appendConfigParams(Map<String, String> parameters, String prefix) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            try {
                String name = method.getName();
                if (isConfigMethod(method)) {
                    int idx = name.startsWith("get") ? 3 : 2;
                    String prop = name.substring(idx, idx + 1).toLowerCase() + name.substring(idx + 1);
                    String key = prop;
                    ConfigDesc configDesc = method.getAnnotation(ConfigDesc.class);
                    if (configDesc != null && !StringUtils.isBlank(configDesc.key())) {
                        key = configDesc.key();
                    }

                    Object value = method.invoke(this);
                    if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                        if (configDesc != null && configDesc.required()) {
                            throw new LionFrameworkException(String.format("%s.%s should not be null or empty", this.getClass()
                                    .getSimpleName(), key), LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
                        }
                        continue;
                    }
                    if (prefix != null && prefix.length() > 0) {
                        key = prefix + "." + key;
                    }
                    parameters.put(key, String.valueOf(value).trim());
                } else if ("getParameters".equals(name) && Modifier.isPublic(method.getModifiers())
                        && method.getParameterTypes().length == 0 && method.getReturnType() == Map.class) {
                    Map<String, String> map = (Map<String, String>) method.invoke(this);
                    if (map != null && map.size() > 0) {
                        String pre = prefix != null && prefix.length() > 0 ? prefix + "." : "";
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            parameters.put(pre + entry.getKey(), entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                throw new LionFrameworkException(String.format("Error when append params for config: %s.%s", this.getClass()
                        .getSimpleName(), method.getName()), e, LionErrorMsgConstant.FRAMEWORK_INIT_ERROR);
            }
        }
    }

    private boolean isConfigMethod(Method method) {
        boolean checkMethod =
                (method.getName().startsWith("get") || method.getName().startsWith("is")) && !"isDefault".equals(method.getName())
                        && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
                        && isPrimitive(method.getReturnType());

        if (checkMethod) {
            ConfigDesc configDesc = method.getAnnotation(ConfigDesc.class);
            if (configDesc != null && configDesc.excluded()) {
                return false;
            }
        }
        return checkMethod;
    }

    private boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Character.class || type == Boolean.class || type == Byte.class
                || type == Short.class || type == Integer.class || type == Long.class || type == Float.class || type == Double.class;
    }

    @Override
    public String toString() {
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("<lion:");
            buf.append(getTagName(getClass()));
            Method[] methods = getClass().getMethods();
            for (Method method : methods) {
                try {
                    String name = method.getName();
                    if ((name.startsWith("get") || name.startsWith("is")) && !"getClass".equals(name) && !"get".equals(name)
                            && !"is".equals(name) && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
                            && isPrimitive(method.getReturnType())) {
                        int i = name.startsWith("get") ? 3 : 2;
                        String key = name.substring(i, i + 1).toLowerCase() + name.substring(i + 1);
                        Object value = method.invoke(this);
                        if (value != null) {
                            buf.append(" ");
                            buf.append(key);
                            buf.append("=\"");
                            buf.append(value);
                            buf.append("\"");
                        }
                    }
                } catch (Exception e) {
                    LoggerUtil.warn(e.getMessage(), e);
                }
            }
            buf.append(" />");
            return buf.toString();
        } catch (Throwable t) { // 防御性容错
            LoggerUtil.warn(t.getMessage(), t);
            return super.toString();
        }
    }

    private static final String[] SUFFIXS = new String[] {"Config", "Bean"};

    private static String getTagName(Class<?> cls) {
        String tag = cls.getSimpleName();
        for (String suffix : SUFFIXS) {
            if (tag.endsWith(suffix)) {
                tag = tag.substring(0, tag.length() - suffix.length());
                break;
            }
        }
        tag = tag.toLowerCase();
        return tag;
    }
}