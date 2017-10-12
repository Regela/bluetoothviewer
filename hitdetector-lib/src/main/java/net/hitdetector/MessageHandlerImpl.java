package net.hitdetector;

import android.os.Handler;

import android.util.SparseIntArray;
import com.google.gson.Gson;

public class MessageHandlerImpl implements MessageHandler {
    private final Handler handler;

    private class hit{
        public SparseIntArray A = new SparseIntArray();
        public SparseIntArray P = new SparseIntArray();
    }
    
    private class result{
        public Integer MaxA;
        public Integer MaxP;
        public Integer dTa;// Delta T of a
        public Integer dTp;// Delta T of p
    }
    
    private result res(hit Hit){
        result r = new result();
        r.MaxA = Hit.A.valueAt(0);
        r.MaxP = Hit.P.valueAt(0);
        for (int i = 1; i < Hit.A.size(); i++) {
            if(r.MaxA<Hit.A.valueAt(i)){
                r.MaxA=Hit.A.valueAt(i);
            }
        }
        for (int i = 1; i < Hit.P.size(); i++) {
            if(r.MaxP<Hit.P.valueAt(i)){
                r.MaxP=Hit.P.valueAt(i);
            }
        }
        int minTp,maxTp,minTa,maxTa;
        minTp=maxTp=minTa=maxTa=0;

        for (int i = 1; i < Hit.A.size(); i++) {
            if(maxTa<Hit.A.keyAt(i)){
                maxTa=Hit.A.keyAt(i);
            }
            if(minTa>Hit.A.keyAt(i)){
                minTa=Hit.A.keyAt(i);
            }

        }
        for (int i = 1; i < Hit.P.size(); i++) {
            if(maxTp<Hit.P.keyAt(i)){
                maxTp=Hit.P.keyAt(i);
            }
            if(minTp>Hit.P.keyAt(i)){
                minTp=Hit.P.keyAt(i);
            }
        }
        r.dTa=maxTa-minTa;
        r.dTp=maxTp-minTp;
        return r;
    }
    
    private String msg = "";

    public MessageHandlerImpl(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void sendLineRead(String line) {
        msg+=line;
        Boolean completed=true;
        Gson gson = new Gson();
        hit Hit=new hit();
        try {
            Hit = gson.fromJson(msg,hit.class);
        } catch(com.google.gson.JsonSyntaxException e){
            completed=false;
        }

        if(completed) {
            if (Hit==null || Hit.A==null || Hit.P==null || Hit.A.size()<=0 || Hit.P.size()<=0){
                completed=false;
                msg="";
            }
        }

        //line=Integer.toString(Hit.A.keyAt(10));

//        Hit.A.append(1,1);
//        Hit.A.append(2,2);
//        Hit.A.append(3,3);
//        Hit.A.append(4,4);
//
//
//        Hit.P.append(1,1);
//        Hit.P.append(2,2);
//        Hit.P.append(3,3);
//        Hit.P.append(4,4);
//
//        line=gson.toJson(Hit);
        if(completed){
            result r = res(Hit);
            handler.obtainMessage(MSG_LINE_READ, -1, -1, "MaxA=\t" + r.MaxA).sendToTarget();
            handler.obtainMessage(MSG_LINE_READ, -1, -1, "MaxP=\t" + r.MaxP).sendToTarget();
            handler.obtainMessage(MSG_LINE_READ, -1, -1, "dTa=\t" + r.dTa).sendToTarget();
            handler.obtainMessage(MSG_LINE_READ, -1, -1, "dTp=\t" + r.dTp).sendToTarget();
            msg="";
        }
    }
    //{"A":{"mKeys":[1,2,3,4,0,0,0,0,0,0,0,0,0],"mValues":[1,2,3,4,0,0,0,0,0,0,0,0,0],"mSize":4},"P":{"mKeys":[1,2,3,4,0,0,0,0,0,0,0,0,0],"mValues":[1,2,3,4,0,0,0,0,0,0,0,0,0],"mSize":4}}

    @Override
    public void sendBytesWritten(byte[] bytes) {
        handler.obtainMessage(MSG_BYTES_WRITTEN, -1, -1, bytes).sendToTarget();
    }

    @Override
    public void sendConnectingTo(String deviceName) {
        sendMessage(MSG_CONNECTING, deviceName);
    }

    @Override
    public void sendConnectedTo(String deviceName) {
        sendMessage(MSG_CONNECTED, deviceName);
    }

    @Override
    public void sendNotConnected() {
        sendMessage(MSG_NOT_CONNECTED);
    }

    @Override
    public void sendConnectionFailed() {
        sendMessage(MSG_CONNECTION_FAILED);
    }

    @Override
    public void sendConnectionLost() {
        sendMessage(MSG_CONNECTION_LOST);
    }

    private void sendMessage(int messageId, String deviceName) {
        handler.obtainMessage(messageId, -1, -1, deviceName).sendToTarget();
    }

    private void sendMessage(int messageId) {
        handler.obtainMessage(messageId).sendToTarget();
    }
}
