package lox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        byte[] bytes = Files.readAllBytes(Paths.get(filepath));
        run(new String(bytes, Charset.defaultCharset()));
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
    private static void run(String source) {
        lox.Scanner scanner = new lox.Scanner(source);
        List<Token> tokens =  scanner.scanTokens();
        System.out.println(tokens.size());
        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }

    static void error(int line, String message){
        System.err.println("Error: " + line + ": " + message);
        hadError = true;
    }
}