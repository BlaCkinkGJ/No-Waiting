package kr.ac.pusan.cs.nowating.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.pusan.cs.nowating.Activity_Map;
import kr.ac.pusan.cs.nowating.R;


public class MapFragment extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view=inflater.inflate(R.layout.fragment_map,null);
        TextView textView = view.findViewById(R.id.tt);
        textView.setText("Yes");
        Button btn=view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Activity_Map.class);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }
}
