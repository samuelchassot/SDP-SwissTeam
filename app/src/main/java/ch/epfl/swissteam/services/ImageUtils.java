package ch.epfl.swissteam.services;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class contining utilities for manipulating images and bitmaps.
 *
 * @author Adrian Baudat
 */
public class ImageUtils {

    /**
     * Changes the orientation of a bitmap depending on its EXIF data so that it is upright.
     *
     * @param bitmap bitmap to rotate
     * @param image_absolute_path absolute path of the image with EXIT data
     * @return rotated bitmap
     * @throws IOException if the image could not be found at given path
     */
    public static Bitmap modifyOrientation(Bitmap bitmap, InputStream image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int baseOrientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (baseOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    /**
     * Rotates a bitmap a certain number of degrees.
     *
     * @param bitmap bitmap to rotate
     * @param degrees degrees to rotate bitmap
     * @return rotated bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Flips a bitmap around its vertical and horizontal axes.
     *
     * @param bitmap bitmap to flip
     * @param vertical whether bitmap should be flipped vertically
     * @param horizontal whether bitmap should be flipped horizontally
     * @return flipped bitmap
     */
    public static Bitmap flip(Bitmap bitmap, boolean vertical, boolean horizontal) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
