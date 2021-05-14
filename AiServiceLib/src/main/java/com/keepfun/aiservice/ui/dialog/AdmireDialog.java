package com.keepfun.aiservice.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keepfun.aiservice.R;
import com.keepfun.aiservice.constants.ApiDomain;
import com.keepfun.aiservice.constants.Arguments;
import com.keepfun.aiservice.constants.ChatConst;
import com.keepfun.aiservice.entity.AccountInfo;
import com.keepfun.aiservice.global.GlobalDataHelper;
import com.keepfun.aiservice.network.OkHttpUtils;
import com.keepfun.aiservice.network.myokhttp.response.GsonResponseHandler;
import com.keepfun.aiservice.ui.adapter.AdmireAdapter;
import com.keepfun.aiservice.ui.divider.HorizontalDividerItemDecoration;
import com.keepfun.aiservice.ui.impl.CheckClickListener;
import com.keepfun.base.PanDialogFragment;
import com.keepfun.blankj.util.SizeUtils;
import com.keepfun.blankj.util.StringUtils;
import com.keepfun.blankj.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 打赏弹框<p>
 *
 * @author yang
 * @since 2020/12/9 17:30
 */
public class AdmireDialog extends PanDialogFragment implements View.OnClickListener {

    private TextView tv_cancel;
    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_rest_coin;
    private EditText et_input;
    private RecyclerView rv_admire;
    private View layout_input;

    private TextView tv_tip;
    private AdmireAdapter mAdmireAdapter;

    private int type;
    private String groupId;
    private String mSessionId;
    private String memberId;

    @Override
    public int getLayoutId() {
        return R.layout.service_dialog_admire;
    }

    @Override
    public void bindUI(View rootView) {
        tv_cancel = rootView.findViewById(R.id.tv_cancel);
        iv_avatar = rootView.findViewById(R.id.iv_avatar);
        tv_nickname = rootView.findViewById(R.id.tv_nickname);
        layout_input = rootView.findViewById(R.id.layout_input);
        tv_tip = rootView.findViewById(R.id.tv_tip);
        tv_rest_coin = rootView.findViewById(R.id.tv_rest_coin);
        et_input = rootView.findViewById(R.id.et_input);
        rv_admire = rootView.findViewById(R.id.rv_admire);
        rootView.findViewById(R.id.tv_admire).setOnClickListener(new CheckClickListener(this));
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(Arguments.TYPE);
            groupId = bundle.getString(Arguments.GROUP_ID);
            mSessionId = bundle.getString(Arguments.SESSION_ID);
            memberId = bundle.getString(Arguments.ID);
            String avatar = bundle.getString(Arguments.DATA);
            Glide.with(getContext()).load(avatar).placeholder(R.mipmap.service_bg_chat_default).into(iv_avatar);
            String nickname = bundle.getString(Arguments.DATA1);
            tv_nickname.setText(nickname);
        }


        initRecyclerView();

        getAccountInfo();
    }

    @Override
    public void bindEvent() {
        tv_cancel.setOnClickListener(new CheckClickListener(this));
    }

    public void setRestCoin(String coin) {
        tv_rest_coin.setText(coin);
    }

    private void initRecyclerView() {

        rv_admire.setLayoutManager(new GridLayoutManager(getContext(), 3));
        if (mAdmireAdapter == null) {
            RecyclerView.ItemDecoration decor = new HorizontalDividerItemDecoration.Builder(getContext())
                    .colorResId(R.color.transport)
                    .size(SizeUtils.dp2px(8f))
                    .build();
            rv_admire.addItemDecoration(decor);

            List<String> data = new ArrayList<>();
            data.add("1");
            data.add("2");
            data.add("3");
            data.add("5");
            data.add("10");
            data.add("自定义");
            mAdmireAdapter = new AdmireAdapter(data);
            rv_admire.setAdapter(mAdmireAdapter);

            mAdmireAdapter.setOnLabelSelectedListener(new AdmireAdapter.OnLabelSelectedListener() {
                @Override
                public void onLabelSelected(String label) {
                    layout_input.setVisibility("自定义".equals(label) ? View.VISIBLE : View.GONE);
                }
            });
        }
    }


    private void getAccountInfo() {
        OkHttpUtils.get(ApiDomain.GET_ACCOUNT_INFO, new GsonResponseHandler<AccountInfo>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (accountInfo != null) {
                    setRestCoin(accountInfo.getAmountStr() + accountInfo.getCurrency() + " (可兑换" + accountInfo.getExchangeAmountStr() + accountInfo.getCsCurrency() + ")");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_admire) {
            String count;
            if (layout_input.getVisibility() == View.VISIBLE) {
                count = et_input.getText().toString().trim();
            } else {
                count = mAdmireAdapter.getSelectLabel();
            }
            if (StringUtils.isEmpty(count)) {
                ToastUtils.showShort("打赏金额不能为空");
                return;
            }
            admire(count);
        }

    }

    public void admire(String amount) {
        List<String> ids = new ArrayList<>();
        ids.add(memberId);
        HashMap<String, Object> params = new HashMap<>();
        params.put("amount", amount);// 打赏金额 ,
        params.put("csUserIds", ids);    // 客服/主播id集合 ,
        params.put("imGroupId", mSessionId);// IM返回的群组id，用来发送IM消息 ,
        params.put("sourceId", groupId);//来源id: 私聊传会话id；群聊传群id；直播间传直播间id,
        params.put("sourceType", type);//来源类型 1 - 单聊 2 - 群聊 3 - 直播间,
        params.put("token", GlobalDataHelper.getInstance().getImToken());//聊天token
        OkHttpUtils.postJson(ApiDomain.REDPACKET_REWARD, params, new GsonResponseHandler<Boolean>() {
            @Override
            public void onFailure(String statusCode, String error_msg) {
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("操作失败，" + error_msg);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result != null && result) {
                    ToastUtils.showShort("打赏成功");
                    tv_tip.setVisibility(View.GONE);
                    dismiss();
                } else {
                    tv_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("操作失败，" + result);
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

        cleanUI();
    }

    private void cleanUI() {
        if (mAdmireAdapter != null) {
            mAdmireAdapter.setSelectPosition(0);
            mAdmireAdapter.notifyDataSetChanged();
        }
        if (et_input != null) {
            et_input.setText("");
        }
        if (layout_input != null) {
            layout_input.setVisibility(View.GONE);
        }
    }
}
