package edu.aku.hassannaqvi.drig_survey.other;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.aku.hassannaqvi.drig_survey.core.DatabaseHelper;

public final class CheckingID {

    public static final boolean checkFile(Context mContext, String fName) {

        String DirectoryName = Environment.getExternalStorageDirectory() + File.separator + DatabaseHelper.PROJECT_NAME;

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

    public static final String accessingFile(String tagID, String fName, boolean increment) {
        try {
            String fileName = Environment.getExternalStorageDirectory()
                    + File.separator
                    + DatabaseHelper.PROJECT_NAME
                    + File.separator
                    + fName;
            String ID = "";
            tagID = tagID != null ? tagID : "";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            File idFile = new File(fileName);
            while ((ID = reader.readLine()) == null) {
                writeInFile(idFile, tagID + "-1");

                return tagID + "-1";
            }

            if (increment)
                return incrementInFile(idFile, ID);

            return ID;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private static final void writeInFile(File file, String id) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(id);

        writer.flush();
        writer.close();
    }

    private static final String incrementInFile(File idFile, String fileID) throws IOException {
        String[] idLength = fileID.split("-");
        int lastCont = Integer.valueOf(idLength[idLength.length - 1]) + 1;
        String subStr = fileID.substring(0, fileID.length() - idLength[idLength.length - 1].length());

        writeInFile(idFile, subStr + lastCont);

        return subStr + lastCont;
    }


}
