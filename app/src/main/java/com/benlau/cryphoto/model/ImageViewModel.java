package com.benlau.cryphoto.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {
  private final MutableLiveData<Bitmap> selectedBitmap = new MutableLiveData<>();

  public void selectBitmap(@NonNull Bitmap bitmap) {
    selectedBitmap.setValue(bitmap);
  }

  @NonNull
  public LiveData<Bitmap> getSelectedItem() {
    return selectedBitmap;
  }
}
