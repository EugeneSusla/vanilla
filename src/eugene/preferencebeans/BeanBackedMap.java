package eugene.preferencebeans;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanBackedMap extends AbstractMap<String, Object> {
    private Object bean;
    private Set<String> excludedGetters;

    public BeanBackedMap(Object bean) {
        this(bean, null);
    }

    public BeanBackedMap(Object bean, Set<String> excludedGetters) {
        super();
        this.bean = bean;
        this.excludedGetters = excludedGetters;
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getType(String key) {
        try {
            //TODO support isXxx()
            return bean.getClass().getMethod("get" + key).getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> result = new HashSet<Entry<String, Object>>();
        for (final Method getter : bean.getClass().getMethods()) {
            final String getterName = getter.getName();
            if ((getterName.startsWith("get") || getterName.startsWith("is")) &&
                    getter.getParameterTypes().length == 0 &&
                    !getterName.equals("getClass") &&
                    !getterName.equals("getBean") &&
                    (excludedGetters == null || !excludedGetters.contains(getterName))) {
                getter.setAccessible(true);
                final String name = getterName.replaceAll("^get", "").replaceAll("^is", "");
                result.add(new TypeAwareEntry(name, getter));
            }
        }
        return result;
    }

    private class TypeAwareEntry implements Map.Entry<String, Object> {
        private String name;
        private Method getter;

        private TypeAwareEntry(String name, Method getter) {
            this.name = name;
            this.getter = getter;
        }

        public Class<?> getType() {
            return getter.getReturnType();
        }

        @Override
        public String getKey() {
            return name;
        }

        @Override
        public Object getValue() {
            try {
                return getter.invoke(bean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object setValue(Object object) {
            Object oldValue = getValue();
            String setterName = "set" + name;
            try {
                Method setter = bean.getClass()
                        .getMethod(setterName, getter.getReturnType());
                setter.setAccessible(true);
                setter.invoke(bean, object);
                return oldValue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
