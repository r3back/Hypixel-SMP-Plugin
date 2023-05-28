package me.reb4ck.smp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface LinuxTerminal {
    default void executeCommand(String[] command, String pathTxt){
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(command);

            pb.directory(new File(pathTxt));

            pb.redirectErrorStream();

            process = pb.start();

            InputStream inputStream = process.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    default void executeCommand(String[] command){
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(command);

            process = pb.start();

            InputStream inputStream = process.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    default void executeCommand(String cmd, String pathTxt){
        String[] command = cmd.split(" ");

        executeCommand(command, pathTxt);
    }


    default void executeCommand(String cmd){
        String[] command = cmd.split(" ");

        executeCommand(command);
    }
}
