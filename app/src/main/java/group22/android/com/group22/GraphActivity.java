package group22.android.com.group22;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import group22.android.com.group22.helper.SensorHandler;

public class GraphActivity extends AppCompatActivity {
    GraphView myView;
    static boolean conti;
    String xlabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
    String ylabels[] = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};


    private final static int INTERVAL = 100; //millisecs
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = new Intent(this, SensorHandler.class);
        //Start Service
        startService(intent);


        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        mHandler =new Handler();

        myView = new GraphView(GraphActivity.this, new float[0], "Group 22", xlabels, ylabels, true);
        RelativeLayout graphlayout = (RelativeLayout) findViewById(R.id.graphPlot);
        graphlayout.removeAllViews();
        RelativeLayout.LayoutParams graphParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myView.setLayoutParams(graphParam);
        ((RelativeLayout) findViewById(R.id.graphPlot)).addView(myView);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startRepeatingTask();          }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                conti = false;
                stopRepeatingTask();
                clearGraph();
            }
        });
    }



    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            setGraph();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }

    void setGraph(){
        //final float[] floats = {200, 300, 400, 200, 500, 30, 100, 200, 200, 900};

        float[] floats = new float[10];
        for (int i = 0; i < 10; i++) {
            float n = (float) (Math.random() * 9 + 1);
            floats[i] = n;
        }

        myView = new GraphView(GraphActivity.this, floats, "Group 22", xlabels, ylabels, true);
        RelativeLayout graphlayout = (RelativeLayout) findViewById(R.id.graphPlot);
        graphlayout.removeAllViews();
        RelativeLayout.LayoutParams graphParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myView.setLayoutParams(graphParam);
        ((RelativeLayout) findViewById(R.id.graphPlot)).addView(myView);
    }


    void clearGraph(){
        myView = new GraphView(GraphActivity.this, new float[0], "Group 22", xlabels, ylabels, true);
        RelativeLayout graphlayout = (RelativeLayout) findViewById(R.id.graphPlot);
        graphlayout.removeAllViews();
        RelativeLayout.LayoutParams graphParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myView.setLayoutParams(graphParam);
        ((RelativeLayout) findViewById(R.id.graphPlot)).addView(myView);
    }



}
