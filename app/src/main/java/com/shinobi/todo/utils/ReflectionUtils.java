/*
 * Copyright (c) 2017. SeneVideo
 *  All Rights Reserved
 *
 *  This product is protected by copyright and distributed under
 *  licenses restricting copying, distribution and decompilation.
 */

package com.shinobi.todo.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Thiependa SEYE AKA Thush Capone on 26/02/2017.
 * SeneVideos,
 * thiependa.seye@gmail.com
 */

/**
 * Contains all the reflection method helpers
 */
public abstract class ReflectionUtils {
    
    private ReflectionUtils() {
    }
    
    /**
     * Get the underlying class for a type, or null if the type is a variable type.
     *
     * @param type the type
     * @return the underlying class
     */
    public static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Get the actual type arguments a child class has used to extend a generic base class.
     *
     * @param baseClass  the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass,
                                                      Class<? extends T> childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class) parameterizedType.getRawType();
                
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }
                
                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }
        
        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }
    
    /**
     * Returns the first {@link Field} in the hierarchy for the specified name
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        return field;
    }
    
    /**
     * Returns the first {@link Field} in the hierarchy for the specified name
     */
    public static Field getField(String className, String fieldName) {
        try {
            return getField(Class.forName(className), fieldName);
        } catch (ClassNotFoundException ignored) {
            throw new RuntimeException("Can't find class: " + className);
        }
    }
    
    /**
     * Returns the list of fields of the class passed in parameters
     *
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }
    
    /**
     * Returns the list of fields wanted from the class passed in parameters
     *
     * @param clazz
     * @param typeWanted
     * @return
     */
    public static Field[] getFields(Class<?> clazz, Class<?> typeWanted) {
        List<Field> temp = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(typeWanted)) {
                temp.add(field);
            }
        }
        return temp.toArray(new Field[temp.size()]);
    }
    
    /**
     * Returns the object reflected
     *
     * @param field
     * @param object
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object object) {
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }
    
    
    public static void copyFields(Object source, Object target) {
        Field[] fieldsSource = source.getClass().getDeclaredFields();
        Field[] fieldsTarget = target.getClass().getDeclaredFields();
        
        for (Field fieldTarget : fieldsTarget) {
            for (Field fieldSource : fieldsSource) {
                if (fieldTarget.getName().equals(fieldSource.getName())) {
                    try {
                        fieldTarget.set(target, fieldSource.get(source));
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    break;
                }
            }
        }
    }
    
}
