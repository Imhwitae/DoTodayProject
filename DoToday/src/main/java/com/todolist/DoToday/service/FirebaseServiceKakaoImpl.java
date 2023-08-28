package com.todolist.DoToday.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.entity.Test;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceKakaoImpl implements FirebaseServiceKakao{

    public static final String COLLECTION_NAME = "members";
    Firestore db = FirestoreClient.getFirestore();

    @Override
    public String insert(Members members) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME).document(members.getEmail()).set(members);
        System.out.println("가입 완료");
        return apiFuture.get().getUpdateTime().toString();
    }

    @Override
    public Members getDetail(String memberEmail) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(memberEmail);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        Members members = null;
        if(documentSnapshot.exists()){
            members = documentSnapshot.toObject(Members.class);
            return members;
        }
        else{
            return null;
        }
    }

    @Override
    public String update(Members members) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture
                = firestore.collection(COLLECTION_NAME).document(members.getEmail()).set(members);
        return apiFuture.get().getUpdateTime().toString();
    }

    @Override
    public String delete(String memberEmail) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture
                = firestore.collection(COLLECTION_NAME).document(memberEmail).delete();
        return "Document id: "+memberEmail+" delete";
    }
}
