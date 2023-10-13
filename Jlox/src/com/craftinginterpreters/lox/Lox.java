package com.craftinginterpreters.lox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Lox {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException{
        if (args.length > 1) {
            System.out.println("Usage: Jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // 直接从源文件执行脚本语言
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get((path)));

        // 让已出现问题的代码不再执行
        if (hadError) System.exit(65);

        run(new String(bytes, Charset.defaultCharset()));
    }

    // 从命令行读取并执行
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">>");
            String line = reader.readLine();
            if (line == null) break;
            run(line);

            // 重置错误标识，报错但不影响下次命令的执行
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // error handling
    public static void error(int line, String message) {
        report(line, "", message);
    }
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
