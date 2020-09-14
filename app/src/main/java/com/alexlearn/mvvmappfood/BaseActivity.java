package com.alexlearn.mvvmappfood;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

//Мы создаем эту активность руками самостоятельно, а не через кнопку создать активность
//ВНИМАНИЕ!!! ЭТА АКТИВНОСТЬ НЕ БУДЕТ ПРОПИСАНА В АНДРОИД МАНИФЕСТЕ !!!!! ДЛЯ ЭТОГО ДЕЛАЕМ ЕЕ АБСТРАКТНОЙ
public abstract class BaseActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    //Подключаем активность к макетк activity_base
    // ВАЖНО// ЭТОТ МЕТОД НАДО ВЫБРАТЬ ИЗ СПИСКА ПОСЛЕ НАЖАТИЯ ALT Insert. Тот который выдает при простом печатании не подходит!!!!
    // ПРАВИЛЬНЫЙ setContentView
    @Override
    public void setContentView(int layoutResID) {
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);

        super.setContentView(constraintLayout);
    }
// НЕПРАВИЛЬНЫЙ setContentView который появляется если просто впечатать setContentView
//    @Override
//    public void setContentView(View view) {
//
//
//        super.setContentView(view);
//    }

    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
