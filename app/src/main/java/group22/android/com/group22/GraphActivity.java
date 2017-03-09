package group22.android.com.group22;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import group22.android.com.group22.helper.DatabaseHandler;
import group22.android.com.group22.helper.SensorHandler;
import group22.android.com.group22.helper.UploadAsync;

public class GraphActivity extends AppCompatActivity {
    private static final String TAG = "GraphActivity";

    GraphView myViewx;
    GraphView myViewy;
    GraphView myViewz;
    String xlabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
    String ylabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
    float xvalues[];
    float yvalues[];
    float zvalues[];

    private final static int INTERVAL = 1000; //millisecs
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        final EditText patient_name = (EditText) findViewById(R.id.patient_name);
        final RadioGroup patient_sex = (RadioGroup) findViewById(R.id.radioSex);
        final EditText patient_age = (EditText) findViewById(R.id.age);
        final EditText patient_id = (EditText) findViewById(R.id.patient_id);

        final Intent intent = new Intent(this, SensorHandler.class);

        mHandler = new Handler();

        RelativeLayout graphlayoutx = (RelativeLayout) findViewById(R.id.graphPlotx);
        graphlayoutx.removeAllViews();
        RelativeLayout.LayoutParams graphParamx = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myViewx = new GraphView(GraphActivity.this,
                new float[0][0], "Group 22", xlabels, ylabels, true);
        myViewx.setLayoutParams(graphParamx);
        ((RelativeLayout) findViewById(R.id.graphPlotx)).addView(myViewx);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation(patient_id, patient_name, patient_age, patient_sex)) {
                    startService(intent);

                    startRepeatingTask();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();
                clearGraph();
            }
        });

        final Button upload = (Button) findViewById(R.id.uplaod);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadAsync uploadAsync = new UploadAsync(GraphActivity.this);
                uploadAsync.execute();
            }
        });

        final Button download = (Button) findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsync dwnldAsync = new DownloadAsync(GraphActivity.this);
                clearGraph();
                dwnldAsync.execute();
            }
        });
    }


    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            setGraph();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask() {
        mHandlerTask.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    void setGraph() {
        DatabaseHandler hdb = new DatabaseHandler(GraphActivity.this);
        SQLiteDatabase db = hdb.getReadableDatabase();
        float outValues[][] = hdb.get10records(db);

        xvalues = new float[outValues.length];
        yvalues = new float[outValues.length];
        zvalues = new float[outValues.length];

        Log.d(TAG, " list size is   " + outValues.length);
        for (int i = 0; i < outValues.length; i++) {
            xvalues[i] = outValues[i][0];
            yvalues[i] = outValues[i][1];
            zvalues[i] = outValues[i][2];
            Log.d(TAG, "  " + outValues[i][0] + "  " + outValues[i][1] + "   " + outValues[i][2]);
        }

        myViewx = new GraphView(GraphActivity.this, outValues, "Group 22", xlabels, ylabels, true);
        RelativeLayout graphlayoutx = (RelativeLayout) findViewById(R.id.graphPlotx);
        graphlayoutx.removeAllViews();
        RelativeLayout.LayoutParams graphParamx = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myViewx.setLayoutParams(graphParamx);
        ((RelativeLayout) findViewById(R.id.graphPlotx)).addView(myViewx);
    }


    void clearGraph() {
        myViewx = new GraphView(GraphActivity.this, new float[0][0], "Group 22", xlabels, ylabels, true);
        RelativeLayout graphlayoutx = (RelativeLayout) findViewById(R.id.graphPlotx);
        graphlayoutx.removeAllViews();
        RelativeLayout.LayoutParams graphParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myViewx.setLayoutParams(graphParam);
        ((RelativeLayout) findViewById(R.id.graphPlotx)).addView(myViewx);
    }


    private boolean inputValidation(EditText patient_id, EditText patient_name,
                                    EditText patient_age, RadioGroup patient_sex) {

        if (patient_id.getText().toString().trim().equals("")) {
            Toast.makeText(GraphActivity.this, "Patient Id cannot be null", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (patient_name.getText().toString().equals("")) {
            Toast.makeText(GraphActivity.this, "Patient name cannot be null", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (patient_age.getText().toString().equals("")) {
            Toast.makeText(GraphActivity.this, "Age cannot be null", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (patient_sex.getCheckedRadioButtonId() != R.id.radioMale && patient_sex.getCheckedRadioButtonId() != R.id.radioFemale) {
            Toast.makeText(GraphActivity.this, "Please select Male or Female", Toast.LENGTH_SHORT).show();
            return false;
        }
        String tablename = patient_name.getText().toString() + "_" + patient_id.getText().toString() + "_" + patient_age.getText().toString();
        if (patient_sex.getCheckedRadioButtonId() == R.id.radioMale) {
            tablename = tablename + "_male";
        } else
            tablename = tablename + "_female";

        return true;
    }


    class DownloadAsync extends AsyncTask<Void, Void, Void> {
        public Context context;
        GraphView myViewx;

        String xlabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
        String ylabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};

        public DownloadAsync(Context context) {
            this.context = context;
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Downloading", "Please wait ...", true);
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            Toast.makeText(context, "Download Successful", Toast.LENGTH_SHORT).show();
            //clearGraph();
            String path = Environment.getExternalStorageDirectory().toString() + "/Group22.db";

            DatabaseHandler hdb = new DatabaseHandler(context);
            SQLiteDatabase db = hdb.getReadableDatabase();
            float outValues[][] = hdb.get10records(db);

            myViewx = new GraphView(context, outValues, "Group 22", xlabels, ylabels, true);
            RelativeLayout graphlayoutx = (RelativeLayout) findViewById(R.id.graphPlotx);
            graphlayoutx.removeAllViews();
            RelativeLayout.LayoutParams graphParamx = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            myViewx.setLayoutParams(graphParamx);
            ((RelativeLayout) findViewById(R.id.graphPlotx)).addView(myViewx);
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileDownload();
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        public void fileDownload() {  //this is the downloader method
            try {

                File root = android.os.Environment.getExternalStorageDirectory();

                File dir = new File (root.getAbsolutePath() + "/xmls");
                if(dir.exists()==false) {
                    dir.mkdirs();
                }


                URL url = new URL("https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php");
                File file = new File(dir, "Group22.db");
                URLConnection conection = url.openConnection();
                conection.connect();

                InputStream input = conection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                byte data[] = new byte[1024];
                int current = 0;

                while((current = bis.read()) != -1){
                    buffer.write(data,0,current);
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(buffer.toByteArray());
                fos.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

}
