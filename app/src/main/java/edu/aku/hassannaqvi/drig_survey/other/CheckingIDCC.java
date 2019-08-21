package edu.aku.hassannaqvi.drig_survey.other;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;
import edu.aku.hassannaqvi.drig_survey.core.MainApp;

public final class CheckingIDCC {

    private static final String TAG = CheckingIDCC.class.getName();
    private static final String DirectoryName = Environment.getExternalStorageDirectory() + File.separator + DatabaseHelper.PROJECT_NAME;

    public static final String accessingFile(Context mContext, String tagID, String fName, String type, String autoCode, boolean increment) {
        try {

            if (!creatingFile(mContext, fName)) return "";

            String fileName = DirectoryName
                    + File.separator
                    + fName;
            StringBuffer lineBuffer = new StringBuffer();
            File idFile = new File(fileName);
            FileInputStream fis = new FileInputStream(idFile);
            byte[] byteArray = new byte[(int) idFile.length()];
            fis.read(byteArray);
            String data = new String(byteArray);
            String[] stringArray = data.split("\n");

            if (stringArray.length == 1) {
                lineBuffer.append("CASE\n");
                lineBuffer.append(tagID + "-000001\n");
                lineBuffer.append(tagID + "-000001\n");
                lineBuffer.append("CONTROL\n");
                lineBuffer.append(tagID + "-000001\n");
                lineBuffer.append(tagID + "-000001");

                writeInFile(idFile, lineBuffer.toString());
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lineBuffer.append(line + "\n");
                    }
                }
            }

            return incrementInFile(idFile, lineBuffer.toString(), getLineInFile(type), autoCode, increment);

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private static final boolean creatingFile(Context mContext, String fName) {

        File folder = new File(DirectoryName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {

            File idFile = new File(folder, fName);
            if (!idFile.exists()) {
                try {
                    writeInFile(idFile, "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;

        } else {
            Toast.makeText(mContext, "Can't create folder!!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private static int getLineInFile(String type) {
        switch (type) {
            case MainApp.CASESCR:
                return 1;
            case MainApp.CASEID:
                return 2;
            case MainApp.CONTROLSCR:
                return 4;
            case MainApp.CONTROLID:
                return 5;
            default:
                return 0;
        }
    }

    private static final void writeInFile(File file, String line) throws IOException {
        FileWriter writer = new FileWriter(file);

        writer.write(line);

        writer.flush();
        writer.close();
    }

    private static final String incrementInFile(File idFile, String lines, int type, String autoCode, boolean flag) {
        String tarLine = "", combStr = "";
        StringBuffer lineBuffer = new StringBuffer();

        try {
            String[] indLines = lines.split("\n");

            for (int i = 0; i < indLines.length; i++) {
                if (i == type) {
                    tarLine = indLines[i];

                    String[] idLength = tarLine.split("-");
                    String id = idLength[idLength.length - 1];
                    String idSubStr = id.substring(2);

                    int lastCont = flag ? Integer.valueOf(idSubStr) + 1 : Integer.valueOf(idSubStr);

                    String subStr = tarLine.substring(0, (tarLine.length() - idLength[idLength.length - 1].length()));
                    combStr = subStr + autoCode + String.format("%04d", Integer.valueOf(lastCont));

                    lineBuffer.append(combStr + "\n");

                } else
                    lineBuffer.append(indLines[i] + "\n");
            }

            writeInFile(idFile, lineBuffer.toString());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return combStr;
    }


}
