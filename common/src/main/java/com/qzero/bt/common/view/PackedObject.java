package com.qzero.bt.common.view;

import java.util.Collection;

public interface PackedObject {

    void addObject(Object obj);

    void addObject(String specialName,Object obj);

    <T> T parseObject(Class<T> cls);

    <T> T parseObject(String specialName,Class<T> cls);

    Collection parseCollectionObject(Class collectionClass, Class... elementClasses);

    Collection parseCollectionObject(String specialName, Class collectionClass, Class... elementClasses);
}
