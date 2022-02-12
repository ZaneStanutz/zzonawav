package com.trioszs.zaneprojectapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivityLogin extends AppCompatActivity {

    final Handler handler = new Handler();
    private final String VERIFY_URL ="https://api.nexmo.com/verify/json";
    private final String VERIFY_CHECK_URL = "https://api.nexmo.com/verify/check/json";
    private String userPhone = "";

    RequestVerification getCode = new RequestVerification(); // async classes
    VerifyCheck verifyCheck = new VerifyCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        Button numberSubmit = findViewById(R.id.numberSubmit);
        Button validate = findViewById(R.id.verifyValidate);
        validate.setEnabled(false);
        Button tryAgain = findViewById(R.id.buttonTryAgain);
        tryAgain.setVisibility(View.GONE);
        TextView validationFailed = findViewById(R.id.validationFailed);
        validationFailed.setVisibility(View.GONE);
        Button nextPage = findViewById(R.id.buttonContinueLogin);
        nextPage.setEnabled(false);  // disable for testing

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(getApplicationContext(), MainActivityLogin.class);
                startActivity(refresh);
            }
        });

        numberSubmit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                EditText userPhoneEt = findViewById(R.id.editTextPhone);
                if(userPhoneEt.getText().length() < 11) {
                    Toast.makeText(getApplicationContext(), "Please enter country code and " +
                            "number with no spaces",Toast.LENGTH_LONG).show();
                }
                else{
                    userPhone = userPhoneEt.getText().toString();
                    requestCode(VERIFY_URL, userPhone);
                    validate.setEnabled(true);
                    numberSubmit.setEnabled(false);
                    // what if user did not receive number?
                }

            }
        }); // number submit onclick()
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText smsCode = findViewById(R.id.editTextVerify);
                String smsCodeString = smsCode.getText().toString();
                if(smsCodeString.length() == 6) {
                    System.out.println(getCode.requestId); // check to see it is working
                    verifyCode(VERIFY_CHECK_URL, getCode.getRequestId(), smsCodeString);
                    nextPage.setEnabled(true);
                    validate.setEnabled(false);// if we do not do this app will crash

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter a 6 digit " +
                            "number for Validation", Toast.LENGTH_LONG).show();
                }
                System.out.println(verifyCheck.verifyStatus + " verify code request status");

            }
        }); // validate onclick()

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "0" = Ok
                // "16" = incorrect verification code
                
                if(verifyCheck.getVerifyCheckStatus().equals("0")) {
                    Intent loginPage = new Intent(getApplicationContext(), MainActivityVerifiedLogin.class );
                    loginPage.putExtra("userPhone", userPhone);
                    startActivity(loginPage);
                }
                else{
                    validationFailed.setVisibility(View.VISIBLE);
                    tryAgain.setVisibility(View.VISIBLE);
                    tryAgain.setEnabled(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tryAgain.setEnabled(true);
                        }
                    },120000);
                }
                System.out.println(verifyCheck.getVerifyCheckStatus());

            }
        });

    }
    private void requestCode(String url, String userPhone) {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUrl(url);
        requestPackage.setParam("api_key","72ddb7e5");
        requestPackage.setParam("api_secret","wd0hY0pMPWRXSxT0");
        requestPackage.setParam("number",userPhone);
        requestPackage.setParam("brand","ZZONAWAV");
        requestPackage.setParam("country","CA");
        requestPackage.setParam("sender_id","ZZONAWAV");
        requestPackage.setParam("code_length", "6");
        requestPackage.setParam("lg","en-us");
        requestPackage.setParam("pin_expiry", "120");
        requestPackage.setParam("next_event_wait", "60");
        requestPackage.setParam("workflow_id", "4");

        getCode.execute(requestPackage); // global async
    } // requestCode()

    private void verifyCode(String url, String requestId, String validationCode ) {

        RequestPackage requestPackage2 = new RequestPackage();
        requestPackage2.setMethod("POST");
        requestPackage2.setUrl(url);
        requestPackage2.setParam("api_key","72ddb7e5");
        requestPackage2.setParam("api_secret","wd0hY0pMPWRXSxT0");
        requestPackage2.setParam("request_id", requestId);
        requestPackage2.setParam("code", validationCode);

        verifyCheck.execute(requestPackage2); // async global object
    } // requestCode()
} // class MainActivity

class RequestVerification extends AsyncTask<RequestPackage, String, String> {
    String requestId = "";
    @Override
    protected String doInBackground(RequestPackage... params) {
        return HttpManager.getData(params[0]);
    }

    //The String that is returned in the doInBackground() method is sent to the
    // onPostExecute() method below. The String should contain JSON data.
    @Override
    protected void onPostExecute(String result) {
        try {
            //We need to convert the string in result to a JSONObject
            JSONObject jsonObject = new JSONObject(result);

            requestId = jsonObject.getString("request_id");
            String response = jsonObject.toString();
            System.out.println(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    protected String getRequestId(){
        return requestId;
    }
} // class RequestVerification


class VerifyCheck extends AsyncTask<RequestPackage, String, String> {
    String verifyStatus = "";
    @Override
    protected String doInBackground(RequestPackage... params) {
        return HttpManager.getData(params[0]);
    }

    //The String that is returned in the doInBackground() method is sent to the
    // onPostExecute() method below. The String should contain JSON data.
    @Override
    protected void onPostExecute(String result) {
        try {
            //We need to convert the string in result to a JSONObject

            JSONObject jsonObject = new JSONObject(result);
            verifyStatus = jsonObject.getString("status");
            String answer = jsonObject.toString();
            System.out.println(answer); // logging Json object for debugging
            System.out.println(verifyStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    protected String getVerifyCheckStatus() {
        return verifyStatus;
    }
} //class VerifyCheck

