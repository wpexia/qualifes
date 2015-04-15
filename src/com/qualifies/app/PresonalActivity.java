package com.qualifies.app;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.View;

public class PresonalActivity extends Activity implements View.OnClickListener,View.OnTouchListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {

        return false;
    }
}
