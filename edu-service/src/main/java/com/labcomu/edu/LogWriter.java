package com.labcomu.edu;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

public class LogWriter {

    public static void writeLogCircuitBreaker(CallNotPermittedException e) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_'at'_HH:mm:ss_z");
        Date date = new Date(System.currentTimeMillis());
        String formatedDate = formatter.format(date);
        try (FileWriter myWriter = new FileWriter(String.format("%s.txt", formatedDate))){
            myWriter.write(e.toString());
            System.out.println("Successfully wrote error log to the file.");
        } catch (IOException f) {
            System.out.println("An error occurred while writin log file.");
            f.printStackTrace();
        }
    }
}
