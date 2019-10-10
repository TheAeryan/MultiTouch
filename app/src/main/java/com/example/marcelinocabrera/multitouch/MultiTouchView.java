package com.example.marcelinocabrera.multitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Marcelino Cabrera on 21/03/2017.
 */

public class MultiTouchView extends View {

    private static final int STROKE_WIDTH = 10;
    private static final int CIRCLE_RADIUS = 30;

    // Lista de puntos pulsados en la pantalla
    private ArrayList<PointF> touchPoints = null;
    private Paint drawingPaint = null;

    public MultiTouchView(Context context) {
        super(context);
        initialize();
    }

    public MultiTouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    // Muestra los puntos en la pantalla (incluidos la linea y el punto medio)
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(touchPoints.size() > 0)
        {
            if(touchPoints.size() == 1) // Muesta un punto en pantalla
            {
                canvas.drawCircle(touchPoints.get(0).x,touchPoints.get(0).y,CIRCLE_RADIUS, drawingPaint);
            } else // Muestra un los puntos unidos con una linea y el punto medio
            {
                PointF midpt = null;
                for(int index=1; index<touchPoints.size(); ++index)
                {
                    midpt = getMidPoint(
                            touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                            touchPoints.get(index).x,touchPoints.get(index).y);
                    canvas.drawCircle(touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,CIRCLE_RADIUS, drawingPaint);
                    canvas.drawCircle(touchPoints.get(index).x,touchPoints.get(index).y,CIRCLE_RADIUS, drawingPaint);
                    canvas.drawLine(
                            touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                            touchPoints.get(index).x,touchPoints.get(index).y,drawingPaint);
                    canvas.drawCircle(midpt.x,midpt.y, CIRCLE_RADIUS/2, drawingPaint);
                }
            }
            // Redibuja el canvas
            invalidate();
        }

    }

    // Fija los puntos a dibujar
    public void setPoints(MotionEvent event){
        touchPoints.clear(); // Elimina la lista anterior
        for(int index=0; index<event.getPointerCount(); ++index)
        {
            // Obtiene la lista de puntos pulsados del MotionEvent
            touchPoints.add(new PointF(event.getX(index),event.getY(index)));
        }
    }

    // METODO IMPORTANTE
    // Control de la multipulsacion de la pantalla
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // Define que accion se esta realizando en la pantalla
        // getAction(): clase de acción que se está ejecutando.
        // ACTION_MASK: máscara de bits de partes del código de acción.
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch(action) {
            // Pulsamos
            case MotionEvent.ACTION_DOWN: {
                setPoints(event);// Fija puntos
                invalidate(); // Redibuja
                Log.i("INFO", "Presión:" + event.getPressure());
                Log.i("INFO", "Tamaño:" + event.getSize());
                break;
            }
            // Movemos
            case MotionEvent.ACTION_MOVE:   {
                setPoints(event);// Fija puntos nuevos
                invalidate(); // Redibuja
                break;
            }
            // Levantamos
            case MotionEvent.ACTION_UP:   {
                initialize (); // Borra la pantalla
                break;
            }

            // Pulsamos con mas de un dedo
            case MotionEvent.ACTION_POINTER_DOWN: {
                setPoints(event); //
                invalidate();
                if (touchPoints.size() == 5) Log.i ("INFO","Cinco dedos");
                if (touchPoints.size() == 10) Log.i ("INFO","Diez dedos");
                break;
            }
            // Levantamos
            case MotionEvent.ACTION_POINTER_UP:   {
                initialize (); // Borra la pantalla
                break;
            }

        }


        return true;
    }
    private void initialize(){
        drawingPaint = new Paint();
        drawingPaint.setColor(Color.MAGENTA);
        drawingPaint.setStrokeWidth(STROKE_WIDTH);
        drawingPaint.setStyle(Paint.Style.FILL);
        drawingPaint.setAntiAlias(true);
        touchPoints = new ArrayList<PointF>();
    }

    private PointF getMidPoint(float x1,float y1, float x2, float y2) {
        PointF point = new PointF();
        float x = x1 + x2;
        float y = y1 + y2;
        point.set(x / 2, y / 2);
        return point;
    }

}
