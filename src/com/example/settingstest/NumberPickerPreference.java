package com.example.settingstest;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerPreference extends DialogPreference {
	
	private static final int DEFAULT_VALUE = 10;
	private static final int DEFAULT_MAX_VALUE = 500;
	private static final int DEFAULT_MIN_VALUE = 10;
	private NumberPicker mNumberPicker = null;
	
	private int minValue;
	private int maxValue;
	private int mVal;
	
	public NumberPickerPreference(Context context, AttributeSet attrs){
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, 0, 0);		
		
		try {
			setMinValue(a.getInteger(R.styleable.NumberPickerPreference_min, DEFAULT_MIN_VALUE));
			setMaxValue(a.getInteger(R.styleable.NumberPickerPreference_android_max, DEFAULT_MAX_VALUE));
		} finally {
			
		}
		
		setDialogLayoutResource(R.layout.numberpickerpreference_layout);
		setPositiveButtonText(R.string.set);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
		
	}
	
	protected void onSetInitialState(boolean restore, Object def){
		setValue(restore ? getPersistedInt(DEFAULT_VALUE) : (Integer) def);
	}
	
	protected Object onGetDefaultValue(TypedArray a, int index){
		return a.getInt(index, DEFAULT_VALUE);
	}
	
	protected void onBindDialogView(View v){
		super.onBindDialogView(v);
		
		mNumberPicker = (NumberPicker) v.findViewById(R.id.numberpicker);
		mNumberPicker.setMinValue(minValue);
		mNumberPicker.setMaxValue(maxValue);
		mNumberPicker.setValue(mVal);
		mNumberPicker.setWrapSelectorWheel(false);
	}
	
	public int getMinValue(){
		return minValue;
	}
	
	public int getMaxValue(){
		return maxValue;
	}
	
	public void setMinValue(int val){
		minValue = val;
		setValue(Math.max(val, minValue));
	}
	
	public void setMaxValue(int val){
		maxValue = val;
		setValue(Math.min(val, maxValue));
	}
	
	public int getCurrentValue(){
		return mVal;
	}
	
	public void setValue(int value){
		mVal = Math.max(Math.min(value, maxValue),minValue);
		
		if(value!=mVal){
			mVal = value;
			persistInt(value);
			notifyChanged();
		}
	}
	
	protected void onDialogClosed(boolean positiveRes){
		super.onDialogClosed(positiveRes);
		
		if(positiveRes){
			int curVal = mNumberPicker.getValue();
			if(callChangeListener(curVal)){
				setValue(curVal);
			}
		}
	}
	
	protected Parcelable onSavedInstanceState(){
		final Parcelable  superState = super.onSaveInstanceState();
		
		final SavedState myState = new SavedState(superState);
		myState.minValue = getMinValue();
		myState.maxValue = getMaxValue();
		myState.value = getCurrentValue();
		
		return myState;
	}
	
	protected void onRestoreSavedInstanceState(Parcelable state){
		if(state==null || !state.getClass().equals(SavedState.class)){
			super.onRestoreInstanceState(state);
			return;
		} 
		
		SavedState myState = (SavedState) state;
		setMinValue(myState.minValue);
		setMaxValue(myState.maxValue);
		setValue(myState.value);
		
		super.onRestoreInstanceState(myState.getSuperState());
	}
	
	private static class SavedState extends BaseSavedState
    {
        int minValue;
        int maxValue;
        int value;
 
        public SavedState(Parcelable superState)
        {
            super(superState);
        }
 
        public SavedState(Parcel source)
        {
            super(source);
 
            minValue = source.readInt();
            maxValue = source.readInt();
            value = source.readInt();
        }
 
        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
 
            dest.writeInt(minValue);
            dest.writeInt(maxValue);
            dest.writeInt(value);
        }
 
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
        {
            @Override
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }
 
            @Override
            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }
}
