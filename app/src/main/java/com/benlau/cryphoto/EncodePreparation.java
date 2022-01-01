package com.benlau.cryphoto;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.benlau.cryphoto.model.ImageEncrypt;
import com.benlau.cryphoto.model.ImageViewModel;

import java.io.IOException;
import java.util.Objects;

public class EncodePreparation extends Fragment {
  private ImageView canvas;
  private ImageView secret;

  private Bitmap secretBitmap;
  private Bitmap canvasBitmap;
  private Bitmap resultBitmap;

  private ActivityResultLauncher<String> mGetCanvas;
  private ActivityResultLauncher<String> mGetSecret;

  private ImageViewModel encryptResultViewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mGetCanvas = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
              try {
                canvasBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                canvas.setImageBitmap(canvasBitmap);
              } catch (IOException e) {
                Toast.makeText(getActivity(), "exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
    );

    mGetSecret = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
              try {
                secretBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                secret.setImageBitmap(secretBitmap);
              } catch (Exception e) {
                Toast.makeText(getActivity(), "exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
    );
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_encode_preparation, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    //encryptResultViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

    canvas = view.findViewById(R.id.selectedCanvas);
    secret = view.findViewById(R.id.selectedSecret);

    view.findViewById(R.id.buttonSelectCanvas).setOnClickListener(v -> mGetCanvas.launch("image/*"));

    view.findViewById(R.id.buttonSelectSecret).setOnClickListener(v -> mGetSecret.launch("image/*"));

    view.findViewById(R.id.buttonStartEncode).setOnClickListener(v -> {
      if (canvasBitmap == null) {
        Toast.makeText(getActivity(), "Please select an image as canvas!", Toast.LENGTH_SHORT).show();
      } else if (secretBitmap == null) {
        Toast.makeText(getActivity(), "Please select a secret image!", Toast.LENGTH_SHORT).show();
      } else {
        encryptResultViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
        encryptResultViewModel.selectBitmap(new ImageEncrypt(canvasBitmap, secretBitmap).encryptingImg());
        Navigation.findNavController(view).navigate(R.id.action_encodePreparation_to_encodeResult);
      }
    });
  }
}