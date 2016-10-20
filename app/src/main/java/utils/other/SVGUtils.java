package utils.other;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Mino on 2015-02-19.
 */
public class SVGUtils {

    private static double bezierPoint(double t, double o1, double c1, double c2, double e1) {
        double C1 = (e1 - (3.0 * c2) + (3.0 * c1) - o1);
        double C2 = ((3.0 * c2) - (6.0 * c1) + (3.0 * o1));
        double C3 = ((3.0 * c1) - (3.0 * o1));

        return ((C1 * t * t * t) + (C2 * t * t) + (C3 * t) + (o1));
    }

    // startX, startY, x1, x2, y1, y2, endX, endY
    public static double getArcLength(double[] path) {

        int STEPS = 100; // > precision
        double t = 1 / (double) STEPS;
        double aX = 0;
        double aY = 0;
        double bX = path[0];
        double bY = path[1];
        double dX = 0, dY = 0;
        double dS = 0;
        double sumArc = 0;
        double j = 0;

        for (int i = 0; i < STEPS; j = j + t) {
            aX = bezierPoint(j, path[0], path[2], path[4], path[6]);
            aY = bezierPoint(j, path[1], path[3], path[5], path[7]);
            dX = aX - bX;
            dY = aY - bY;
            // deltaS. Pitagora
            dS = Math.sqrt((dX * dX) + (dY * dY));
            sumArc = sumArc + dS;
            bX = aX;
            bY = aY;
            i++;
        }

        return sumArc;
    }


    public static Pair<ArrayList<String>, ArrayList<Integer>> preparePathList(String[] svgLines) {

        ArrayList<String> lineList = new ArrayList<String>();
        ArrayList<Integer> lengthList = new ArrayList<Integer>();

        String svgPart = "";
        double[] startCoords = new double[2];
        double[] secondControlPoint = null;

        // w przypadku C dostajemy pozycje absolutne - poprzedni end wyznaczone nowe zero
        // w przypadku c dostajemy pozycje relatywne - możemy liczyć dł. względem zera ?

        for (String svgLine : svgLines) {

            svgPart = "";
            svgPart += svgLine.substring(0, svgLine.length() - 2);

            double offset = 0;

            int len = svgPart.length();
            double minus = 1.0;

            boolean relativeMode;
            char previousLetter = 0;

            for (int i = 0; i < len; i++) {

                if (svgPart.charAt(i) == 'M') {

                    previousLetter = 'M';
                    i++;

                    while (Character.isWhitespace(svgPart.charAt(i))) i++;

                    for (int k = 0; k < 2; k++) {

                        String partNumber = "";
                        while (Character.isDigit(svgPart.charAt(i)) || svgPart.charAt(i) == '.') {
                            partNumber += svgPart.charAt(i++);
                        }

                        if (svgPart.charAt(i) == ',' || svgPart.charAt(i) == '-' || Character.isWhitespace(svgPart.charAt(i))) {

                            if (svgPart.charAt(i) == '-') minus = -1.0;
                            i++;
                        }

                        if (!partNumber.isEmpty()) {
                            startCoords[k] = Double.parseDouble(partNumber) * minus;
                            minus = 1.0;
                        }

                    }
                }

                minus = 1.0;

                while (Character.isWhitespace(svgPart.charAt(i))) i++;

                if (svgPart.charAt(i) == 'c' || svgPart.charAt(i) == 'C' || svgPart.charAt(i) == 's' || svgPart.charAt(i) == 'S' || Character.isDigit(svgPart.charAt(i))) {

                    double[] coords = new double[6];

                    if (svgPart.charAt(i) == 'c' || svgPart.charAt(i) == 'C' ||
                            (Character.isDigit(svgPart.charAt(i)) && (previousLetter == 'c'  || previousLetter == 'C'))) {

                        relativeMode = svgPart.charAt(i) == 'c' || (Character.isDigit(svgPart.charAt(i)) && previousLetter == 'c');

                        if (!Character.isDigit(svgPart.charAt(i))) {
                            previousLetter = svgPart.charAt(i);
                            i++;
                        }

                        while (Character.isWhitespace(svgPart.charAt(i))) i++;

                        for (int k = 0; k < 6; k++) {

                            String partNumber = "";

                            while (!Character.isDigit(svgPart.charAt(i))) {
                                if (svgPart.charAt(i) == '-') minus = -1.0;
                                i++;
                            }

                            while (Character.isDigit(svgPart.charAt(i)) || svgPart.charAt(i) == '.') {

                                partNumber += svgPart.charAt(i++);
                            }


                            if (!partNumber.isEmpty()) {
                                coords[k] = Double.parseDouble(partNumber) * minus;
                                minus = 1.0;
                            }
                        }

                        i--;

                        if (!relativeMode) {

                            //offset += Math.sqrt((startCoords[0] - coords[4]) * (startCoords[0] - coords[4]) + (startCoords[1] - coords[5]) * (startCoords[1] - coords[5]));

                            offset += SVGUtils.getArcLength(new double[]{startCoords[0], startCoords[1], coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]});

                            startCoords[0] = coords[4];
                            startCoords[1] = coords[5];

                        } else {

                            offset += SVGUtils.getArcLength(new double[]{0, 0, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]});

                            startCoords[0] += coords[4];
                            startCoords[1] += coords[5];
                        }

                        if (secondControlPoint == null) {

                            secondControlPoint = new double[2];
                            secondControlPoint[0] = coords[2];
                            secondControlPoint[1] = coords[3];
                        }

                    }

                    if (svgPart.charAt(i) == 's' || svgPart.charAt(i) == 'S'
                            || (Character.isDigit(svgPart.charAt(i)) && (previousLetter == 's'  || previousLetter == 'S'))) {

                        relativeMode = svgPart.charAt(i) == 's' || (Character.isDigit(svgPart.charAt(i)) && previousLetter == 's');

                        if (!Character.isDigit(svgPart.charAt(i))) {
                            previousLetter = svgPart.charAt(i);
                            i++;
                        }

                        while (Character.isWhitespace(svgPart.charAt(i))) i++;

                        for (int k = 0; k < 4; k++) {

                            String partNumber = "";

                            while (!Character.isDigit(svgPart.charAt(i))) {
                                if (svgPart.charAt(i) == '-') minus = -1;
                                i++;
                            }

                            while (Character.isDigit(svgPart.charAt(i)) || svgPart.charAt(i) == '.') {

                                partNumber += svgPart.charAt(i++);
                            }


                            if (!partNumber.isEmpty()) {
                                coords[k] = Double.parseDouble(partNumber) * minus;
                                minus = 1;
                            }
                        }

                        i--;

                        if (!relativeMode) {

                            if (secondControlPoint == null) {
                                offset += SVGUtils.getArcLength(new double[]{startCoords[0], startCoords[1], startCoords[0], startCoords[1], coords[0], coords[1], coords[2], coords[3]});

                            } else {
                                offset += SVGUtils.getArcLength(new double[]{startCoords[0], startCoords[1], secondControlPoint[0], secondControlPoint[1], coords[0], coords[1], coords[2], coords[3]});
                            }

                            startCoords[0] = coords[2];
                            startCoords[1] = coords[3];

                        } else {


                            if (secondControlPoint == null) {
                                offset += SVGUtils.getArcLength(new double[]{0, 0, 0, 0, coords[0], coords[1], coords[2], coords[3]});

                            } else {
                                offset += SVGUtils.getArcLength(new double[]{0, 0, secondControlPoint[0], secondControlPoint[1], coords[0], coords[1], coords[2], coords[3]});
                            }


                            startCoords[0] += coords[2];
                            startCoords[1] += coords[3];
                        }

                        if (secondControlPoint == null) {

                            secondControlPoint = new double[2];
                            secondControlPoint[0] = coords[0];
                            secondControlPoint[1] = coords[1];
                        }

                    }
                }
            }

            //if (offset < framesPerLine) offset = framesPerLine;

            offset += 0.2 * offset;
            //if (offset < 50) offset = 50;

            svgPart += " style=\"fill:none;stroke:#000000;stroke-width:4;stroke-linecap:round;stroke-linejoin:round; stroke-dasharray: " + String.valueOf((int) offset) + "; stroke-dashoffset: 0" + "; \"";

            //svgPart += " style=\"fill:none;stroke:#000000;stroke-width:3;stroke-linecap:round;stroke-linejoin:round; \"";
            svgPart += "/>";
            svgPart += "\n";

            lineList.add(svgPart);
            lengthList.add((int) offset);

            startCoords = new double[2];
            secondControlPoint = null;

        }

        return new Pair<ArrayList<String>, ArrayList<Integer>>(lineList, lengthList);
    }
}
