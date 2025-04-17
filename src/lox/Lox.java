package lox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Lox {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException{
        int len = args.length;
        if(len > 1){
            System.out.println("Usage: jlox [file]");
        } else if (len == 1) {
            // runs the file path from the argument
            runFile(args[0]);
        }
        else {
            // interactive session with run each line
            runPrompt();
        }
    }
    private static void runFile(String filepath)  throws IOException {
        var path = Path.of(filepath);

            String fileContents = Files.readString(path);
            for (String line : fileContents.split("\n")) {
                run(line);
                if (hadError) System.exit(65);
            }
    }
    private static void runPrompt() throws IOException {
        System.out.println("Welcome to Interactive Lox!!!");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        for(;;) {
            String line = scanner.nextLine();
            run(line);
            hadError = false;
        }
    }
    private static void run(String source){
        lox.Scanner scanner = new lox.Scanner();
        List<Token> tokens =  scanner.scanTokens(source);

        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }

    private static void error(int line, String message){
        System.err.println("Error: " + line + ": " + message);
        hadError = true;
    }
}