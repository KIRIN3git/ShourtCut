package com.example.shinji_win.shourtcut;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gotoNextIntent();

        Button button = (Button) findViewById(R.id.os7button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShortcutDialog();
            }
        });
    }

    private void gotoNextIntent() {
        Bundle extras = getIntent().getExtras();
        String className = null;

        if(extras != null && extras.containsKey("shortcuttype")) {
            className = extras.getString("shortcuttype");
        }
        else{
            return;
        }

        Intent mNextIntent;
        mNextIntent = new Intent();
        mNextIntent.setAction(Intent.ACTION_MAIN);
        mNextIntent.setClassName("com.example.shinji_win.shourtcut", className);
        startActivity(mNextIntent);

    }

    private void showCreateShortcutDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View shortcutView = factory.inflate(R.layout.shortcut_dialog, null);
        final CheckBox checkBoxTimetable = (CheckBox)shortcutView.findViewById(R.id.timetable);
        final CheckBox checkBoxLive = (CheckBox)shortcutView.findViewById(R.id.live);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setView(shortcutView);
        dlg.setIcon(R.drawable.ic_shortcut);
        dlg.setTitle("ショートカット作成");
        dlg.setPositiveButton("作成", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                createShortcutLink(checkBoxTimetable.isChecked(), checkBoxLive.isChecked());
            }
        });
        dlg.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if (!isFinishing()) {
            dlg.show();
        }
    }


    private void createShortcutLink(boolean _timetable, boolean _live) {
        if (!_timetable && !_live) {
            Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_LONG).show();
            return;
        }

        if (_live) {
            createShortcutDetail(getApplicationContext(),
                    Intent.ACTION_VIEW,
                    MainActivity.class.getName(),
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK,
                    Intent.CATEGORY_LAUNCHER,
                    new String[]{"shortcuttype"},
                    new String[]{"com.example.shinji_win.shourtcut.LiveActivity"},
                    null,
                    null,
                    null,
                    null,
                    R.drawable.ic_shortcut_1,
                    "ライブ!",
                    "Live createShortcut ",
                    "");
        }

        if (_timetable) {
            createShortcutDetail(getApplicationContext(),
                    Intent.ACTION_VIEW,
                    MainActivity.class.getName(),
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK,
                    Intent.CATEGORY_LAUNCHER,
                    new String[]{"shortcuttype"},
                    new String[]{"com.example.shinji_win.shourtcut.TimetableActivity"},
                    null,
                    null,
                    null,
                    null,
                    R.drawable.ic_shortcut_2,
                    "時刻表",
                    "TrainDiagram createShortcut ",
                    "");
        }

    }

    /**
     * ショートカット作成
     * @param _context
     * @param _action インテントアクション
     * @param _className 起動クラス
     * @param _flags 起動フラグ
     * @param _category 起動カテゴリ
     * @param _stringName putExtraの文字列データの名前の配列
     * @param _stringValue putExtraの文字列データの値の配列
     * @param _intName putExtraの数値データの名前の配列
     * @param _intValue putExtraの数値データの値の配列
     * @param _booleanName putExtraの真偽データの名前の配列
     * @param _booleanValue putExtraの真偽データの値の配列
     * @param _drawable ショートカットアイコン
     * @param _shortcutName ショートカット名
     * @param _GAaction Googleアナリティクスアクション文字列
     * @param _GAlabel Googleアナリティクスラベル文字列
     */
    public static void createShortcutDetail(Context _context,
                                      String _action,
                                      String _className,
                                      int _flags,
                                      String _category,
                                      String[] _stringName,
                                      String[] _stringValue,
                                      String[] _intName,
                                      int[] _intValue,
                                      String[] _booleanName,
                                      boolean[] _booleanValue,
                                      int _drawable,
                                      String _shortcutName,
                                      String _GAaction,
                                      String _GAlabel){
        Intent intent = new Intent(_action);
        intent.setClassName(_context, _className);

        if(_flags > 0){
            intent.setFlags(_flags);
        }

        intent.addCategory(_category);

        //文字列データ
        for(int i = 0; _stringName != null && i < _stringName.length; i++){
            intent.putExtra(_stringName[i], _stringValue[i]);
        }

        //数値データ
        for(int i = 0; _intName != null && i < _intName.length; i++){
            intent.putExtra(_intName[i], _intValue[i]);
        }

        //真偽データ
        for(int i = 0; _booleanName != null && i < _booleanName.length; i++){
            intent.putExtra(_booleanName[i], _booleanValue[i]);
        }

        _context.sendBroadcast(makeShortcutIntent(_context, _drawable, _shortcutName, intent));

    }

    /**
     * ショートカット向けの Intent を返却します
     */
    public static Intent makeShortcutIntent(Context ctx, int shortcutIcon, String shortcutName, Intent shortcutIntent) {
        Parcelable parcelable = Intent.ShortcutIconResource.fromContext(ctx, shortcutIcon);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, parcelable);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("duplicate", false);
        return intent;
    }
}
