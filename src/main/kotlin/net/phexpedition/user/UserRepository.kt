package net.phexpedition.user

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.FirestoreOptions
import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.enterprise.context.ApplicationScoped
import org.jboss.logmanager.Logger
import java.util.concurrent.TimeUnit

@ApplicationScoped
class UserRepository() {

    val userCollection = "user"

    private val log = Logger.getLogger(this::class.qualifiedName)

    @WithSpan(value = "firestorelookup")
    fun findUserByEmail(email: String): User {
        log.info("Looking up user $email...")
        val documentSnapshot = getDocumentSnapshot(userCollection, email)

        val foundUser = User(
            id = documentSnapshot.get("id", String::class.java)!!,
            email = documentSnapshot.get("email", String::class.java)!!,
            displayName = documentSnapshot.get("displayName", String::class.java)!!,
            permissions = documentSnapshot.get("permissions") as? List<String> ?: emptyList()
        )

        log.info("Found '${foundUser.displayName}' for '$email'")
        return foundUser
    }

    private fun getDocumentSnapshot(collection: String, documentKey: String): DocumentSnapshot {
        try {
            return FirestoreOptions
                .getDefaultInstance().service
                .collection(collection)
                .document(documentKey).get().get(10, TimeUnit.SECONDS)
        } catch (ex: Exception) {
            throw IllegalArgumentException("Failed to retrieve a user from datase for email '$documentKey'", ex)
        }
    }

}

/*
fun store(user: User) {
    val firestore = FirestoreOptions.getDefaultInstance().service
    val collection = firestore.collection("user")
    val document = collection.document(user.email)
    val result = document.set(user, SetOptions.merge())
    result.get(10, TimeUnit.SECONDS)
}*/
