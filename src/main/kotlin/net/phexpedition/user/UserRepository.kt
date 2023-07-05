package net.phexpedition.user

import com.google.cloud.firestore.Firestore
import jakarta.enterprise.context.RequestScoped
import java.util.concurrent.TimeUnit

@RequestScoped
class UserRepository(val firestore: Firestore) {

    fun findUserByEmail(email: String): User {
        println("Finding user by eMail: $email")
        val collection = firestore.collection("user")
        val sn = collection.document(email).get().get(20, TimeUnit.SECONDS)
        println("sn: ")
        //val u = sn.toObject(User::class.java)
        return User(
            id = sn.get("id", String::class.java)!!,
            email = sn.get("email", String::class.java)!!,
            displayName = sn.get("displayName", String::class.java)!!
        )
    }
}