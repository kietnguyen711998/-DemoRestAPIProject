package com.example.restapiprojectappdemo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.restapiprojectappdemo.model.ServerRequest;
import com.example.restapiprojectappdemo.model.ServerResponse;
import com.example.restapiprojectappdemo.model.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener{
    private Button btn_reset;
    private EditText et_email,et_code,et_password;
    private TextView tv_timer;
    private ProgressBar progress;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password,container,false);
        initViews(view);
        return view;
    }


    private void initViews(View view) {
        btn_reset = view.findViewById(R.id.btn_reset);
        tv_timer = view.findViewById(R.id.timer);
        et_code = view.findViewById(R.id.et_code);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        tv_timer.setVisibility(View.GONE);
        btn_reset.setOnClickListener(this);
        progress = view.findViewById(R.id.progress);
    }
    ////dat lai pass khi ban dau
    private void initiateResetPasswordProcess(String email){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RESET_PASSWORD_INITIATE);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)){

                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    et_email.setVisibility(View.GONE);
                    et_code.setVisibility(View.VISIBLE);
                    et_password.setVisibility(View.VISIBLE);
                    tv_timer.setVisibility(View.VISIBLE);
                    btn_reset.setText("Change Password");
                    isResetInitiated = true;
                    startCountdownTimer();

                } else {

                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Log.d(Constants.TAG,"xxxxxxxx " +t.getMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }
    //dat lai pass sau khi hoan thanh
    private void finishResetPasswordProcess(String email,String code, String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setCode(code);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RESET_PASSWORD_FINISH);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)){

                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    countDownTimer.cancel();
                    isResetInitiated = false;
                    goToLogin();

                } else {

                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Log.d(Constants.TAG,"yyyyyyyyyyyy " +t.getMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Snackbar.make(getView(), "Time Out ! Request again to reset password.", Snackbar.LENGTH_LONG).show();
                goToLogin();
            }
        }.start();
    }

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_reset:

                if(!isResetInitiated) {


                    email = et_email.getText().toString();
                    if (!email.isEmpty()) {
                        progress.setVisibility(View.VISIBLE);
                        initiateResetPasswordProcess(email);
                    } else {

                        Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    String code = et_code.getText().toString();
                    String password = et_password.getText().toString();

                    if(!code.isEmpty() && !password.isEmpty()){

                        finishResetPasswordProcess(email,code,password);
                    } else {
                        //////////////////////////////////
                        Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }

                }

                break;
        }
    }
}
