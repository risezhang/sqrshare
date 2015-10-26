package sqrshare.shouleta.com.sqrshare;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import sqrshare.shouleta.com.sqrshare.sqrshare.shouleta.com.sqrshare.util.ImageUtil;

public class MainActivity extends AppCompatActivity {

    private Button pickPicBtn = null;
    private Button shrBtn = null;
    private Bitmap bmp = null;
    private ImageView imageView = null;
    private Integer IMAGE_NAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickPicBtn = (Button) this.findViewById(R.id.pickPicBtn);
        shrBtn = (Button) this.findViewById(R.id.shrBtn);
        pickPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去相册取图片
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        shrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去相册取图片
                IMAGE_NAME = 0;
                Bitmap pic = ImageUtil.getSquare(bmp);
                Bitmap[] arr = ImageUtil.getSquares(pic, 8);
                pic.recycle();
                ImageUtil.addText(arr[8], "\"一九分享\" 制作");
                shareToWX("haha", new File[]{saveImageToSdCard(arr[0])
                        , saveImageToSdCard(arr[1])
                        , saveImageToSdCard(arr[2])
                        , saveImageToSdCard(arr[3])
                        , saveImageToSdCard(arr[4])
                        , saveImageToSdCard(arr[5])
                        , saveImageToSdCard(arr[6])
                        , saveImageToSdCard(arr[7])
                        , saveImageToSdCard(arr[8])
                });
            }
        });

        imageView = (ImageView) this.findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //选择图片
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                    bmp.recycle();
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("the bmp toString: " + bmp);
//            imageSV.setBmp(bmp);
            int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
            imageView.setImageBitmap(scaled);

            imageView.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(MainActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
        }

    }

    private void shareToWX(String title, File[] files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", title);
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        startActivity(intent);
    }

    public File saveImageToSdCard(Bitmap bitmap) {
        boolean success = false;
        File file = null;
        try {
            file = createStableImageFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//           100 to keep full quality of the image
            outStream.flush();
            outStream.close();
            bitmap.recycle();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
//          Toast.makeText(getApplicationContext(), "Image saved with success",
//                  Toast.LENGTH_LONG).show();
            return file;
        } else {
            return null;
        }
    }

    public File createStableImageFile() throws IOException {
        IMAGE_NAME++;
        String imageFileName = Integer.toString(IMAGE_NAME) + ".jpg";
        File storageDir = this.getApplicationContext().getExternalCacheDir();

//        File image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",         /* suffix */
//            storageDir      /* directory */
//        );
        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
