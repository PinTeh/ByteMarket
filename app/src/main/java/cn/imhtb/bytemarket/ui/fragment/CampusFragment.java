package cn.imhtb.bytemarket.ui.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.imhtb.bytemarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFragment extends Fragment {

    private Context context;

    public CampusFragment() {
        // Required empty public constructor
        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }


}
