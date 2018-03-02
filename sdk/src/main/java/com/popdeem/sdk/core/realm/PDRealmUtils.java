/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Popdeem
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.popdeem.sdk.core.realm;

import android.content.Context;

import com.popdeem.sdk.core.model.PDUser;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by mikenolan on 18/02/16.
 */
public class PDRealmUtils {

    private static final int REALM_SCHEMA_VERSION = 9;

    public static void initRealmDB(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name("popdeemrealm.realm")
                .modules(new PDRealmModule())
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(REALM_MIGRATION)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.compactRealm(realmConfiguration);
    }

    private static final RealmMigration REALM_MIGRATION = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            //Update for migrating user to allow advocacy score.
            RealmSchema schema = realm.getSchema();


            if(oldVersion < REALM_SCHEMA_VERSION){
                RealmObjectSchema userSchema = schema.get("PDRealmUserDetails");
                if(userSchema!=null){
                    if(userSchema.hasField("advocacyScore")) {
                        userSchema.renameField("advocacyScore", "advocacy_score");
                    }
                    if(!userSchema.hasField("advocacy_score")) {
                        userSchema.addField("advocacy_score", float.class, null);
                    }
                    if(userSchema.hasField("advocacyScore")) {
                        userSchema.removeField("advocacyScore");
                    }
                    if(!schema.contains("PDRealmCustomer")){
                        schema.create("PDRealmCustomer")
                                .addField(PDRealmCustomer.NAME, String.class, null)
                                .addField(PDRealmCustomer.FB_APP_ID, String.class, null)
                                .addField(PDRealmCustomer.FB_APP_ACCESS_TOKEN, String.class, null)
                                .addField(PDRealmCustomer.FACEBOOK_NAMESPACE, String.class, null)
                                .addField(PDRealmCustomer.TWITTER_CONSUMER_KEY, String.class, null)
                                .addField(PDRealmCustomer.TWITTER_CONSUMER_SECRET, String.class, null)
                                .addField(PDRealmCustomer.TWITTER_HANDLE, String.class, null)
                                .addField(PDRealmCustomer.INSTAGRAM_CLIENT_ID, String.class, null)
                                .addField(PDRealmCustomer.INSTAGRAM_CLIENT_SECRET, String.class, null)
                                .addField(PDRealmCustomer.COUNTDOWN_TIMER, int.class, null)
                                .addField(PDRealmCustomer.INCREMENT_ADVOCACY_POINTS, int.class, null)
                                .addField(PDRealmCustomer.DECREMENT_ADVOCACY_POINTS, int.class, null);
                    }

                }
                oldVersion = REALM_SCHEMA_VERSION;
            }
        }
    };

}
