package com.projetofut.demo.util

import com.google.firebase.database.*
import java.util.concurrent.CompletableFuture

object FirebaseDatabaseHelper {

    fun <T> getValue(ref: DatabaseReference, clazz: Class<T>): CompletableFuture<T?> {
        val future = CompletableFuture<T?>()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(clazz)
                future.complete(value)
            }

            override fun onCancelled(error: DatabaseError) {
                future.completeExceptionally(error.toException())
            }
        })

        return future
    }

    fun <T> getChildrenAsMap(ref: DatabaseReference, clazz: Class<T>): CompletableFuture<Map<String, T>> {
        val future = CompletableFuture<Map<String, T>>()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableMapOf<String, T>()
                for (child in snapshot.children) {
                    val value = child.getValue(clazz)
                    if (value != null && child.key != null) {
                        result[child.key!!] = value
                    }
                }
                future.complete(result)
            }

            override fun onCancelled(error: DatabaseError) {
                future.completeExceptionally(error.toException())
            }
        })

        return future
    }
}
