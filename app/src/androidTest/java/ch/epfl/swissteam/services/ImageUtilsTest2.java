package ch.epfl.swissteam.services;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ImageUtilsTest2 {
    Bitmap twoByTwo = Bitmap.createBitmap(new int[] {1, 2, 3, 4}, 2, 2, Bitmap.Config.ARGB_8888);

    @Test
    public void flipWorks() {
        Bitmap hFlip = ImageUtils.flip(twoByTwo, false, true);
        assertEquals("The top left pixel of the flip and the top right pixel of the original don't match.",
                twoByTwo.getPixel(1, 0), hFlip.getPixel(0, 0));
        assertEquals("The bottom left pixel of the flip and the bottom right pixel of the original don't match.",
                twoByTwo.getPixel(1, 1), hFlip.getPixel(0, 1));

        Bitmap vFlip = ImageUtils.flip(twoByTwo, true, false);
        assertEquals("The top left pixel of the flip and the bottom left pixel of the original don't match.",
                twoByTwo.getPixel(0, 1), vFlip.getPixel(0, 0));
        assertEquals("The top right pixel of the flip and the bottom right pixel of the original don't match.",
                twoByTwo.getPixel(1, 1), vFlip.getPixel(1, 0));

        Bitmap vhFlip = ImageUtils.flip(twoByTwo, true, true);
        assertEquals("The top left pixel of the flip and the bottom right pixel of the original don't match.",
                twoByTwo.getPixel(1, 1), vhFlip.getPixel(0, 0));
        assertEquals("The top right pixel of the flip and the bottom left pixel of the original don't match.",
                twoByTwo.getPixel(0, 1), vhFlip.getPixel(1, 0));
    }

    @Test
    public void rotateWorks() {
        Bitmap rotate90 = ImageUtils.rotate(twoByTwo, 90);
        assertEquals("The top left pixel of the rotated and the bottom left pixel of the original don't match",
                twoByTwo.getPixel(0, 1), rotate90.getPixel(0,0));

        Bitmap rotate180 = ImageUtils.rotate(twoByTwo, 180);
        assertEquals("The top left pixel of the rotated and the bottom right pixel of the original don't match",
                twoByTwo.getPixel(0, 1), rotate180.getPixel(1,1));

        Bitmap rotate270 = ImageUtils.rotate(twoByTwo, 270);
        assertEquals("The top left pixel of the rotated and the top right pixel of the original don't match",
                twoByTwo.getPixel(0, 1), rotate270.getPixel(1,0));

        Bitmap rotate360 = ImageUtils.rotate(twoByTwo, 360);
        assertEquals("The top left pixel of the rotated and the top left pixel of the original don't match",
                twoByTwo.getPixel(0, 0), rotate360.getPixel(0,0));
    }
}