package org.baseballbaedal.baseballbaedal;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-08-008.
 */


public enum PushUtil {
    INSTANCE;

    public static PushUtil getInstance() {
        return INSTANCE;
    }
//    private static PushUtil pushUtil = new PushUtil();
//
//    private PushUtil(){}
//
//    public static PushUtil getInstance() {
//        return pushUtil;
//    }

    //푸쉬 메세지 발송 기능 함수
    public void send(String title, String content, String type, String regId, RequestQueue queue) {

        JSONObject requestData = new JSONObject();

        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();
            dataObj.put("title", title);
            dataObj.put("content", content);
            dataObj.put("type", type);
            requestData.put("data", dataObj);

            JSONArray idArray = new JSONArray();
            idArray.put(0, regId);
            requestData.put("registration_ids", idArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
            }

            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestWithError(VolleyError error) {
            }
        }, queue);

    }

    public interface SendResponseListener {
        public void onRequestStarted();

        public void onRequestCompleted();

        public void onRequestWithError(VolleyError error);
    }

    public static void sendData(JSONObject requestData, final SendResponseListener listener, RequestQueue queue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAA1wB4ltE:APA91bGdXJDV8jpxBB2ivWYSgpHf6_NBme0Qc4V6MWMatxpl0lywMNC3N-kKPT_BN3rgvGXcNQ-1YNB0QB9zY-2O391qgIlzyn3uxuuhkdQjSJwT9aPT-30CBcciP04E_OQOHZ4WbplJ");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        queue.add(request);
    }
}
