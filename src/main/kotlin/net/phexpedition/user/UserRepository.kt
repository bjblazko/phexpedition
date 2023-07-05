package net.phexpedition.user

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.TimeUnit

@ApplicationScoped
class UserRepository() {

    @WithSpan
    fun findUserByEmail(email: String): User {
        val firestore = FirestoreOptions.getDefaultInstance().getService()
        val collection = firestore.collection("user")
        val sn = collection.document(email).get().get(10, TimeUnit.SECONDS)
        //val u = sn.toObject(User::class.java)
        return User(
            id = sn.get("id", String::class.java)!!,
            email = sn.get("email", String::class.java)!!,
            displayName = sn.get("displayName", String::class.java)!!
        )
    }
}