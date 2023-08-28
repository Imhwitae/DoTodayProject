package com.todolist.DoToday.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.todolist.DoToday.entity.Test;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseServiceImpl implements FirebaseService{

    public static final String COLLECTION_NAME = "test";
    Firestore db = FirestoreClient.getFirestore();

    @Override
    public String insert(Test test) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME).document(test.getId()).set(test);
        System.out.println("가입 완료");
        return apiFuture.get().getUpdateTime().toString();
    }


//   삽입 테스트용 메서드
    public void insert() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "park");
        data.put("id", "3");
        data.put("email", "qwe@gmail.com");
        data.put("pw", "1111");
        ApiFuture<DocumentReference> addedDocRef = db.collection("test").add(data);
//        return "Added document with ID: " + addedDocRef.get().getId();
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
