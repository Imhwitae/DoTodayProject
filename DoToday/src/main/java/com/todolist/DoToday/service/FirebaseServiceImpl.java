package com.todolist.DoToday.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.todo.todolist.dto.response.TestDto;
import com.todo.todolist.entity.Test;

public class FirebaseServiceImpl implements FirebaseService{

    public static final String COLLECTION_NAME = "Test";
    @Override
    public String insert(TestDto testDto) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME).document(testDto.getId()).set(member);
        return apiFuture.get().getUpdateTime().toString();
    }

    @Override
    public Test getDetail(String id) throws Exception {
        return null;
    }

    @Override
    public String update(TestDto testDto) throws Exception {
        return null;
    }

    @Override
    public String delete(String id) throws Exception {
        return null;
    }
}
