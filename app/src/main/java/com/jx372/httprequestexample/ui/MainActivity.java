package com.jx372.httprequestexample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;
import com.jx372.httprequestexample.R;
import com.jx372.httprequestexample.core.domain.GuestbookVo;
import com.jx372.httprequestexample.network.JSONResult;
import com.jx372.httprequestexample.network.SafeAsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFetchGuestBookClick(View view){
        new FetchGuestbookListAsynceTask().execute(); // thread 위에 call
    }
    //제네릭을 사용하면 JSONResult 클래스 상속받아 사용
    //private class JSONResultGuestBookList extends JSONResult<List<GuestbookVo>> {}

    //통신 결과를 담을 result 클래스 (API당 하나씩)
    private class JSONResultFectchGeustbookList{
        private String result ;
        private String message ;
        private List<GuestbookVo> data;

        public List<GuestbookVo> getData() {
            return data;
        }

        public void setData(List<GuestbookVo> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
    //통신 내부 클래스 API 하나당 하나씩
    private class FetchGuestbookListAsynceTask extends SafeAsyncTask<List<GuestbookVo>>{

        @Override
        public List<GuestbookVo> call() throws Exception {
            //1.요청 세팅
            String url = "http://192.168.1.36:8088/mysite03/guestbook/api/list";
            HttpRequest request = HttpRequest.get(url);

            //보낼때
            //"name=name&no=1"
            //request.contentType(HttpRequest.CONTENT_TYPE_FORM);
            //"{name:name, no:1}"
            //request.contentType(HttpRequest.CONTENT_TYPE_JSON);

            //2.요청
            request.accept(HttpRequest.CONTENT_TYPE_JSON);
            request.connectTimeout(3000);
            request.readTimeout(3000);

            //3.응답처리
            int responseCode = request.code();
            if(responseCode != HttpURLConnection.HTTP_OK){
                //오류 처리
                throw new RuntimeException("Http Reseponse Error"+responseCode);
            }

//            BufferedReader br = request.bufferedReader();
//            String json = "";
//            String line = null;
//            try {
//                while( (line=br.readLine() ) !=null){
//                    json+=line;
//                }
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            // 4.GSon을 사용한 객체 생성
            Reader reader = request.bufferedReader();
            JSONResultFectchGeustbookList jsonResult =
                    new GsonBuilder().create().fromJson(reader,JSONResultFectchGeustbookList.class);
            //5. 결과 에러
            if("fail".equals(jsonResult.getResult())){
                throw new RuntimeException(jsonResult.getMessage());
            }
            return jsonResult.getData();
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Log.e("FetchGuestBook", "Exception:"+e);
            super.onException(e);
        }

        @Override
        protected void onSuccess(List<GuestbookVo> list) throws Exception {
            super.onSuccess(list);
            for(GuestbookVo guestbookvo: list)
            System.out.println(guestbookvo);
        }
    }
}
