package com.todolist.DoToday.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.todolist.DoToday.entity.Test;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceImpl implements FirebaseService{

    public static final String COLLECTION_NAME = "test";
    @Override
    public String insert(Test test) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME).document(test.getId()).set(test);
        return apiFuture.get().getUpdateTime().toString();
    }

    @Override
    public Test getDetail(String id) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        Test t = null;
        if(documentSnapshot.exists()){
            t = documentSnapshot.toObject(Test.class);
            return t;
        }
        else{
            return null;
        }
    }

    @Override
    public String update(Test test) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> apiFuture
                = firestore.collection(COLLECTION_NAME).document(test.getId()).set(test);
        return apiFuture.get().getUpdateTime().toString();
    }

    @Override
    public String delete(String id) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture
                = firestore.collection(COLLECTION_NAME).document(id).delete();
        return "Document id: "+id+" delete";
    }
}
