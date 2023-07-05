package net.phexpedition.user

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import java.util.*

class UserKtTest {

    @Test
    fun `test POJO firestore`() {
        val user = User(id = UUID.randomUUID().toString(), email = "foo@example.com", displayName = "John Doe")

        store(user)
    }

    @Test
    fun `by email`() {
        val u = findByEMail("foo@example.com")
        println(u)
    }
}