package net.phexpedition

import org.junit.Assert.assertNotEquals
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConfigurationTest {

    @Test
    fun `get value for existing ENV`() {
        val item = getStringConfigurationFromEnvironment(key = "HOME", defaultValue = Optional.of("foo"))
        assertTrue(item.isSuccess)
        assertEquals("HOME", item.getOrNull()!!.key)
        assertNotEquals("foo", item.getOrNull()!!.value)
    }


    @Test
    fun `given default value for non-existing ENV we want to receive the default`() {
        val item = getStringConfigurationFromEnvironment(key = "TEST_NON_EXISTING_ENV", defaultValue = Optional.of("foo"))
        assertTrue(item.isSuccess)
        assertEquals("TEST_NON_EXISTING_ENV", item.getOrNull()!!.key)
        assertEquals("foo", item.getOrNull()!!.value)
    }

    @Test
    fun `mandatory item with no default should fail for non-existing ENV`() {
        val item = getStringConfigurationFromEnvironment(key = "TEST_NON_EXISTING_ENV", defaultValue = Optional.empty())
        assertTrue(item.isFailure)
        assertEquals(null, item.getOrNull())
    }

}