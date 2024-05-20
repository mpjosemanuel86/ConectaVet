package com.mpjosemanuel86.conectavet.utils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.model.Mascota;

import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {
    private FirebaseFirestore mFirestore;

    public FirestoreHelper() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void getAllMascotas(OnSuccessListener<List<Mascota>> onSuccessListener, OnFailureListener onFailureListener) {
        CollectionReference clientesRef = mFirestore.collection("cliente");
        clientesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Task<QuerySnapshot>> tasks = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                String clienteId = document.getId();
                Query mascotasQuery = document.getReference().collection("mascotas");
                tasks.add(mascotasQuery.get());
            }
            Task<List<QuerySnapshot>> combinedTask = Tasks.whenAllSuccess(tasks);
            combinedTask.addOnSuccessListener(querySnapshots -> {
                List<Mascota> todasLasMascotas = new ArrayList<>();
                for (QuerySnapshot snapshot : querySnapshots) {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        todasLasMascotas.add(mascota);
                    }
                }
                onSuccessListener.onSuccess(todasLasMascotas);
            }).addOnFailureListener(onFailureListener);
        }).addOnFailureListener(onFailureListener);
    }
}
