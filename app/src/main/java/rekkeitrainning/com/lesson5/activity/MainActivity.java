package rekkeitrainning.com.lesson5.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

import rekkeitrainning.com.lesson5.R;
import rekkeitrainning.com.lesson5.adapter.ContactsAdapter;
import rekkeitrainning.com.lesson5.model.Contact;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.ItemClickListener{
    BottomNavigationView btNavigation;
    RecyclerView rc_contact;
    ArrayList<Contact> mListContact = null;
    ContactsAdapter mContactAdapter;
    private boolean isGridlayout = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindData();
        initListener();
        setDataVertical();
    }

    private void bindData() {
        mListContact = new ArrayList<>();
        mListContact.add(new Contact("Đặng Xuân Duy", "0967505425"));
        mListContact.add(new Contact("Hoàng Anh Tuấn", "0967505425"));
        mListContact.add(new Contact("Nguyễn Tiến Dũng", "0967505425"));
        mListContact.add(new Contact("Nguyễn Văn Hưng", "0967505425"));
        mListContact.add(new Contact("Đào Duy Khôi", "0967505425"));
        mListContact.add(new Contact("Nguyễn Quang Thủ", "0967505425"));
        mListContact.add(new Contact("Nguyễn Hồng Nhung", "0967505425"));
        mListContact.add(new Contact("Phạm Mạnh Tường", "0967505425"));
    }
    private void setDataVertical(){
        if (mContactAdapter == null){
            mContactAdapter = new ContactsAdapter(this);
        }
        mContactAdapter.setmListContact(mListContact);
        rc_contact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mContactAdapter.onItemClickListener(this);
        rc_contact.setAdapter(mContactAdapter);
    }
    private void setDataGrid(){
        if (mContactAdapter == null){
            mContactAdapter = new ContactsAdapter(this);
        }
        mContactAdapter.setmListContact(mListContact);
        rc_contact.setLayoutManager(new GridLayoutManager(this,2));
        mContactAdapter.onItemClickListener(this);
        rc_contact.setAdapter(mContactAdapter);
    }
    private void initListener() {
        btNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private void initView() {
        btNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        disableShiftMode(btNavigation);
        rc_contact = findViewById(R.id.rcContact);
    }
    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_transaction:
                isGridlayout = !isGridlayout;
                if (isGridlayout){
                    setDataGrid();
                } else {
                    setDataVertical();
                }
                break;
            case R.id.action_add:
                showDialogAddContact();
                break;

        }
        return true;
    }

    private void showDialogAddContact() {
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_contact);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        TextInputEditText et_name = mDialog.findViewById(R.id.etName);
        TextInputEditText et_phone = mDialog.findViewById(R.id.etPhone);
        Button btn_save = mDialog.findViewById(R.id.btnSave);
        Button btn_cancle = mDialog.findViewById(R.id.btnCancle);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)){
                    Contact mContact = new Contact(name, phone);
                    mListContact.add(mContact);
                    mContactAdapter.notifyDataSetChanged();
                    rc_contact.smoothScrollToPosition(mContactAdapter.getItemCount() - 1);
                    mDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this,"Bạn vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        tv_title.setText(getResources().getString(R.string.insert));
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(mDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        mDialog.getWindow().setAttributes(lWindowParams);
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("TAG", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("TAG", "Unable to change value of shift mode");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        showDialogUpdateContact(position);
    }

    private void showDialogUpdateContact(int position) {
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_contact);
        TextView tv_title = mDialog.findViewById(R.id.tv_title);
        TextInputEditText et_name = mDialog.findViewById(R.id.etName);
        TextInputEditText et_phone = mDialog.findViewById(R.id.etPhone);
        Button btn_save = mDialog.findViewById(R.id.btnSave);
        Button btn_cancle = mDialog.findViewById(R.id.btnCancle);
        et_name.setText(mListContact.get(position).getNameContact());
        et_phone.setText(mListContact.get(position).getPhoneNumber());
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(name) &&
                    !TextUtils.isEmpty(phone) && (
                    !name.equalsIgnoreCase(mListContact.get(position).getNameContact()) ||
                    !phone.equalsIgnoreCase(mListContact.get(position).getPhoneNumber()))){
                    mListContact.get(position).setNameContact(name);
                    mListContact.get(position).setPhoneNumber(phone);
                    mContactAdapter.notifyDataSetChanged();
                    rc_contact.smoothScrollToPosition(position);
                    mDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this,"Thông tin chưa thay đổi hoặc không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        tv_title.setText(getResources().getString(R.string.update));
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(mDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        mDialog.getWindow().setAttributes(lWindowParams);
    }
}
