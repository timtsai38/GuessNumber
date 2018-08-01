package com.example.user.guessnumber;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 2018/3/31.
 */

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumberViewHolder> {
    private static final String TAG = NumbersAdapter.class.toString();

    public final int DEFAULT_LOWER_LIMIT_POSITION = 0;
    public final int DEFAULT_ANSWER_POSITION = -1;

    private int mNumbersRange;
    private int mAnswerPosition = DEFAULT_ANSWER_POSITION;
    private int mUpperLimitPosition;
    private int mLowerLimitPosition;

    private Drawable mNormalBackground;
    private Drawable mCorrectBackground;
    private Drawable mIncorrctBackground;

    final private ItemClickListener mOnCliclListener;

    public NumbersAdapter(int numbersRange,ItemClickListener onCliclListener ) {
        mNumbersRange = numbersRange;
        mOnCliclListener = onCliclListener;
        mUpperLimitPosition = mNumbersRange - 1;
        mLowerLimitPosition = DEFAULT_LOWER_LIMIT_POSITION;
        Log.d(TAG, mLowerLimitPosition + "-" + mUpperLimitPosition);
    }

    public void setNumbersRange(int numbersRange){
        mNumbersRange = numbersRange;
        Log.d(TAG, mLowerLimitPosition + "-" + mUpperLimitPosition);
    }

    public void setAnswerPosition(int answerPosition){
        mAnswerPosition = answerPosition;
        Log.d(TAG,"BINGO!");
    }

    public void setUpperLimitPosition(int upperLimitPosition){
        mUpperLimitPosition = upperLimitPosition;
        Log.d(TAG, mLowerLimitPosition + "-" + mUpperLimitPosition);
    }

    public int getUpperLimitPosition(){
        return mUpperLimitPosition;
    }

    public void setLowerLimitPosition(int lowerLimitPosition){
        mLowerLimitPosition = lowerLimitPosition;
        Log.d(TAG, mLowerLimitPosition + "-" + mUpperLimitPosition);
    }


    public int getLowerLimitPosition(){
        return mLowerLimitPosition;
    }

    public void setDrawbleResource(Drawable normalBackground, Drawable correctBackground, Drawable incorrctBackground){
        mNormalBackground = normalBackground;
        mCorrectBackground = correctBackground;
        mIncorrctBackground = incorrctBackground;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_number, parent, false);

        NumberViewHolder numberViewHolder = new NumberViewHolder(itemView);

        return numberViewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        int number = position + 1;
        holder.mNumberTextView.setText(String.valueOf(number));
        if(position == mAnswerPosition){
            holder.mNumberTextView.setBackground(mCorrectBackground);
        }else if(position >= mLowerLimitPosition && position <= mUpperLimitPosition){
            holder.mNumberTextView.setBackground(mNormalBackground);
        }else {
            holder.mNumberTextView.setBackground(mIncorrctBackground);
        }
    }

    @Override
    public int getItemCount() {
        return mNumbersRange;
    }

    interface ItemClickListener{
        void onItemClick(int clickedItemPosition);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNumberTextView;

        public NumberViewHolder(View itemView) {
            super(itemView);
            mNumberTextView = (TextView) itemView.findViewById(R.id.tv_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnCliclListener.onItemClick(clickedPosition);
        }
    }
}
