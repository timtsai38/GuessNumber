package com.example.user.guessnumber;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity
implements NumbersAdapter.ItemClickListener{
    private static final String TAG = MainActivity.class.toString();

    private static final int DEFAULT_NUMBERS_RANGE = 30;

    private int mAnswerPosition;

    private EditText mRangeEditText;
    private Toast mMessageToast;

    private NumbersAdapter mNumbersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shutDownKeyboard();
        setRecyclerView();
        setRandomAnswerPosition(DEFAULT_NUMBERS_RANGE);
        getDrawableResource();
        displayMessage(getString(R.string.message_game_start));
    }

    private void shutDownKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setRecyclerView(){
        RecyclerView numbersRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        mNumbersAdapter = new NumbersAdapter(DEFAULT_NUMBERS_RANGE, this);
        numbersRecyclerView.setAdapter(mNumbersAdapter);
        numbersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setNumbersRange() {
        int numbersRange;

        mRangeEditText = (EditText) findViewById(R.id.et_range);
        numbersRange = Integer.valueOf(mRangeEditText.getText().toString());
        Log.d(TAG,"mNumbersRange:" + numbersRange);

        if(numbersRange > 0){
            mNumbersAdapter.setNumbersRange(numbersRange);
            initialPosition(numbersRange);
            setRandomAnswerPosition(numbersRange);
        }else{
            displayMessage(getString(R.string.message_range_error));
        }
    }

    private void initialPosition(int numbersRange) {
        mNumbersAdapter.setAnswerPosition(mNumbersAdapter.DEFAULT_ANSWER_POSITION);
        mNumbersAdapter.setUpperLimitPosition(numbersRange - 1);
        mNumbersAdapter.setLowerLimitPosition(mNumbersAdapter.DEFAULT_LOWER_LIMIT_POSITION);
        mNumbersAdapter.notifyDataSetChanged();
    }

    private final Random random = new Random();

    private void setRandomAnswerPosition(int numbersRange){
        mAnswerPosition = random.nextInt(numbersRange);
        Log.d(TAG,"randomAnswerPosition:" + mAnswerPosition);
    }

    private void displayMessage(String message){
        if(mMessageToast != null) mMessageToast.cancel();
        mMessageToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mMessageToast.show();
    }

    private void displayCurrentNumbersLimit(){
        int currentUpperNumberLimit = mNumbersAdapter.getUpperLimitPosition() + 1;
        int currentLowerNumberLimit = mNumbersAdapter.getLowerLimitPosition() + 1;
        displayMessage(currentLowerNumberLimit + "~" + currentUpperNumberLimit);
    }

    private void checkAnswerPosition(int clickedItemPosition){
        if(mAnswerPosition == clickedItemPosition){
            mNumbersAdapter.setAnswerPosition(mAnswerPosition);
            displayMessage(getString(R.string.message_bingo));
            Log.d(TAG,"AnswerPosition:" + clickedItemPosition);
        }else if( mAnswerPosition > clickedItemPosition){
            mNumbersAdapter.setLowerLimitPosition(clickedItemPosition);
            displayCurrentNumbersLimit();
            Log.d(TAG,"LowerLimitPosition:" + clickedItemPosition);
        }else if (mAnswerPosition < clickedItemPosition){
            mNumbersAdapter.setUpperLimitPosition(clickedItemPosition);
            displayCurrentNumbersLimit();
            Log.d(TAG,"UpperLimitPosition:" + clickedItemPosition);
        }

        mNumbersAdapter.notifyDataSetChanged();
    }

    private void getDrawableResource(){
        Drawable normalBackground;
        Drawable correctBackground;
        Drawable incorrectBackground;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            normalBackground = ContextCompat.getDrawable(this, R.drawable.item_normal);
            correctBackground = ContextCompat.getDrawable(this, R.drawable.item_correct);
            incorrectBackground = ContextCompat.getDrawable(this, R.drawable.item_incorrect);
        }else{
            normalBackground = getResources().getDrawable(R.drawable.item_normal);
            correctBackground = getResources().getDrawable(R.drawable.item_correct);
            incorrectBackground = getResources().getDrawable(R.drawable.item_incorrect);
        }

        mNumbersAdapter.setDrawbleResource(normalBackground, correctBackground, incorrectBackground);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();
        if (clickedItemId == R.id.action_setting){
                setNumbersRange();
                displayMessage(getString(R.string.message_new_round));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        displayMessage("position:" + String.valueOf(clickedItemPosition));
        checkAnswerPosition(clickedItemPosition);
    }
}
