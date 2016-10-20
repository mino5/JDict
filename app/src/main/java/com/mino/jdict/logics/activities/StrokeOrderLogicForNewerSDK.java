package com.mino.jdict.logics.activities;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.larvalabs.svgandroid.SVGBuilder;
import com.mino.jdict.R;
import com.mino.jdict.logics.basic.Logic;

import org.acra.ACRA;

import java.util.ArrayList;

import utils.other.SVGUtils;
import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 7/26/2015.
 */
public class StrokeOrderLogicForNewerSDK extends Logic {

    String strokeSvg = "";
    String[][] svgParts = null;
    ArrayList<String> paths = null;
    ArrayList<Integer> lengths = null;
    com.larvalabs.svgandroid.SVGBuilder svgBuilder;
    AnimationDrawable[] animationDrawables;
    PictureDrawable[] backgrounds;

    ImageView imageView;
    ImageView imageBackground;
    SeekBar seekBar;

    int partsCount = 0;
    int currentPart = 0;
    String[] svgLines;
    Boolean forceStop = false;
    ArrayList<GetBackground> backgroundTasks = new ArrayList<GetBackground>();
    ArrayList<GetSvgPart> svgTasks = new ArrayList<GetSvgPart>();


    public void initialize() {

        forceStop = false;
        if (mBundle == null) {

            Bundle extras = mActivity.getIntent().getExtras();
            strokeSvg = extras.getString("SVG");
            StaticObjectContainer.StaticObject = strokeSvg;
        }

        setImageSize();
        showSvg();

    }

    public void resume() {

        //ClassObject mObj = StaticObjectContainer.GetClassObject(this.hashCode());

        forceStop = false;

        if (seekBar != null) {
            seekBar.setProgress(0);
        }

        super.resume();
    }

    public void onPause() {

        //StaticObjectContainer.SetClassObject(this.hashCode(), new ClassObject(strokeSvg));
        killAllTasks();
    }

    public void onBackPressed() {

        killAllTasks();
    }

    private void killAllTasks() {
        forceStop = true;

        for (GetBackground bg : backgroundTasks) {
            if (bg != null && bg.getStatus() == AsyncTask.Status.RUNNING) {
                try {
                    bg.cancel(true);
                } catch (Exception ex) {
                    ACRA.getErrorReporter().handleSilentException(ex);
                }
            }
        }

        for (GetSvgPart svg : svgTasks) {
            if (svg != null && svg.getStatus() == AsyncTask.Status.RUNNING) {
                try {
                    svg.cancel(true);
                } catch (Exception ex) {
                    ACRA.getErrorReporter().handleSilentException(ex);
                }
            }
        }

        if (animationDrawables != null) {
            for (int i = 0; i < animationDrawables.length; i++) {
                if (animationDrawables[i] != null) {
                    animationDrawables[i].stop();
                }
            }
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        strokeSvg = savedInstanceState.getString("strokeSvg");
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("strokeSvg", strokeSvg);
    }

    private void setImageSize() {
        final FrameLayout mFrame = (FrameLayout) mActivity.findViewById(R.id.frame_id);

        mFrame.post(new Runnable() {

            @Override
            public void run() {
                RelativeLayout.LayoutParams mParams;
                mParams = (RelativeLayout.LayoutParams) mFrame.getLayoutParams();
                mParams.height = mFrame.getWidth();
                mFrame.setLayoutParams(mParams);
                mFrame.postInvalidate();
            }
        });
    }

    private int getLinesCount() {

        if (strokeSvg == null) return 0;

        return (strokeSvg.split("path", -1).length - 1);// * framesPerLine;
    }


    private String generateSvgs(int i, int j, Boolean getAll) {
        String svgPart = "";

        int start = getAll ? 0 : i;

        for (int k = start; k <= i; k++) {

            if (paths.size() > k) {
                svgPart += paths.get(k);

                if (k == i) {
                    svgPart = svgPart.substring(0, svgPart.length() - 7);

                    int dashArraySize = lengths.get(k);
                    int frames = getFrameCount(k);

                    double advance = (double) dashArraySize / frames;
                    int offset = (int) (dashArraySize - (j * advance));

                    if (getAll) offset = 0;

                    svgPart += String.valueOf(offset) + "; \"";
                    svgPart += "/>";
                    svgPart += "\n";
                }
            }
        }

        return svgPart;
    }

    private int getFrameCount(int i) {

        return (lengths.get(i) / 4) + 15;
    }

    private void showSvg() {

        if (svgParts == null) {

            if (strokeSvg == null || strokeSvg.isEmpty()) {
                strokeSvg = (String) StaticObjectContainer.StaticObject;
            }

            if (strokeSvg == null || strokeSvg.isEmpty()) {
                mActivity.onBackPressed();
            }

            currentPart = 0;

            if (strokeSvg != null) {
                svgLines = strokeSvg.split("\n");
            } else {
                mActivity.onBackPressed();
                return;
            }

            partsCount = getLinesCount();

            imageView = (ImageView) mActivity.findViewById(R.id.img1);
            imageBackground = (ImageView) mActivity.findViewById(R.id.background);
            seekBar = (SeekBar) mActivity.findViewById(R.id.seekBar);
            seekBar.setMax(partsCount);

            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageBackground.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            animationDrawables = new AnimationDrawable[partsCount + 1];
            backgrounds = new PictureDrawable[partsCount + 1];

            svgBuilder = new SVGBuilder();

            Init();
        }
    }

    String createSvg(String inSvgPart) {
        String res = "<svg xmlns=\"http://www.w3.org/2000/svg\"  viewBox=\"0 0 109 109\">";
        res += '\n';
        res += inSvgPart;
        res += "</svg>";

        return res;
    }

    private void checkIfAnimationDone(AnimationDrawable anim) {
        final AnimationDrawable a = anim;
        final int timeBetweenChecks = anim.getDuration(0) + 15;

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {

                if (forceStop) return;

                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1) && a.isRunning()) {
                    checkIfAnimationDone(a);
                } else {

                    if (currentPart < partsCount - 1) {

                        currentPart++;

                        seekBar.setProgress(currentPart);

                        if (animationDrawables[currentPart] != null) {

                            //imageView.setImageDrawable(animationDrawables[currentPart]);

                            imageBackground.setImageDrawable(backgrounds[currentPart - 1]);


                            //animationDrawables[currentPart].start();

                            imageView.post(new Runnable() {
                                @Override
                                public void run() {

                                    imageView.setImageDrawable(animationDrawables[currentPart]);
                                    animationDrawables[currentPart].start();

                                    checkIfAnimationDone(animationDrawables[currentPart]);
                                }
                            });


                        }
                    } else {

                        seekBar.setProgress(currentPart + 1);
                        imageBackground.setImageDrawable(backgrounds[partsCount - 1]);
                        imageView.setImageResource(android.R.color.transparent);
                    }
                }
            }
        }, timeBetweenChecks);
    }

    private class GetBackground extends AsyncTask<Integer, Void, PictureDrawable> {

        private int i;

        @Override
        protected PictureDrawable doInBackground(Integer... params) {

            i = params[0];

            SVG svg;
            PictureDrawable draw = null;

            try {

                String part = generateSvgs(i, 0, true);
                svg = SVG.getFromString(createSvg(part));


                draw = new PictureDrawable(svg.renderToPicture());

            } catch (SVGParseException ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }

            return draw;
        }

        @Override
        protected void onPostExecute(PictureDrawable result) {

            backgrounds[i] = result;
        }
    }

    public void Init() {
        Pair<ArrayList<String>, ArrayList<Integer>> pair = SVGUtils.preparePathList(svgLines);
        backgroundTasks.clear();
        svgTasks.clear();
        paths = pair.first;
        lengths = pair.second;

        for (int i = 0; i < partsCount; i++) {
            GetBackground bgTask = new GetBackground();
            backgroundTasks.add(bgTask);
            bgTask.execute(i);
        }

        for (int i = 0; i < partsCount; i++) {

            int frames = getFrameCount(i);

            for (int j = 0; j < frames; j++) {
                GetSvgPart part = new GetSvgPart();
                svgTasks.add(part);
                part.execute(i, j);
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {

                    if (animationDrawables[progress] == null) {
                        progress = currentPart;
                    } else if (currentPart >= partsCount - 1) {
                        currentPart = progress;

                        imageView.setImageResource(android.R.color.transparent);

                        if (currentPart > 0) {
                            imageBackground.setImageResource(android.R.color.transparent);
                            imageBackground.setImageDrawable(backgrounds[currentPart - 1]);
                            animationDrawables[currentPart - 1].stop();
                        } else imageBackground.setImageResource(android.R.color.transparent);

                        animationDrawables[currentPart].start();
                        imageView.setImageDrawable(animationDrawables[currentPart]);

                        checkIfAnimationDone(animationDrawables[currentPart]);
                    }

                    if (progress < partsCount - 1) {
                        currentPart = progress;
                    } else {
                        currentPart = Math.max(partsCount - 2, 0);
                    }
                }
            }
        });
    }

    private class GetSvgPart extends AsyncTask<Integer, Void, com.caverock.androidsvg.SVG> {

        private int i, j;

        @Override
        protected com.caverock.androidsvg.SVG doInBackground(Integer... params) {

            i = params[0];
            j = params[1];

            int frames = getFrameCount(i);

            if (svgParts == null) {
                svgParts = new String[partsCount][];
            }

            if (svgParts[i] == null) {
                svgParts[i] = new String[frames];
            }

            if (svgParts[i].length <= j || svgParts[i][j] == null) {
                svgParts[i][j] = generateSvgs(i, j, false);
            }

            SVG svg = null;
            try {

                String parts = svgParts[i][j];

                svg = SVG.getFromString(createSvg(parts));


            } catch (SVGParseException ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }
            return svg;
        }

        @Override
        protected void onPostExecute(com.caverock.androidsvg.SVG result) {

            int frames = getFrameCount(i);

            PictureDrawable draw = new PictureDrawable(result.renderToPicture());

            if (animationDrawables[i] == null) {
                animationDrawables[i] = new AnimationDrawable();
                animationDrawables[i].setOneShot(true);
            }

            int frameTime = (int) ((7 * lengths.get(i) + 200.0) / frames);

            animationDrawables[i].addFrame(draw, frameTime);

            if (i == 0 && j == frames - 1) {

                // animationDrawables[i].start();


                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImageDrawable(animationDrawables[i]);
                        animationDrawables[i].start();

                        checkIfAnimationDone(animationDrawables[i]);
                    }
                }, 500);

            }

        }
    }

}
