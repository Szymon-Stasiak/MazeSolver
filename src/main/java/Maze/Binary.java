package Maze;


import java.io.*;

public class Binary {


    public static int readUnsignedShortLittleEndian(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();
        return (b2 << 8) | b1;
    }

    public static void writeUnsignedShortLittleEndian(RandomAccessFile dos, int value) throws IOException {
        dos.writeByte(value & 0xFF);
        dos.writeByte((value >> 8) & 0xFF);
    }

    public static long readUnsignedIntLittleEndian(DataInputStream dis) throws IOException {
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();
        int b3 = dis.readUnsignedByte();
        int b4 = dis.readUnsignedByte();
        return ((long) b4 << 24) | ((long) b3 << 16) | (b2 << 8) | b1;
    }

    public static void writeUnsignedIntLittleEndian(RandomAccessFile dos, long value) throws IOException {
        dos.writeByte((int) (value & 0xFF));
        dos.writeByte((int) ((value >> 8) & 0xFF));
        dos.writeByte((int) ((value >> 16) & 0xFF));
        dos.writeByte((int) ((value >> 24) & 0xFF));
    }

    public static void writeUnsignedIntLittleEndianWithSeek(RandomAccessFile dos, long value, int seek) throws IOException {
        dos.seek(seek);
        dos.writeByte((int) (value & 0xFF));
        dos.writeByte((int) ((value >> 8) & 0xFF));
        dos.writeByte((int) ((value >> 16) & 0xFF));
        dos.writeByte((int) ((value >> 24) & 0xFF));
        dos.seek(dos.length());

    }


    public static void convertBinaryToText(String binaryFilePath) throws IOException {
        String textFilePath = System.getProperty("user.dir") + "/src/main/resources/decodedMaze.txt";

        try (DataInputStream binaryFile = new DataInputStream(new FileInputStream(binaryFilePath)); BufferedWriter textFile = new BufferedWriter(new FileWriter(textFilePath))) {

            long fileId = readUnsignedIntLittleEndian(binaryFile);
            int escape = binaryFile.readUnsignedByte();
            int columns = readUnsignedShortLittleEndian(binaryFile);
            int lines = readUnsignedShortLittleEndian(binaryFile);
            int entryX = readUnsignedShortLittleEndian(binaryFile);
            int entryY = readUnsignedShortLittleEndian(binaryFile);
            int exitX = readUnsignedShortLittleEndian(binaryFile);
            int exitY = readUnsignedShortLittleEndian(binaryFile);
            binaryFile.skipBytes(12);
            long counter = readUnsignedIntLittleEndian(binaryFile);
            long solutionOffset = readUnsignedIntLittleEndian(binaryFile);
            int separator = binaryFile.readUnsignedByte();
            int wall = binaryFile.readUnsignedByte();
            int path = binaryFile.readUnsignedByte();

            System.out.println("File ID: " + fileId + " Escape: " + escape + " Columns: " + columns + " Lines: " + lines + " EntryX: " + entryX + " EntryY: " + entryY + " ExitX: " + exitX + " ExitY: " + exitY + " Counter: " + counter + " Solution Offset: " + solutionOffset + " Separator: " + separator + " Wall: " + wall + " Path: " + path);

            if (solutionOffset > 0) {
                binaryFile.skipBytes((int) (solutionOffset - 40));
            }
            generateMazeFromEncoding(binaryFile, textFile, counter, separator, wall, path, columns, lines, entryX, entryY, exitX, exitY);
        }
    }



    private static void generateMazeFromEncoding(DataInputStream binaryFile, BufferedWriter textFile, long wordCount, int separator, int wall, int path, int cols, int rows, int entryX, int entryY, int exitX, int exitY) throws IOException {
        int cellsProcessed = 0;
        int currentRow = 1, currentCol = 1;
        while (wordCount-- > 0 && cellsProcessed < cols * rows) {
            int byteRead = binaryFile.readUnsignedByte();
            if (byteRead != separator) {
                throw new IOException("Invalid separator");
            }

            int value = binaryFile.readUnsignedByte();
            int countByte = binaryFile.readUnsignedByte();

            for (int i = 0; i < countByte + 1 && cellsProcessed < cols * rows; i++) {
                if (currentRow == entryY && currentCol == entryX) {
                    textFile.write('P');
                } else if (currentRow == exitY && currentCol == exitX) {
                    textFile.write('K');
                } else {
                    textFile.write(value == wall ? 'X' : ' ');
                }

                cellsProcessed++;
                currentCol++;
                if (currentCol > cols) {
                    textFile.newLine();
                    currentCol = 1;
                    currentRow++;
                }
            }
        }
    }




    public static void convertTextToBinary(String textFilePath, String binaryFilePath) throws IOException {
        try (RandomAccessFile binaryFile = new RandomAccessFile(binaryFilePath, "rw"); BufferedReader textFile = new BufferedReader(new FileReader(textFilePath))) {

            String line;
            int cols = 0, rows = 0;
            int entryX = 0, entryY = 0, exitX = 0, exitY = 0;
            int separator = 0x23, wall = 'X', path = ' ';
            boolean entryFound = false, exitFound = false;

            while ((line = textFile.readLine()) != null) {
                if (cols == 0) {
                    cols = line.length();
                } else if (cols != line.length()) {

                    throw new IOException("Invalid text file format");
                }

                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == 'P') {
                        if (entryFound) {
                            throw new IOException("Found more than one entry point");
                        }
                        entryX = i + 1;
                        entryY = rows + 1;
                        entryFound = true;
                    } else if (line.charAt(i) == 'K') {
                        if (exitFound) {
                            throw new IOException("Found more than one exit point");
                        }
                        exitX = i + 1;
                        exitY = rows + 1;
                        exitFound = true;
                    }
                }

                rows++;
            }

            if (!entryFound) {
                throw new IOException("Entry point not found");
            }
            if (!exitFound) {
                throw new IOException("Exit point not found");
            }


            writeUnsignedIntLittleEndian(binaryFile, 0x52524243);
            binaryFile.writeByte(0x1B);
            writeUnsignedShortLittleEndian(binaryFile, cols);
            writeUnsignedShortLittleEndian(binaryFile, rows);
            writeUnsignedShortLittleEndian(binaryFile, entryX);
            writeUnsignedShortLittleEndian(binaryFile, entryY);
            writeUnsignedShortLittleEndian(binaryFile, exitX);
            writeUnsignedShortLittleEndian(binaryFile, exitY);
            binaryFile.write(new byte[12]);
            writeUnsignedIntLittleEndian(binaryFile, 0);
            writeUnsignedIntLittleEndian(binaryFile, 0);
            binaryFile.writeByte(separator);
            binaryFile.writeByte(wall);
            binaryFile.writeByte(path);

            EncodeMazeToBinary(binaryFile, textFilePath, separator, wall, path, cols, rows, entryX, entryY, exitX, exitY);

        }
    }

    public static void modifyWordCount(RandomAccessFile file, int newValue) throws IOException {
        writeUnsignedIntLittleEndianWithSeek(file, newValue, 29);
    }

    private static void EncodeMazeToBinary(RandomAccessFile binaryFile, String textFilePath, int separator, int wall, int path, int cols, int rows, int entryX, int entryY, int exitX, int exitY) throws IOException {
        try (BufferedReader textFile = new BufferedReader(new FileReader(textFilePath))) {
            int currentCol = 1;
            int currentCell = 0;
            int currentCellValue = 0;

            int wordCount = 0;

            String line;
            while ((line = textFile.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char cell = line.charAt(i);
                    if (cell == 'P' || cell == 'K') {
                        cell = ' ';
                    }


                    if (currentCellValue == 0) {
                        currentCellValue = cell;
                        currentCell = 1;
                    } else if (currentCellValue == cell) {
                        currentCell++;
                    } else {
                        writeWord(binaryFile, separator, currentCellValue, currentCell);
                        wordCount++;
                        currentCellValue = cell;
                        currentCell = 1;
                    }

                    currentCol++;
                    if (currentCol > cols) {
                        currentCol = 1;
                    }
                }
            }

            if (currentCell > 0) {
                writeWord(binaryFile, separator, currentCellValue, currentCell);
                wordCount++;
            }


            modifyWordCount(binaryFile, wordCount);
        }
    }

    private static void writeWord(RandomAccessFile binaryFile, int separator, int cellValue, int cellCount) throws IOException {
        binaryFile.writeByte(separator);
        binaryFile.writeByte(cellValue);
        binaryFile.writeByte(cellCount - 1);
    }

}

