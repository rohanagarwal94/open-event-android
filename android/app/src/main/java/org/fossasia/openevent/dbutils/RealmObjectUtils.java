package org.fossasia.openevent.dbutils;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by rohanagarwal94 on 15/7/17.
 */
public class RealmObjectUtils {

    /**
     * Update a realmList from a realmResults.
     * The purpose is to enable realm data to make it into widgets which run
     * in a different thread.
     */
    public static <T extends RealmClonable<T>> void cloneToRealmList(RealmResults<T> realmResults, RealmList<T> realmList) {
        realmList.clear();

        for(T realmModel : realmResults) {
            realmList.add(realmModel.realmClone());
        }
    }
}
