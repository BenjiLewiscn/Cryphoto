package com.benlau.cryphoto.model;

import android.graphics.Bitmap;

public class ImageEncrypt {
  private Bitmap topImg;
  private Bitmap secretImg;

  public ImageEncrypt(Bitmap top, Bitmap secret) {
    topImg = top;
    secretImg = secret;
  }

  public ImageEncrypt(Bitmap top) {
    topImg = top.copy(Bitmap.Config.ARGB_8888, true);
  }

  public Bitmap getTopImg() {
    return topImg;
  }

  public Bitmap getSecretImg() {
    return secretImg;
  }

  public void setTopImg(Bitmap newTopImg) {
    topImg = newTopImg;
  }

  public void setSecretImg(Bitmap newSecretImg) {
    secretImg = newSecretImg;
  }

  private void preprocessing() {
    float ratio;
    if (topImg.getHeight() / (2.0 * secretImg.getHeight())
            > topImg.getWidth() / (2.0 * secretImg.getWidth())) {
      ratio = (float) (topImg.getWidth() / (2.0 * secretImg.getWidth()));
    } else {
      ratio = (float) (topImg.getHeight() / (2.0 * secretImg.getHeight()));
    }

    if (ratio < 1) {
      secretImg = Bitmap.createScaledBitmap(secretImg,
              (int) (secretImg.getWidth() * ratio),
              (int) (secretImg.getHeight() * ratio),
              true);
    }
  }

  private static int encoding(int canvas, int message) {
    int result = canvas & ((0xfc << 24) | (0xfc << 16) | (0xfc << 8) | 0xfc);
    result |= ((message >> 6) << 24)
              | (((message & 0x30) >> 4) << 16)
              | (((message & 0x0c) >> 2) << 8)
              | (message & 0x03);
    return result;
  }

  public Bitmap encryptingImg() {
    preprocessing();
    Bitmap result = topImg.copy(Bitmap.Config.ARGB_8888, true);
    secretImg.reconfigure(secretImg.getWidth(), secretImg.getHeight(), Bitmap.Config.ARGB_8888);

    for (int x = 0; x < secretImg.getWidth(); x++) {
      for (int y = 0; y < secretImg.getHeight(); y++) {
        int color = secretImg.getPixel(x, y);
        int A = 0xff & (color >> 24);
        int B = 0xff & (color >> 16);
        int G = 0xff & (color >> 8);
        int R = 0xff & color;

        result.setPixel(2 * x,2 * y, encoding(topImg.getPixel(2 * x, 2 * y), A));
        result.setPixel(2 * x + 1,2 * y, encoding(topImg.getPixel(2 * x + 1, 2 * y), B));
        result.setPixel(2 * x,2 * y + 1, encoding(topImg.getPixel(2 * x, 2 * y + 1), G));
        result.setPixel(2 * x + 1,2 * y + 1, encoding(topImg.getPixel(2 * x + 1, 2 * y + 1), R));
      }
    }

    return result;
  }

  private static int decoding(int canvas) {
    int p1 = (canvas & (3 << 24)) >> 24;
    int p2 = (canvas & (3 << 16)) >> 16;
    int p3 = (canvas & (3 << 8)) >> 8;
    int p4 = canvas & 3;
    return (p1 << 6) | (p2 << 4) | (p3 << 2) | p4;
  }

  public Bitmap decryptingImg() {
    topImg.reconfigure(topImg.getWidth(), topImg.getHeight(), Bitmap.Config.ARGB_8888);
    secretImg = Bitmap.createScaledBitmap(topImg,
            (int) (topImg.getWidth() / 2),
            (int) (topImg.getHeight() / 2),
            true);

    for (int x = 0; x < secretImg.getWidth(); x++) {
      for (int y = 0; y < secretImg.getHeight(); y++) {
        int A = decoding(topImg.getPixel(2 * x,2 * y));
        int B = decoding(topImg.getPixel(2 * x + 1,2 * y));
        int G = decoding(topImg.getPixel(2 * x,2 * y + 1));
        int R = decoding(topImg.getPixel(2 * x + 1,2 * y + 1));
        secretImg.setPixel(x, y,
                (A & 0xff) << 24 | (B & 0xff) << 16 | (G & 0xff) << 8 | (R & 0xff));
      }
    }

    return secretImg.copy(Bitmap.Config.ARGB_8888, true);
  }
}
