package com.houxj.generaltools.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 侯晓戬 on 2018/7/9.
 * 泛型List处理
 */

class JParameterizedTypeImpl implements ParameterizedType {
    private final Class mClazz;

    JParameterizedTypeImpl(Class mClazz) {
        this.mClazz = mClazz;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{mClazz};
    }

    @Override
    public Type getRawType() {
        return List.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
