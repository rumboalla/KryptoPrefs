package com.kryptoprefs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.kryptoprefs.context.KryptoContext;
import com.kryptoprefs.context.preference.NullablePreference;
import com.kryptoprefs.context.preference.Preference;
import com.kryptoprefs.preferences.KryptoBuilder;
import com.kryptoprefs.preferences.KryptoPrefs;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Small test to check Java compatibility.
 */
public class KryptoContextTestJava {

    @Test
    public void preference_test_java() {
        Context context = InstrumentationRegistry.getContext();
        context.getSharedPreferences("file", Context.MODE_PRIVATE).edit().clear().apply();
        TestPreferenceJava prefs = new TestPreferenceJava(KryptoBuilder.pref(context, "file", "TestPass", "TestSalt", 10));

        assertThat(prefs.intPref.get()).isEqualTo(0);
        prefs.intPref.put(1);
        assertThat(prefs.intPref.get()).isEqualTo(1);

        assertThat(prefs.intPrefBacked.get()).isEqualTo(0);
        prefs.intPrefBacked.put(1);
        assertThat(prefs.intPrefBacked.get()).isEqualTo(1);

        assertThat(prefs.stringPref.get()).isEqualTo(null);
        prefs.stringPref.put("42");
        assertThat(prefs.stringPref.get()).isEqualTo("42");

        assertThat(prefs.stringPrefBacked.get()).isEqualTo(null);
        prefs.stringPrefBacked.put("42");
        assertThat(prefs.stringPrefBacked.get()).isEqualTo("42");
    }

}

class TestPreferenceJava extends KryptoContext {

    TestPreferenceJava(KryptoPrefs prefs) {
        super(prefs);
    }

    public Preference<Integer> intPref = this.intPref("int", 0);
    public Preference<Integer> intPrefBacked = this.intPref("intBacked", 0, true);
    public NullablePreference<String> stringPref = this.nullStringPref("string", null, false);
    public NullablePreference<String> stringPrefBacked = this.nullStringPref("stringBacked", null, true);

}
