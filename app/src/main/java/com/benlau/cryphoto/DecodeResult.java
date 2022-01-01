package com.benlau.cryphoto;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.benlau.cryphoto.model.ImageViewModel;

import java.io.IOException;
import java.io.OutputStream;

public class DecodeResult extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_decode_result, container, false);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ImageViewModel imageViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);
    Bitmap decryptedImage = imageViewModel.getSelectedItem().getValue();
    ((ImageView) view.findViewById(R.id.decryptedResult)).setImageBitmap(decryptedImage);
    try {
      SaveImage(decryptedImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void SaveImage(Bitmap finalBitmap) throws IOException {
    OutputStream fos;
    ContentResolver resolver = getActivity().getContentResolver();
    ContentValues contentValues = new ContentValues();
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "decrypted_result_" + System.currentTimeMillis() + ".jpg");
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/cryphoto/saved_images");
    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    fos = resolver.openOutputStream(imageUri);
    finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
    fos.flush();
    fos.close();
  }
}