package com.keepfun.login.ui.act;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keepfun.aiservice.threads.YLPoolExecutor;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.base.PanActivity;
import com.keepfun.blankj.util.LogUtils;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.login.utils.HanziToPinyin;
import com.keepfun.login.R;
import com.keepfun.login.entity.GlCountryEntity;
import com.keepfun.login.net.KeepfunCountryTask;
import com.keepfun.login.ui.adapter.SelectCountryAdapter;
import com.keepfun.login.ui.view.MyCitySiderBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author yang
 * @description
 * @date 2020/12/25 10:14 AM
 */
public class SelectCountryActivity extends PanActivity {

    RecyclerView rv_country;
    MyCitySiderBar sidebar;

    private List<GlCountryEntity> countryList;
    private SelectCountryAdapter mAdapter;

    private List<GlCountryEntity> siderList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_country;
    }

    @Override
    public void bindUI(View rootView) {
        rv_country = findViewById(R.id.rv_country);
        sidebar = findViewById(R.id.sidebar);
    }

    @Override
    public void initData() {

        rv_country.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.color_F9F8FB)
                .size(SizeUtils.dp2px(1))
                .build();
        rv_country.addItemDecoration(decor);

        if (countryList == null) {
            countryList = new ArrayList<>();
        }
        mAdapter = new SelectCountryAdapter(countryList);
        mAdapter.setOnCountrySelectedListener(countryEntity -> {
            Intent intent = new Intent();
            intent.putExtra("country", countryEntity);
            setResult(RESULT_OK, intent);
            finish();
        });
        rv_country.setAdapter(mAdapter);

        /***************设置标题和侧边栏**********************/
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        siderList = new ArrayList<>();
        for (String str : letters) {
            GlCountryEntity cityBean = new GlCountryEntity();
            cityBean.setItemType(GlCountryEntity.TYPE_TITLE);
            cityBean.setPinyin(str);
            cityBean.setCnName(str);
            siderList.add(cityBean);
        }

        initSideBar();
        getCountries();
    }

    public void initSideBar() {
        sidebar.setLetters(siderList);
        sidebar.setOnTouchingLetterChangedListener(cityBean -> {
            //
            ((LinearLayoutManager) rv_country.getLayoutManager()).scrollToPositionWithOffset(cityBean.getScrollPosition(), 0);
        });
    }


    @Override
    public void bindEvent() {

    }

    private void getCountries() {
        FutureTask<List<GlCountryEntity>> futureTask = new FutureTask<>(new KeepfunCountryTask());
        YLPoolExecutor.getInstance().execute(futureTask);

        try {
            List<GlCountryEntity> countries = futureTask.get();
            if (countries != null) {
                for (GlCountryEntity glCountryEntity : countries) {
                    List<HanziToPinyin.Token> pinyin = HanziToPinyin.getInstance().getTokens(glCountryEntity.getCnName());
                    if (pinyin != null && pinyin.size() > 0) {
                        glCountryEntity.setPinyin(pinyin.get(0).target);
                    }

                }
                compareCity(countries);

                LogUtils.e("countries  1 : " + countries.size());
                List<GlCountryEntity> siderList1 = new ArrayList<>();
                List<GlCountryEntity> newCountryList = new ArrayList<>();
                for (GlCountryEntity cityBean1 : countries) {
                    for (GlCountryEntity cityBean : siderList) {
                        if (cityBean1.getPinyin().toUpperCase().startsWith(cityBean.getPinyin().toUpperCase())) {
                            if (!newCountryList.contains(cityBean)) {
                                newCountryList.add(cityBean);
                                siderList1.add(cityBean);
                            }
                            break;
                        }
                    }
                    newCountryList.add(cityBean1);
                }
                sidebar.setLetters(siderList1);
                countryList.clear();
                countryList.addAll(newCountryList);
                LogUtils.e("countries  2 : " + countryList.size());
                setFinalList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFinalList() {
        for (int i = 0; i < countryList.size(); i++) {
            GlCountryEntity cityBean = countryList.get(i);
            cityBean.setScrollPosition(i);
        }
        mAdapter.notifyDataSetChanged();
    }

    public List<GlCountryEntity> compareCity(List<GlCountryEntity> beanList) {
        Collections.sort(beanList, comparator);
        return beanList;
    }

    private Comparator comparator = (Comparator<GlCountryEntity>) (o1, o2) -> {
        if (o1 == null || o2 == null) {
            return 0;
        }
        String lhsSortLetters = o1.getPinyin().toUpperCase();
        String rhsSortLetters = o2.getPinyin().toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    };

}
