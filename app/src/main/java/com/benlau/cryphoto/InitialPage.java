package com.benlau.cryphoto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InitialPage extends Fragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_initial_page, container, false);

    view.findViewById(R.id.buttonEncode).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_initialPage_to_encodePreparation));

    view.findViewById(R.id.buttonDecode).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_initialPage_to_decodePreparation));

    return view;
  }
}