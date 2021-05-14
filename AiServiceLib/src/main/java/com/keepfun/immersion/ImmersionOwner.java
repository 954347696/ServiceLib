//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.keepfun.immersion;

public interface ImmersionOwner {
    void onLazyBeforeView();

    void onLazyAfterView();

    void onVisible();

    void onInvisible();

    void initImmersionBar();

    boolean immersionBarEnabled();
}
