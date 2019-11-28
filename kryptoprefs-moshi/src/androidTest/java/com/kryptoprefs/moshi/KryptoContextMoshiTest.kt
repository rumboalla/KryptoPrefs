package com.kryptoprefs.moshi

import android.content.Context
import android.support.test.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.kryptoprefs.invoke
import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoBuilder
import com.kryptoprefs.preferences.KryptoPrefs
import org.junit.Before
import org.junit.Test

class KryptoContextMoshiTest {

	private val context: Context = InstrumentationRegistry.getContext()
	private val prefsName = "prefs"
	lateinit var prefs: TestPreference

	@Before
	fun init() {
		context.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit().clear().apply()
		context.getSharedPreferences("$prefsName-hybrid", Context.MODE_PRIVATE).edit().clear().apply()
		prefs = TestPreference(KryptoBuilder.hybrid(context, prefsName))
	}

	@Test
	fun preference_json() {
		// jsonPref
		assertThat(prefs.jsonPref() == TestClass())
		prefs.jsonPref(TestClass(1, "1"))
		assertThat(prefs.jsonPref() == TestClass(1, "1"))

		// jsonPrefBacked
		assertThat(prefs.jsonPrefBacked() == TestClass())
		prefs.jsonPrefBacked(TestClass(1, "1"))
		assertThat(prefs.jsonPrefBacked() == TestClass(1, "1"))

		// nullJsonPref
		assertThat(prefs.nullJsonPref() == null)
		prefs.nullJsonPref(TestClass(1, "1"))
		assertThat(prefs.nullJsonPref() == TestClass(1, "1"))
		prefs.nullJsonPref(null)
		assertThat(prefs.nullJsonPref() == null)

		// nullJsonPrefBacked
		assertThat(prefs.nullJsonPrefBacked() == null)
		prefs.nullJsonPrefBacked(TestClass(1, "1"))
		assertThat(prefs.nullJsonPrefBacked() == TestClass(1, "1"))
		prefs.nullJsonPrefBacked(null)
		assertThat(prefs.nullJsonPrefBacked() == null)
	}

	@Test
	fun preference_list() {
		// listPref
		assertThat(prefs.listPref() == emptyList<TestClass>())
		prefs.listPref(listOf(TestClass(1, "1"), TestClass(2, "2")))
		assertThat(prefs.listPref() == listOf(TestClass(1, "1"), TestClass(2, "2")))

		// listPrefBacked
		assertThat(prefs.listPrefBacked() == emptyList<TestClass>())
		prefs.listPrefBacked(listOf(TestClass(1, "1"), TestClass(2, "2")))
		assertThat(prefs.listPrefBacked() == listOf(TestClass(1, "1"), TestClass(2, "2")))

		// nullListPref
		assertThat(prefs.nullListPref() == null)
		prefs.nullListPref(listOf(TestClass(1, "1"), TestClass(2, "2")))
		assertThat(prefs.nullListPref() == listOf(TestClass(1, "1"), TestClass(2, "2")))
		prefs.nullListPref(null)
		assertThat(prefs.nullListPref() == null)

		// nullListPrefBacked
		assertThat(prefs.nullListPrefBacked() == null)
		prefs.nullListPrefBacked(listOf(TestClass(1, "1"), TestClass(2, "2")))
		assertThat(prefs.nullListPrefBacked() == listOf(TestClass(1, "1"), TestClass(2, "2")))
		prefs.nullListPrefBacked(null)
		assertThat(prefs.nullListPrefBacked() == null)
	}
}

data class TestClass(val id: Int = 0, val text: String = "")

class TestPreference(prefs: KryptoPrefs) : KryptoContext(prefs) {

	val jsonPref = json("jsonPref", TestClass())
	val jsonPrefBacked = json("jsonPrefBacked", TestClass(), true)
	val nullJsonPref = json<TestClass>("nullJsonPref", null)
	val nullJsonPrefBacked = json<TestClass>("nullJsonPrefBacked", null)

	val listPref = json("listPref", emptyList<TestClass>())
	val listPrefBacked = json("listPrefBacked", emptyList<TestClass>(), true)
	val nullListPref = json<List<TestClass>>("nullListPref", null)
	val nullListPrefBacked = json<List<TestClass>>("nullListPrefBacked", null)
}