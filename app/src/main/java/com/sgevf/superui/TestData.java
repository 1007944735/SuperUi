package com.sgevf.superui;

import com.sgevf.ui.pickerView.MyPickerView;

public class TestData implements MyPickerView.IPickerData {
    @Override
    public String showString() {
        return "测试数据";
    }
}
