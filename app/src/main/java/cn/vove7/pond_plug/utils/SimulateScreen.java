package cn.vove7.pond_plug.utils;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import cn.vove7.pond_plug.FloatWindow;

import java.util.ArrayList;

import static cn.vove7.pond_plug.utils.Snode.N;

/**
 * Created by Vove on 2017/6/15.
 * 模拟操作
 */

public class SimulateScreen {
    private FloatWindow floatWindow;
    private int maBeginX;
    private int maBeginY;
    private int bumpHeight;
    private int bumpWidth;

    public SimulateScreen(FloatWindow floatWindow) {
        this.floatWindow = floatWindow;

        DisplayMetrics metrics = floatWindow.getContext().getResources().getDisplayMetrics();

        int height = metrics.heightPixels;//屏幕高
        int width = metrics.widthPixels;//屏幕宽
        maBeginX = (int) (height * 0.2242);
        maBeginY = (int) (width * 0.0486);
        int matrixHeight = (int) (height * 0.5109);
        int matrixWidth = (int) (width * 0.9048);
        bumpHeight =(matrixHeight / (2 * N));
        bumpWidth =(matrixWidth / (2 * N));

    }

    public void simulateOperate(ResponseMessage responseMessage) {
        ArrayList<Step> steps=responseMessage.getSteps();
        StringBuilder builder=new StringBuilder();
        for (Step step : steps) {
            int bumpCoorY = step.getBumpCoor()[0];
            int bumpCoorX = step.getBumpCoor()[1];//移动块坐标
            int stepNum = step.getStepNum();//步数
            int stepRate=(2*(stepNum-1)+1);

            int beginX = maBeginY + (2 * bumpCoorX + 1) * bumpWidth;
            int beginY = maBeginX + (2 * bumpCoorY + 1) * bumpHeight;
            int endX ;
            int endY ;

            switch (step.getDirection()){
                case 'U':
                    endX = beginX;
                    endY = (beginY - bumpHeight * stepRate);
                    break;
                case 'D':
                    endX = beginX;
                    endY = (beginY + bumpHeight * stepRate);
                    break;
                case 'R':
                    endX = (beginX + bumpWidth * stepRate);
                    endY = beginY;
                    break;
                case 'L':
                    endX = (beginX - bumpWidth * stepRate);
                    endY = beginY;
                    break;
                default:
                    endX=endY=0;
            }
//            int beginX = maBeginX + floatWindow.dp2px(pointX);
//            int beginY = floatWindow.dp2px(pointY);
            int moveTime=stepNum*300;
            String swipeCmd = "input swipe " + beginX + " " + beginY + " " + endX + " " + endY+" "+moveTime+" \n";
            builder.append(swipeCmd);
        }
        int beginX = maBeginY + (2 * responseMessage.getLastFishCoor()[1] + 1) * bumpWidth;
        int beginY = maBeginX + (2 * responseMessage.getLastFishCoor()[0] + 1) * bumpHeight;
        int endX = maBeginX+ 13*bumpWidth;
        String lastCmd="input swipe " + beginX + " " + beginY + " " + endX + " " + beginY+" 300";
        builder.append(lastCmd);
//        Log.d("swipeCmd",builder.toString());
        executeCmd(builder.toString());
    }

    private void executeCmd(String cmd) {
        RootCmd.rootCmd(cmd);
    }
}
