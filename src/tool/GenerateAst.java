package tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Usage: generate_ast <path to directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }
    private static void defineAst(String outputDir, String abstractClassName, List<String> classes) throws IOException {
        String path = outputDir + File.separator + abstractClassName + ".java";
        PrintWriter writer = new PrintWriter(path);
        writer.println("package lox;");
        writer.println();
        writer.println("public abstract class " + abstractClassName + " {");
        defineVisitor(writer, abstractClassName, classes);
        writer.println("\t abstract <R> R accept("+ abstractClassName+ "Visitor<R> visitor" +");");
        writer.println();

        for(String _class: classes){
            String[] lines = _class.split(":");
            String subClassName = lines[0].strip();

            writer.println("\tpublic class " + subClassName + " extends " + abstractClassName + " {");

            String[] arguments = lines[1].split(",");

            defineTypes(writer, arguments);
            defineConstructor(writer, subClassName, lines[1], arguments);

            // Visitor pattern.
            writer.println();
            writer.println("\t\t@Override");
            writer.println("\t\t<R> R accept("+abstractClassName+"Visitor<R> visitor) {");
            writer.println("\t\t\treturn visitor.visit" +
                    subClassName + abstractClassName + "(this);");
            writer.println("\t\t}");

            writer.println("\t}");
        }
        writer.println("}");
        writer.close();
    }
    private static void defineTypes(PrintWriter writer, String [] arguments) throws IOException {
        for(String arg: arguments){
            arg = arg.strip();
            String[] tokens = arg.split(" ");
            writer.println("\t\tprivate final " + tokens[0] + " " + tokens[1] + ";");
        }
    }
    private static void defineConstructor(PrintWriter writer, String subClassName, String argString, String [] arguments) throws IOException {
        writer.println("\t\t"+ subClassName + " (" + argString + ")" + "{");
        for(String arg: arguments){
            arg = arg.strip();
            String name = arg.split(" ")[1];
            writer.println("\t\t\tthis."+ name + " = " + name + ";");
        }
        writer.println("\t\t}");
    }
    private static void defineVisitor(PrintWriter writer, String abstractClassName, List<String> classes) throws IOException {
        writer.println("\t interface " + abstractClassName + "Visitor<R> {");
        for(String className : classes){
            String[] lines = className.split(":");
            String subClassName = lines[0].strip();
            writer.println("\t\t R visit" + subClassName + abstractClassName + "("+ subClassName + " " +  subClassName.toLowerCase() + ");");
        }
        writer.println("\t}");
    }
}
