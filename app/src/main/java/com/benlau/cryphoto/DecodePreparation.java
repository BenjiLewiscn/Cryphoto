package com.benlau.cryphoto;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.benlau.cryphoto.model.ImageEncrypt;
import com.benlau.cryphoto.model.ImageViewModel;

import java.io.IOException;
import java.util.Objects;

public class DecodePreparation extends Fragment {

  private ActivityResultLauncher<String> mGetMessage;
  private ImageView message;
  private Bitmap messageBitmap;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mGetMessage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
              try {
                messageBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                message.setImageBitmap(messageBitmap);
              } catch (IOException e) {
                Toast.makeText(getActivity(), "exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
    );
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
   return inflater.inflate(R.layout.fragment_decode_preparation, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    message = view.findViewById(R.id.selectedDecode);

    view.findViewById(R.id.buttonSelectDecode).setOnClickListener(v -> mGetMessage.launch("image/*"));

    view.findViewById(R.id.buttonStartDecode).setOnClickListener(v -> {
      if (messageBitmap == null) {
        Toast.makeText(getActivity(), "Please select an image to decode!", Toast.LENGTH_SHORT).show();
      } else {
        ImageViewModel decryptResultViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
        decryptResultViewModel.selectBitmap(new ImageEncrypt(messageBitmap).decryptingImg());
        Navigation.findNavController(view).navigate(R.id.action_decodePreparation_to_decodeResult);
      }
    });
  }
}