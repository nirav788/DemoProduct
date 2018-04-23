package app.demoproduct.com.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import app.demoproduct.com.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.Models.LoginResponce;
import app.demoproduct.com.R;
import app.demoproduct.com.Utills.AppPreference;
import app.demoproduct.com.Utills.Helper;
import app.demoproduct.com.Utills.ServiceGeneratorr;
import app.demoproduct.com.interfaces.ApiInterface;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddNewItemActivity";
    AppPreference mAppPreference;
    ImageView iv_uploadpic, img_select;
    EditText edt_productname, edt_description;
    Spinner sp_branch, sp_qty;
    LinearLayout ll_cancel, ll_save;
    private int REQUEST_CAMERA = 001;
    private static final int SELECT_FILE = 2;
    private File imgFile;
    private static final int PERMISSION_CALLBACK_CONSTANT = 2100;
    private static final int REQUEST_PERMISSION_SETTING = 2101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean IS_FROM_UPDATE_SERVICE = false;
    private File compressedImage;
    private boolean sentToSettings = false;
    String Productname = "", Description = "", Branches = "", Quentity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        init();
        if (Build.VERSION.SDK_INT >= 23) {
            takePermissions();
        } else {
            proceedAfterPermission();
        }
    }

    private void init() {
        mAppPreference = new AppPreference(AddNewItemActivity.this);
    }

    private void initView() {
        iv_uploadpic = (ImageView) findViewById(R.id.iv_uploadpic);
        img_select = (ImageView) findViewById(R.id.img_select);

        edt_productname = (EditText) findViewById(R.id.edt_productname);
        edt_description = (EditText) findViewById(R.id.edt_description);

        sp_branch = (Spinner) findViewById(R.id.sp_branch);
        sp_qty = (Spinner) findViewById(R.id.sp_qty);

        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_save = (LinearLayout) findViewById(R.id.ll_save);

    }

    private void onClickHandle() {
        ll_cancel.setOnClickListener(this);
        ll_save.setOnClickListener(this);
        iv_uploadpic.setOnClickListener(this);
        img_select.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel:
                finish();
                break;
            case R.id.ll_save:
                verify();
                break;
            case R.id.iv_uploadpic:
                Uploadpic();
                break;
            case R.id.img_select:
                mAppPreference.setIsUpdateType(false);
                Uploadpic();
                break;
        }
    }

    private void Uploadpic() {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNewItemActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        Toast.makeText(AddNewItemActivity.this, "Please, try after sometimes.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                imgFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (imgFile != null) {
                Uri photoURI = FileProvider.getUriForFile(AddNewItemActivity.this, BuildConfig.APPLICATION_ID + ".provider", imgFile);
                // Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + resultCode + " NiravShah " + requestCode);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imgFile = new File(getPath(selectedImageUri));
            Log.e(TAG, "onActivityResult: " + imgFile.getAbsolutePath());


            compressImage();
            // Helper.setGlide(imgFile.getAbsolutePath(), edituser_imageView, ChangeProfilePicActivity.this);

        }
        if (requestCode == REQUEST_CAMERA) {
            Log.e(TAG, "onActivityResult: " + resultCode);
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: " + imgFile.getAbsolutePath());
                mAppPreference.setIsUpdateType(true);
                compressImage();
                //Helper.setGlide(imgFile.getAbsolutePath(), edituser_imageView, ChangeProfilePicActivity.this);
            }

        }

    }

    private void compressImage() {

        if (imgFile == null) {
            showErrorMessage("Please choose an image!");
        } else {

            new Compressor(this)
                    .compressToFileAsFlowable(imgFile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            previewCapturedImage(compressedImage);
                            //setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showErrorMessage(throwable.getMessage());
                        }
                    });
        }

    }


    private void previewCapturedImage(File compressedImage) {
        Helper.setGlide(compressedImage.getAbsolutePath(), iv_uploadpic, AddNewItemActivity.this);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void verify() {
        boolean flag = true;


        Productname = edt_productname.getText().toString();
        Description = edt_description.getText().toString();
        Branches = sp_branch.getSelectedItem().toString();
        Quentity = sp_qty.getSelectedItem().toString();

        if (imgFile == null) {
            flag = false;
            showErrorMessage("Please Upload product picture ");
        } else if (Productname.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Product name");
        } else if (Description.isEmpty()) {
            flag = false;
            showErrorMessage("Enter Product Description");
        } else if (Branches.isEmpty() || Branches.equalsIgnoreCase("Please Select any branch")) {
            flag = false;
            showErrorMessage("Select Any Branch");
        } else if (Quentity.isEmpty() || Quentity.equalsIgnoreCase("Please Select Quantity")) {
            flag = false;
            showErrorMessage("Select Quantity");
        }

        if (flag) {
            AddnewProduct(Productname, Description, Branches, Quentity);
        }

    }


    public void AddnewProduct(String productname, String description, String branch, String quentity) {
        Helper.showProgressBar(AddNewItemActivity.this, getResources().getString(R.string.pleasewait));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        Log.e(TAG, "currentDateandTime: " + currentDateandTime);
        MultipartBody.Part body = null;
        try {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            body = MultipartBody.Part.createFormData("p_image", compressedImage.getName(), reqFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody p_name = RequestBody.create(MediaType.parse("text/plain"), productname);
        RequestBody p_description = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody p_branch = RequestBody.create(MediaType.parse("text/plain"), branch);
        RequestBody p_qty = RequestBody.create(MediaType.parse("text/plain"), quentity);
        RequestBody tdate = RequestBody.create(MediaType.parse("text/plain"), currentDateandTime);

        ApiInterface restInterface = ServiceGeneratorr.createService(ApiInterface.class);
        Call<LoginResponce> call = restInterface.addProduct(body, p_name, p_description, p_branch, p_qty, tdate);
        call.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.e(TAG, "onResponse: " + response.code());
                Helper.hideProgressBar();

                if (response.body() != null) {
                    boolean successCode = response.body().getStatus();
                    if (successCode) {
                        showErrorMessage(response.body().getMessage());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //write your code here to be executed after 3 second
                                finish();
                            }
                        }, 1500);
                    } else {
                        showErrorMessage(response.body().getMessage());
                    }
                } else {
                    showErrorMessage(getResources().getString(R.string.server_connection_problem));
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                Log.e("error", t.toString());
                Helper.hideProgressBar();
            }
        });


    }

    public void takePermissions() {


        if (ActivityCompat.checkSelfPermission(AddNewItemActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(AddNewItemActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewItemActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddNewItemActivity.this, permissionsRequired[1])) {
                //Show Information about why you need the permission
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNewItemActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddNewItemActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showErrorMessage(getResources().getString(R.string.need_permission));
                        if (IS_FROM_UPDATE_SERVICE) {
                            Intent intent1 = new Intent();
                            intent1.putExtra("is_edit", false);
                            setResult(309, intent1);
                            finish();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            } else if (mAppPreference.getPermission(permissionsRequired[0])) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNewItemActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showErrorMessage(getResources().getString(R.string.need_permission));
                        if (IS_FROM_UPDATE_SERVICE) {
                            /*Intent intent1 = new Intent();
                            intent1.putExtra("is_edit", false);
                            setResult(309, intent1);*/
                            finish();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(AddNewItemActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            mAppPreference.setPermission(permissionsRequired[0], true);
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        initView();
        onClickHandle();
    }

    public void showErrorMessage(String msg) {
        Toast.makeText(AddNewItemActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(AddNewItemActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewItemActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddNewItemActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddNewItemActivity.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(AddNewItemActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
