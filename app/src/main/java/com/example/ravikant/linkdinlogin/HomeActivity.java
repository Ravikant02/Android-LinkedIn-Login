package com.example.ravikant.linkdinlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.R.id.progress;

public class HomeActivity extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
    private TextView userName;
    private ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userImage = (ImageView) findViewById(R.id.imageView);
        userName = (TextView) findViewById(R.id.textView);
        linkededinApiHelper();
    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(HomeActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public  void  showResult(JSONObject response){

        try {
            Log.e("RESPONSE==", response.toString());
            userName.setText(response.get("formattedName").toString());
            Log.e("RESPONSE==IMAGE==", response.getString("pictureUrls"));
            JSONObject imgObject = new JSONObject(response.getString("pictureUrls"));
            JSONArray jsonArray = new JSONArray(imgObject.getString("values"));
            Log.e("RESPONSE==PATH==", jsonArray.get(0).toString());
            Picasso.with(this).load(jsonArray.get(0).toString())
                    .into(userImage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
