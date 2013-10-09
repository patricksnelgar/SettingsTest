package com.example.settingstest;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class NumberPickerPreference extends DialogPreference {
	
	private static final int DEFAULT_VALUE = 0;
	private int mNewValue = 0;
	private int mCurrentValue = 0;
	
	public NumberPickerPreference(Context context, AttributeSet attrs){
		super(context, attrs);
		
		setDialogLayoutResource(R.layout.numberpickerpreference_layout);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}
	
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue){
		if(restorePersistedValue){
			mCurrentValue = (Integer) defaultValue;
		} else {
			mCurrentValue = (Integer) defaultValue;
			persistInt(mCurrentValue);
		}
	}
	
	protected Object onGetDefaultValue(TypedArray a, int index){
		return a.getInteger(index, DEFAULT_VALUE);
	}
	
	protected void onDialogClosed(boolean positiveResult){
		if(positiveResult){
			persistInt(mNewValue);
		}
	}
}
