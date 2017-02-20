package cn.qiditu.guet.android.sqliteopeartorexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText etUsername;
    @BindView(R.id.interest)
    EditText etInterest;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButtonEnableState();
            }
        };
        etUsername.addTextChangedListener(textWatcher);
        etInterest.addTextChangedListener(textWatcher);

        updateButtonEnableState();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnAdd)
    void onBtnAddClicked() {
        String username = etUsername.getText().toString();
        String interest = etInterest.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DataHelper.name, username);
        intent.putExtra(DataHelper.interest, interest);
        setResult(MainActivity.inputResultCode, intent);
        finish();
    }

    private void updateButtonEnableState() {
        btnAdd.setEnabled(etUsername.getText().length() != 0
                && etInterest.getText().length() != 0);
    }

}
