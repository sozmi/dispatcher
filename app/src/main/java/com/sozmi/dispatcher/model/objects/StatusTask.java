package com.sozmi.dispatcher.model.objects;

import com.sozmi.dispatcher.R;

public enum StatusTask {
    executed,
    execute,
    wait,
    not_executed;

    public int toColor() {
        switch (this) {
            case executed:
                return R.drawable.share_blue;
            case execute:
                return R.drawable.share_green;
            case wait:
                return R.drawable.share_yellow;
            case not_executed:
                return R.drawable.share_red;
            default:
                return 0;
        }
    }
}
