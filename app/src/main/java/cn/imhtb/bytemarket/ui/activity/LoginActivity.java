package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.imhtb.bytemarket.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    @BindView(R.id.et_login_account)
    EditText et_account;

    @BindView(R.id.civ_login_avatar)
    CircleImageView civ_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.et_login_account)
    public void accountChangeListener(){
        //TODO
        if (et_account.getText().toString().trim().length() == 6){
            civ_avatar.setImageResource(R.mipmap.avatar);
        }else {
            civ_avatar.setImageResource(R.mipmap.avatar_un_login);
        }
    }

    @OnClick(R.id.btn_login_login)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:{
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                finish();
            } break;
            case 666: {

            } break;
        }
    }
}
