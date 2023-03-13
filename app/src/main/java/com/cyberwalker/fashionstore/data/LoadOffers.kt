package com.cyberwalker.fashionstore.data

import android.util.Log
import com.cyberwalker.fashionstore.circle.CircleOffers
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun loadOffers(){
    val data =  mutableListOf( CircleOffers("Hair car", "https://target.scene7.com/is/image/Target/prod_offer_386526_000fb4a2-9228-344a-b0a0-d0461b194110?wid=320&hei=320", "15% off", "Expires Mar 21, 2023", false),
        CircleOffers("Bedding", "https://target.scene7.com/is/image/Target/prod_offer_386552_d7f26f1d-60f6-399b-8eef-b8d974c16fac?wid=320&hei=320", "17% off", "Expires Mar 21, 2023", true),
        CircleOffers("Sun care", "https://target.scene7.com/is/image/Target/prod_offer_386525_86f42818-aa4f-3dfc-9bf2-19d223d05a87?wid=320&hei=320", "$12.99", "Expires Mar 21, 2023",false),
        CircleOffers("Outdoor furniture", "https://target.scene7.com/is/image/Target/prod_offer_386545_ed252eb2-f7ca-35e7-bea4-88ec607a6c57?wid=320&hei=320", "$11.99", "Expires Mar 21, 2023",true)
    )

    data.forEach {
        val user = hashMapOf(
            "itemName" to it.itemName,
            "image" to it.image,
            "discount" to it.discount,
            "expirationDate" to it.expirationDate
        )
        // Add a new document with a generated ID
        Firebase.firestore.collection("offers")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("loadOffers", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("loadOffers", "Error adding document", e)
            }
    }
}