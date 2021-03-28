package com.sgevf.ui.pickerView;

import java.util.List;

public interface IPicker {

    <T extends MyPickerView.IPickerData> void setData(List<T> list);

    <T extends MyPickerView.IPickerData> void setData(int position, List<T> list);

    void setOnPickerSelectListener(MyPickerView.OnPickerSelectListener listener);
}
