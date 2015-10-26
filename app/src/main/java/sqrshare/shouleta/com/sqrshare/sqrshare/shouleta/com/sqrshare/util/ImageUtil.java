package sqrshare.shouleta.com.sqrshare.sqrshare.shouleta.com.sqrshare.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liang on 15/9/11.
 */
public class ImageUtil {

    public static Bitmap getSquare(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        boolean isWidthLonger = width > height;
        int dimensionSize = isWidthLonger ? height : width;
        int x = (isWidthLonger ? (width - dimensionSize) / 2 : 0);
        int y = (!isWidthLonger ? (height - dimensionSize) / 2 : 0);
        Bitmap sqrBitMap = Bitmap.createBitmap(bm,
                x,
                y,
                dimensionSize, dimensionSize
        );
        return sqrBitMap;
    }

    public static Bitmap[] getSquares(Bitmap bm, int margin) {
        List<Bitmap> list = new LinkedList<Bitmap>();
        bm = Bitmap.createScaledBitmap(bm, 1000, 1000, false);

        int size = bm.getWidth();
        int sqrSize = (size - margin * 2) / 3;
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                int x = j * margin + j*sqrSize;
                int y = i * margin + i*sqrSize;
                Bitmap piece = Bitmap.createBitmap(bm, x, y, sqrSize, sqrSize);
                list.add(piece);
            }
        }
        return list.toArray(new Bitmap[9]);
    }

    public static void addText(Bitmap bitmap, String text) {
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(255, 255, 255));
        // text size in pixels
        paint.setTextSize((int) (28 * 1));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int xx = bitmap.getWidth() - bounds.width() - 20;
        int yy = bitmap.getHeight() - 30;

        canvas.drawText(text, xx, yy, paint);
    }
}
