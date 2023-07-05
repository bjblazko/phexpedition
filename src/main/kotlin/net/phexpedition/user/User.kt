package net.phexpedition.user

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.SetOptions
import com.google.cloud.firestore.v1.FirestoreClient
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.concurrent.TimeUnit

data class User(
    val id: String,
    val email: String,
    val displayName: String
)

fun userFromSecurityContext(ctx: SecurityContext): User {
    val jwt = ctx.userPrincipal as JsonWebToken

    return User(
        id = "123",//jwt.getClaim("id"),
        email = jwt.getClaim("email"),
        displayName = "${jwt.claim<String>("given_name").get()} ${jwt.claim<String>("family_name").get()}"
    )
}

fun store(user: User) {
    val firestore = FirestoreOptions.getDefaultInstance().getService()
    val collection = firestore.collection("user")
    val document = collection.document(user.email)
    val result = document.set(user, SetOptions.merge())
    result.get(10, TimeUnit.SECONDS)
}

fun findByEMail(email: String): User {
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