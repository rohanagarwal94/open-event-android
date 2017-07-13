package org.fossasia.openevent.dbutils;

import io.realm.RealmModel;

/**
 * Created by rohanagarwal94 on 15/7/17.
 */
public interface RealmClonable<T> extends RealmModel {
    T realmClone();
}