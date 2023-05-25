package com.example.diplomka_java;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class like_comment_video1_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_like_comment_video1_, container, false);
        LoadingDialog dialog=new LoadingDialog(getActivity());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView like_sany_textView=view.findViewById(R.id.like_comment_sany_textView_video1_fragment);
        String holder_dateTime="";
        db.collection("Пікір уақыты").document("қазіргі пікір уақыты").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                String datetime=documentSnapshot.getData().get("уақыт").toString();
                Toast.makeText(getActivity(), "dateTime:"+datetime, Toast.LENGTH_SHORT).show();
                db.collection("Пікірлер").document(datetime).collection("like-dislike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int like_sany=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String like = document.getData().get("like саны").toString();
                                like_sany+=Integer.parseInt(like);
                            }
                            like_sany_textView.setText(String.valueOf(like_sany));

                        }
                        else {
                            Toast.makeText(getActivity(), "Somethin went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        db.collection("Ақпарат").document("Қазіргі қолданушы").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String username=document.getData().get("Қолданушы есімі").toString();
                db.collection("Пікір уақыты").document("қазіргі пікір уақыты").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot=task.getResult();
                        String datetime=documentSnapshot.getData().get("уақыт").toString();
                        db.collection("Пікірлер").document(datetime).collection("like-dislike").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                String like=document.getData().get("like саны").toString();
                                if(like.equals("1")) {
                                    like_sany_textView.setTextColor(getResources().getColor(R.color.colour_green));
                                }
                            }
                        });
                    }
                });
            }
        });
        return view;
    }
}