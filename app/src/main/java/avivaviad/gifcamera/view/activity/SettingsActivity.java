package avivaviad.gifcamera.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import avivaviad.gifcamera.R;
import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.presenter.BaseView;
import avivaviad.gifcamera.presenter.Presenter;
import avivaviad.gifcamera.presenter.SettingsPresenter;
import avivaviad.gifcamera.view.BaseActivity;

/**
 * Created by Aviad on 14/08/2017.
 */

public class SettingsActivity extends BaseActivity implements BaseView, SettingsPresenter.SettingsPresenterCallBack, AdapterView.OnItemSelectedListener, View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private static final int READ_RC_SMALL_IMG = 41, READ_RC_FRAME = 42;
    private EditText edit_duration_for_each_frame, edit_capture_frame_rate,
            edit_frame_count, edit_title, edit_tag_db;
    private Spinner spinner_quality, spinner_font, spinner_font_size, spinner_color;
    private Button btn_add_frame, btn_add_image,btn_gif_gallery,btn_save,btn_font_color;
    private CheckBox checkBoxAddFrame, checkBoxAddImage;
    private ImageView img_frame, img_small;
    private int[] arrFontColors = {Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.RED, Color.BLUE,Color.DKGRAY};
    private int[] arrFontSize = {18, 20, 22, 24, 26, 28, 30};
    private String[] arrFontType = {"GothamRounded-Bold.otf", "Grange.ttf", "a.ttf"};
    private Context context;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context=  getApplicationContext();
        initViews();
        loadViewLastSettings();


    }


    private void initViews() {
        edit_duration_for_each_frame = (EditText) findViewById(R.id.edit_duration_for_each_frame);
        edit_frame_count = (EditText) findViewById(R.id.edit_frame_count);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_capture_frame_rate = (EditText) findViewById(R.id.edit_capture_frame_rate);
        edit_tag_db = (EditText) findViewById(R.id.edit_tag_db);
        checkBoxAddFrame = (CheckBox) findViewById(R.id.checkbox_frame);
        checkBoxAddImage = (CheckBox) findViewById(R.id.checkbox_image);
        img_frame = (ImageView) findViewById(R.id.img_frame);
        img_small = (ImageView) findViewById(R.id.img_small);

        spinner_font = (Spinner) findViewById(R.id.spinner_font);
        spinner_font_size = (Spinner) findViewById(R.id.spinner_font_size);
        spinner_quality = (Spinner) findViewById(R.id.spinner_quality);
        btn_add_frame = (Button) findViewById(R.id.btn_add_frame);
        btn_add_image = (Button) findViewById(R.id.btn_add_image);
        btn_gif_gallery = (Button) findViewById(R.id.btn_gif_gallery);
        btn_font_color = (Button) findViewById(R.id.btn_font_color);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_font_color.setOnClickListener(this);
        btn_add_frame.setOnClickListener(this);
        btn_add_image.setOnClickListener(this);
        btn_gif_gallery.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        checkBoxAddFrame.setOnCheckedChangeListener(this);
        checkBoxAddImage.setOnCheckedChangeListener(this);
        findViewById(R.id.btn_default_settings).setOnClickListener(this);



       /* EditText.OnEditorActionListener editorActionListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("Value:" + v.getId() + " saved successfully", "Whaay");

                switch (v.getId()) {

                    case R.id.edit_duration_for_each_frame:
                        saveValue(SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME, v.getText());
                        break;
                    case R.id.edit_frame_count:
                        saveValue(SharedPreferencesManager.KEY_FRAME_COUNT, v.getText());
                        break;
                    case R.id.edit_title:
                        saveValue(SharedPreferencesManager.KEY_TITLE, v.getText());
                        break;
                    case R.id.edit_capture_frame_rate:
                        saveValue(SharedPreferencesManager.KEY_CAPTURE_FRAME_RATE, v.getText());
                        break;
                    case R.id.edit_tag_db:
                        saveValue(SharedPreferencesManager.KEY_DB_TAG, v.getText());
                        break;
                }

                return true;
            }
        };
        edit_duration_for_each_frame.setOnEditorActionListener(editorActionListener);
        edit_frame_count.setOnEditorActionListener(editorActionListener);
        edit_title.setOnEditorActionListener(editorActionListener);
        edit_capture_frame_rate.setOnEditorActionListener(editorActionListener);
        edit_tag_db.setOnEditorActionListener(editorActionListener);*/


        String[] quality = new String[]{"low", "medium", "high"};
        String[] fontSize = new String[]{"18", "22", "24", "26", "28", "30"};
        String[] fontType = new String[]{"font abc", "font abc", "font abc"};
        String[] fontColor = new String[]{"  ", "  ", "  ", "  ", "  ", "  "};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quality);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_quality.setAdapter(spinnerAdapter);
        spinner_quality.setSelection(1);
        spinner_quality.setOnItemSelectedListener(this);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fontType) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"+arrFontType[position]));
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"+arrFontType[position]));
                return v;
            }
        };

        spinner_font.setAdapter(spinnerAdapter);
        spinner_font.setSelection(0);
        spinner_font.setOnItemSelectedListener(this);

       /* spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fontColor) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setBackgroundColor(arrFontColors[position]);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = superz.getView(position, convertView, parent);
                v.setBackgroundColor(arrFontColors[position]);
                return v;
            }
        };

        spinner_color.setAdapter(spinnerAdapter);
        spinner_color.setSelection(0);
        spinner_color.setOnItemSelectedListener(this);*/

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fontSize);
        spinner_font_size.setAdapter(spinnerAdapter);
        spinner_font_size.setSelection(1);
        spinner_font_size.setOnItemSelectedListener(this);
    }

    private void saveValue(String keyTitle, CharSequence newValue) {
        SharedPreferencesManager.saveValue(this, keyTitle, newValue + "");
    }


    private void loadViewLastSettings() {
        HashMap<String, String> hmLastValues = ((SettingsPresenter) mPresenter).getLastValues(this);
        String strIndex = "";
        String loadedValue;
        Log.d("firstfirst", "last" + hmLastValues.get(0 + ""));
        for (int i = 0; i < hmLastValues.size(); i++) {
            strIndex = String.valueOf(i);
            loadedValue = hmLastValues.get(strIndex);
            switch (strIndex) {
                case "0":
                    edit_capture_frame_rate.setText(loadedValue);
                    break;
                case "1":
                    edit_duration_for_each_frame.setText(loadedValue);
                    break;
                case "2":
                    edit_frame_count.setText(loadedValue);
                    break;
                case "3":
                    spinner_quality.setSelection(Integer.parseInt(loadedValue));
                    break;
                case "4":
                    edit_title.setText(loadedValue);
                    break;
                case "5":
                    spinner_font_size.setSelection(Integer.parseInt(loadedValue));
                    break;
                case "6":
                    spinner_font.setSelection(Integer.parseInt(loadedValue));
                    break;
                /*case "7":
                    spinner_color.setSelection(Integer.parseInt(loadedValue));
                    break;*/
                case "8":
                    checkBoxAddFrame.setChecked(isChecked(loadedValue));
                    break;
                case "9":
                    Log.d("uriths", hmLastValues.get(strIndex) + "");
                    final String finalLoadedValue = loadedValue;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getApplicationContext()).load(finalLoadedValue).into(img_frame);
                        }
                    },120);
                    break;
                case "10":
                    checkBoxAddImage.setChecked(isChecked(loadedValue));
                    break;
                case "11":
                    final String finalLoadedValue2 = loadedValue;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getApplicationContext()).load(finalLoadedValue2).into(img_small);
                        }
                    },120);                    break;
                case "12":
                    edit_tag_db.setText(loadedValue);
                    break;
            }
        }
    }

    private boolean isChecked(String trueOrFalse) {
        Log.d("trueOrfalse", trueOrFalse + "");
        return trueOrFalse.equals("true");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_quality:
                saveValue(SharedPreferencesManager.KEY_QUALITY, position + "");
                Log.d("Valuess:" + position + " inside sekected", "Whaay");
                break;
            case R.id.spinner_font_size:
                saveValue(SharedPreferencesManager.KEY_FONT_SIZE, position + "");
                edit_title.setTextSize(arrFontSize[position]);
                break;
            case R.id.spinner_font:
                saveValue(SharedPreferencesManager.KEY_FONT_TYPE, position + "");
                edit_title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/" + arrFontType[position]));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected Presenter getPresenter() {
        return new SettingsPresenter();
    }

    @Override
    protected void bind() {
        mPresenter.bind(this);
    }


    @Override
    protected void unbind() {
        mPresenter.unbind();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }



    private void saveAllInputs() {
        saveValue(SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME, edit_duration_for_each_frame.getText());
        saveValue(SharedPreferencesManager.KEY_TITLE, edit_title.getText());
        saveValue(SharedPreferencesManager.KEY_CAPTURE_FRAME_RATE, edit_capture_frame_rate.getText());
        saveValue(SharedPreferencesManager.KEY_DB_TAG, edit_tag_db.getText());
        if(!edit_frame_count.getText().toString().trim().isEmpty()){
            if (Integer.parseInt(edit_frame_count.toString())<2){
                Toast.makeText(this, "Minimum frame count is 2\nSaved successfully", Toast.LENGTH_LONG).show();
                saveValue(SharedPreferencesManager.KEY_FRAME_COUNT, "2");
                edit_frame_count.setText("2");
            }else{
                saveValue(SharedPreferencesManager.KEY_FRAME_COUNT, edit_frame_count.getText());
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_frame:
                openGallery(READ_RC_FRAME);
                break;
            case R.id.btn_add_image:
                openGallery(READ_RC_SMALL_IMG);
                break;
            case R.id.btn_default_settings:
                SharedPreferencesManager.setDefaultSettings(this);
                loadViewLastSettings();
                break;
            case R.id.btn_gif_gallery:
                ((SettingsPresenter)mPresenter).onGifGalleryPressed();
                break;
            case R.id.btn_font_color:
            //    ((SettingsPresenter)mPresenter).chooseColor(getApplicationContext());
                break;
            case R.id.btn_save:
                saveAllInputs();
                break;
        }
    }

    private void openGallery(int REQUEST_CODE) {
       /* Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
        imageIntent.setType("image*//*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);*/
        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
           // intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getData()!=null & requestCode == READ_RC_SMALL_IMG || requestCode == READ_RC_FRAME) {
            Log.d("urithd", data.getData() + "");
            Log.d("urithp", data.getData().getPath() + "");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int takeFlags = data.getFlags();
                takeFlags &= Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                ContentResolver resolver = getApplicationContext().getContentResolver();

                    resolver.takePersistableUriPermission(data.getData(), takeFlags);

            }

            saveAndDisplayImg(requestCode, data.getDataString());

        }
    }

    private void saveAndDisplayImg(int requestCode, String strData) {
        ImageView currentImgView = img_frame;
        String strKey = "";
        if (requestCode == READ_RC_FRAME) {
            currentImgView = img_frame;
            strKey = SharedPreferencesManager.KEY_FRAME_SRC;
        } else if (requestCode == READ_RC_SMALL_IMG) {
            currentImgView = img_small;
            strKey = SharedPreferencesManager.KEY_IMAGE_SRC;
        }
        if (strData != null) {
            Glide.with(this).load(strData).into(currentImgView);
            Log.d("urithsave", strData);
            SharedPreferencesManager.saveValue(this, strKey, strData);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.checkbox_frame:
                Log.d("istrue",isChecked+"");
                SharedPreferencesManager.saveValue(context,SharedPreferencesManager.KEY_CHECK_ADD_FRAME,isChecked+"");
                break;
            case R.id.checkbox_image:
                Log.d("istrue",isChecked+"");
                SharedPreferencesManager.saveValue(context,SharedPreferencesManager.KEY_CHECK_ADD_IMAGE,isChecked+"");
                break;
        }
    }
}
