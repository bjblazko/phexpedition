package net.phexpedition.user

import java.util.*

class UserKtTest {

    //@Test
    fun `test POJO firestore`() {
        val user = User(id = UUID.randomUUID().toString(), email = "foo@example.com", displayName = "John Doe")

        //store(user)
    }

}