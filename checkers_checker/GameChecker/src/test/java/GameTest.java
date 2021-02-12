import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class GameTest {
    private Game testGame;
    private final static int goodTestNumber = 4;
    private final static int invalidTestNumber = 4;
    private final static int busyTestNumber = 3;
    private final static int whiteTestNumber = 3;
    private Scanner fileinscan = null;
    private PrintStream outputPrintStream = null;

    /**
     * Method to delete all files that may left from previous iterations before the new test call.
     */
    @Before
    public void testPrepare(){
        for(int i = 0; i < goodTestNumber; ++i) {
            File outputFile = new File("src/outputFile" + (i + 1) + ".txt");
            outputFile.delete();
        }
    }

    /**
     * Science task uses console input and output, test uses files and gives them instead of stdin and stdout.
     * This streams should be on their initial places between the tests.
     */
    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    /**
     * Test cases, where exception should not happen and checking the result with a file. It may throw exception anyway.
     * @throws IOException
     * @throws WhiteCellException
     * @throws BusyCellException
     * @throws InvalidMoveException
     */
    @Test
    public void simpleMovesTest() throws IOException, WhiteCellException, BusyCellException,
            InvalidMoveException {
        for(int i = 0; i < goodTestNumber; ++i) {
            try {
                File inputFile = new File("src/inputFile" + (i + 1) + ".txt");
                fileinscan = new Scanner(inputFile);
                File outputFile = new File("src/outputFile" + (i + 1) + ".txt");
                outputPrintStream = new PrintStream("src/outputFile" + (i + 1) + ".txt");
                System.setOut(outputPrintStream);
                File ansFile = new File("src/ansFile" + (i + 1) + ".txt");
                testGame = new Game();
                oneFileTest(fileinscan, outputFile, ansFile);
            } finally {
                fileinscan.close();
                outputPrintStream.close();
            }

        }
    }

    /**
     * Test cases, where InvalidMoveException should be thrown. All other exceptions will be interpreted as mistakes.
     * @throws IOException
     * @throws WhiteCellException
     * @throws BusyCellException
     * @throws InvalidMoveException
     */
    @Test
    public void invalidMoveTest() throws IOException, WhiteCellException, BusyCellException,
            InvalidMoveException {
        for(int i = 0; i < invalidTestNumber; ++i) {
            try {
                final File inputFile = new File("src/inputInvalidFile" + (i + 1) + ".txt");
                fileinscan = new Scanner(inputFile);
                testGame = new Game();
                Assert.assertThrows(InvalidMoveException.class, () -> {
                    oneFileTest(fileinscan, inputFile, inputFile);
                });
            } finally {
                fileinscan.close();
            }

        }
    }

    @Test
    public void busyCellTest() throws IOException, WhiteCellException, BusyCellException,
            InvalidMoveException {
        for(int i = 0; i < busyTestNumber; ++i) {
            try {
                final File inputFile = new File("src/inputBusyFile" + (i + 1) + ".txt");
                fileinscan = new Scanner(inputFile);
                testGame = new Game();
                Assert.assertThrows(BusyCellException.class, () -> {
                    oneFileTest(fileinscan, inputFile, inputFile);
                });
            } finally {
                fileinscan.close();
            }

        }
    }

    @Test
    public void whiteCellTest() throws IOException, WhiteCellException, BusyCellException,
            InvalidMoveException {
        for(int i = 0; i < whiteTestNumber; ++i) {
            try {
                final File inputFile = new File("src/inputWhiteFile" + (i + 1) + ".txt");
                fileinscan = new Scanner(inputFile);
                testGame = new Game();
                Assert.assertThrows(WhiteCellException.class, () -> {
                    oneFileTest(fileinscan, inputFile, inputFile);
                });
            } finally {
                fileinscan.close();
            }

        }
    }

    private void oneFileTest(Scanner in, File out, File ans) throws IOException, WhiteCellException, BusyCellException,
            InvalidMoveException {
        testGame.init(in);
        while (in.hasNext()) {
            testGame.nextMoveForBothColors(in);
        }
        testGame.writeResult();
        Assert.assertTrue(checkEquality(ans, out));
    }

    private boolean checkEquality(File file1, File file2) throws FileNotFoundException {
        Scanner file1scan = new Scanner(file1);
        Scanner file2scan = new Scanner(file2);
        while(file1scan.hasNext() && file2scan.hasNext()) {
            if (!file1scan.next().endsWith(file2scan.next())) {
                return false;
            }
        }
        if(file1scan.hasNext() || file2scan.hasNext()) {
            return false;
        }
        return true;
    }
}
