package com.keepfun.aiservice.skin.loader;

import android.content.Context;

import com.keepfun.aiservice.constants.YLConstant;

import java.io.File;

import skin.support.load.SkinSDCardLoader;

public class CustomSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        return new File(YLConstant.SKIN_PATH, skinName).getAbsolutePath();
    }

    @Override
    public String getTargetResourceEntryName(Context context, String skinName, int resId) {
        return super.getTargetResourceEntryName(context, skinName, resId);
    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}
