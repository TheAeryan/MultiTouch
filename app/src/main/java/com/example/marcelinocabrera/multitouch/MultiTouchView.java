package com.example.marcelinocabrera.multitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcelino Cabrera on 21/03/2017.
 */

public class MultiTouchView extends View {

    private static final int STROKE_WIDTH = 10;
    private static final int CIRCLE_RADIUS = 30;

    private PointF point = null;
    private Paint drawingPaint = null;

    private float minDistTouch = 100; // Distancia mínima en píxeles a la que tiene que estar el puntero para que se pueda mover el punto
                                      // Solo se aplica a la hora de tocar la pantalla para empezar a mover el punto

    private boolean dragStarted = false;


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

    // Dibuja el punto
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(point != null){
            canvas.drawCircle(point.x,point.y,CIRCLE_RADIUS, drawingPaint);
            invalidate();
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
                PointF pointer = new PointF(event.getX(), event.getY());

                // Compruebo que el punto esté dentro de la vista
                if(pointer.x < getWidth() && pointer.y < getHeight()){
                    float point_dist = getDistance(point, pointer);

                    if (point_dist < minDistTouch) { // Compruebo si he pulsado cerca del punto
                        point.x = pointer.x;
                        point.y = pointer.y;

                        dragStarted = true;
                    }

                    invalidate(); // Redibuja
                }

                Log.i("INFO", "Presión:" + event.getPressure());
                Log.i("INFO", "Tamaño:" + event.getSize());

                break;
            }
            // Movemos
            case MotionEvent.ACTION_MOVE:   {
                PointF pointer = new PointF(event.getX(), event.getY());

                if (dragStarted) {
                    // Compruebo que el punto esté dentro de la vista
                    if (pointer.x < getWidth() && pointer.y < getHeight()) {
                        point.x = pointer.x;
                        point.y = pointer.y;
                    }
                    else{ // Si está fuera, vuelve a su posición inicial
                        initialize();
                    }
                    invalidate(); // Redibuja
                }

                Log.i("INFO", "Presión:" + event.getPressure());
                Log.i("INFO", "Tamaño:" + event.getSize());

                break;
            }
            // Levantamos
            case MotionEvent.ACTION_UP:   {
                dragStarted = false;

                break;
            }

            // Pulsamos con mas de un dedo
            case MotionEvent.ACTION_POINTER_DOWN: {
                break;
            }
            // Levantamos
            case MotionEvent.ACTION_POINTER_UP:   {
                break;
            }

            // La ventana pierde el focus
            case MotionEvent.ACTION_CANCEL:{
                initialize(); // El punto vuelve a su posición original
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

        // Inicialmente el punto está en el medio de la pantalla
        point = new PointF(500, 700);
    }

    private float getDistance(PointF a, PointF b){
        double distance;

        distance = Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));

        return (float)distance;
    }


}
