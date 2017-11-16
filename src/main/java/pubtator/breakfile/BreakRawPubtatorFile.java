package pubtator.breakfile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;

class BreakRawPubtatorFile {
    public static void main(String[] args) throws IOException {
        String pubtatorBioconceptsGzFile = args[0];
        int nRowsPerJob = Integer.parseInt(args[1]);
        String outputStem = args[2];
        int verbose = Integer.parseInt(args[3]);

        // break file into chunks, splitting on newlines only
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream(pubtatorBioconceptsGzFile))));
        int lineCount = 0;
        int fileCount = 0;
        PrintWriter printWriter = new PrintWriter(outputStem + "-" + fileCount + ".txt");
        String line;
        while ((line = reader.readLine()) != null) {
            printWriter.println(line);
            if (line.length() < 3 && lineCount > nRowsPerJob) {
                printWriter.flush();
                printWriter.close();
                fileCount++;
                printWriter = new PrintWriter(outputStem + "-" + fileCount + ".txt");
                System.out.println(fileCount);
                lineCount = 0;
            }
            lineCount++;
            if (verbose == 1 && lineCount % 10000 == 0) {
                System.out.println(lineCount);
            }
        }
        printWriter.flush();
        printWriter.close();
    }
}
