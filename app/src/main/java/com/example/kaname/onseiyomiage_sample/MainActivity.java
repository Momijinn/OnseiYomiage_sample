package com.example.kaname.onseiyomiage_sample;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    //音声読み上げのセット
    private TextToSpeech Textspeech;

    //ButtonやEditTextについて
    private Button InputButton;
    private EditText InputEdittext;

    //オウム返しボタン
    private Button OumugaeshiButton;
    static int INPUT_SPEECH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextToSpeechオブジェクトの生成
        Textspeech = new TextToSpeech(this, this);

        InputButton = (Button)findViewById(R.id.inputbutton);
        InputEdittext = (EditText)findViewById(R.id.inputedit);
        OumugaeshiButton = (Button)findViewById(R.id.oumu_buton);

        InputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String read_string = InputEdittext.getText().toString();

                if (Textspeech.isSpeaking()){ //読み上げ途中なら停止させる
                    Textspeech.stop();
                }
                Textspeech.speak(read_string, TextToSpeech.QUEUE_FLUSH,null); //これで話す
            }
        });


        OumugaeshiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Textspeech.isSpeaking()){ //読み上げ途中なら停止させる
                    Textspeech.stop();
                }
                Speech();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 言語設定
     * @param status
     */
    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status){
            Locale local = Locale.JAPAN; //多分言語設定
            if (Textspeech.isLanguageAvailable(local) >= TextToSpeech.LANG_AVAILABLE){
                Textspeech.setLanguage(local);
            }else{
                Log.e("","Error SetLocale");
            }
        }else{
            Log.e("","Error Init");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INPUT_SPEECH && resultCode == RESULT_OK){
            String ResultText = "";
            // 結果文字列リスト
            ArrayList<String> ret = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ResultText = ret.get(0);
            InputEdittext.setText(ResultText);

            Textspeech.speak(ResultText, TextToSpeech.QUEUE_FLUSH,null); //これで話す
        }
    }

    /**
     * 音声入力
     */
    private void Speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,"音声認識実行中");
        startActivityForResult(intent, INPUT_SPEECH);
    }
}
