package com.example.mmr.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.mmr.R;
import com.example.mmr.VolleySingleton;
import com.example.mmr.shared.LoadingDialogBuilder;
import com.example.mmr.shared.MailSender;
import com.example.mmr.shared.SharedModel;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    Button submit;
    private EditText cin;
    private EditText adresse;
    private EditText email;
    private EditText nom;
    private EditText prenom;
    private EditText tele;
    private EditText ville;
    private Spinner sang;
    private Spinner assurance;
    private RadioGroup gender;
    private EditText age;
    private EditText password;
    private EditText confPassword;
    private RequestQueue queue;
    private SharedModel sharedModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        sharedModel = new SharedModel(this, queue);
        submit=findViewById(R.id.submit);
        cin=findViewById(R.id.cin);
        adresse=findViewById(R.id.adrs);
        email=findViewById(R.id.email);
        nom=findViewById(R.id.nom);
        prenom=findViewById(R.id.prenom);
        tele=findViewById(R.id.tele);
        ville=findViewById(R.id.ville);
        sang=findViewById(R.id.spinner);
        assurance=findViewById(R.id.assurance);
        gender=findViewById(R.id.gender);
        password=findViewById(R.id.password);
        confPassword=findViewById(R.id.confirm_password);
        age=findViewById(R.id.age);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test if somme input is empty
                if (cin.getText().toString().matches("") ||
                        adresse.getText().toString().matches("") ||
                        email.getText().toString().matches("") ||
                        nom.getText().toString().matches("") ||
                        prenom.getText().toString().matches("") ||
                        tele.getText().toString().matches("") ||
                        ville.getText().toString().matches("") ||
                        sang.getSelectedItemPosition() == 0 ||
                        gender.getCheckedRadioButtonId() == -1 ||
                        password.getText().toString().matches("") ||
                        confPassword.getText().toString().matches("") ||
                        age.getText().toString().matches("")
                ){
                    //if one at least is empty
                    Toast.makeText(getApplicationContext(),"Remplissez tous les champs !",Toast.LENGTH_LONG).show();
                }else{
                    //non is empty
                    //testing password length
                    if (password.getText().length()<8)
                        Toast.makeText(getApplicationContext(),"Attention mot de passe doit contenir plus que 8 caracteres",Toast.LENGTH_LONG).show();
                    else{
                        //testing matching
                        if (! password.getText().toString().equals(confPassword.getText().toString()))
                            Toast.makeText(getApplicationContext(),"Erreur le mots de passes sont différents",Toast.LENGTH_LONG).show();
                        else{
                            if (!MailSender.isValid(email.getText().toString()))
                                Toast.makeText(getApplicationContext(),"Email non valide",Toast.LENGTH_LONG).show();
                            else{
                                Dialog dialog=new Dialog(SignUp.this);
                                dialog.setContentView(R.layout.dialog_confirm_email);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                EditText code=(EditText) dialog.findViewById(R.id.code);
                                Button validate=(Button) dialog.findViewById(R.id.validate);
                                TextView errorText=(TextView) dialog.findViewById(R.id.erreur);
                                validate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String currentCode= code.getText().toString().trim();
                                        if (MailSender.codeValid(currentCode)){
                                            errorText.setVisibility(View.GONE);
                                            dialog.dismiss();
                                        }else{
                                            errorText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                                MailSender.sendVerificationEmail(SignUp.this,nom.getText().toString()+" "+prenom.getText().toString(),email.getText().toString());
                                dialog.show();
                                DisplayMetrics metrics = getResources().getDisplayMetrics();
                                int width = metrics.widthPixels;
                                dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);
                                dialog.setCancelable(false);
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                        LoadingDialogBuilder.startDialog(SignUp.this);
                                        //everything is okey sending request
                                        //preparing infos
                                        Map<String,String> infos=new HashMap<>();
                                        infos.put("lname",nom.getText().toString());
                                        infos.put("cin",cin.getText().toString());
                                        infos.put("fname",prenom.getText().toString());
                                        infos.put("adrr",adresse.getText().toString());
                                        infos.put("ville",ville.getText().toString());
                                        infos.put("age",age.getText().toString());
                                        infos.put("gender",convertGender(gender.getCheckedRadioButtonId()));
                                        infos.put("sang",convertSang(sang.getSelectedItemPosition()));
                                        infos.put("assur",convertAssurance(assurance.getSelectedItemPosition()));
                                        infos.put("email",email.getText().toString());
                                        infos.put("tele",tele.getText().toString());
                                        infos.put("pass",password.getText().toString());
                                        sharedModel.register(infos, new SharedModel.SignUpCallBack() {
                                            @Override
                                            public void onSuccess(String message) {
                                                LoadingDialogBuilder.closeDialog();
                                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                finish();
                                            }

                                            @Override
                                            public void onErr(String message) {
                                                LoadingDialogBuilder.closeDialog();
                                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });

                            }

                        }
                    }
                }
            }
        });
    }
    //converting sang from position in spinner to a name
    public String convertSang(int pos){
        String s="";
        switch (pos){
            case 1:s= "A-";break;
            case 2:s= "A+";break;
            case 3:s= "B-";break;
            case 4:s= "B+";break;
            case 5:s= "O-";break;
            case 6:s= "O+";break;
            case 7:s= "AB-";break;
            case 8:s= "AB+";break;
        }
        return s;
    }
    //converting sang from position in spinner to a name
    public String convertAssurance(int pos){
        String s="";
        switch (pos){
            case 0:s= "RIEN";break;
            case 1:s= "RAMED";break;
            case 2:s= "CNSS";break;
            case 3:s= "CNOPS";break;
        }
        return s;
    }
    //converting gender from id to a name
    public String convertGender(int id){
        String s="";
        switch (id){
            case R.id.male:s= "H";break;
            case R.id.female:s= "F";break;
        }
        return s;
    }
}