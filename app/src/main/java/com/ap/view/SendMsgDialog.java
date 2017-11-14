package com.ap.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ap.R;
import com.ap.base.BaseTopDialog;

/**
 * 类描述：
 * 创建人：swallow.li
 * 创建时间：
 * Email: swallow.li@kemai.cn
 * 修改备注：
 */
public class SendMsgDialog extends BaseTopDialog {

    public SendMsgDialog(Context context, ISendMsgListener iSendMsgListener) {
        super(context);
        this.iSendMsgListener = iSendMsgListener;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sendmsg, null);
        final EditText msg = (EditText) view.findViewById(R.id.et_msg);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != iSendMsgListener) iSendMsgListener.cancel();
            }
        });
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != iSendMsgListener) iSendMsgListener.sendMsg(msg.getText().toString());
            }
        });
        setContentView(view);
    }

    private ISendMsgListener iSendMsgListener;

    public interface ISendMsgListener {
        void sendMsg(String msg);

        void cancel();
    }

}
