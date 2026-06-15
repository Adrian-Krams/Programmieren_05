package highlighting;

import highlighting.antlr.*;
import org.antlr.v4.runtime.*;

import java.util.Scanner;

public class Main {

    public static void main(String... args) {

        String code = """
        public class Test{private int x;public void run(){if(x){return;}while(x){x=x;}}}
        """;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Einrueckung eingeben, z.B. 2 oder 4: ");
        int indentWidth = scanner.nextInt();

        MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJavaParser parser = new MiniJavaParser(tokens);

        MiniJavaParser.CompilationUnitContext tree = parser.compilationUnit();

        PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentWidth);
        visitor.visit(tree);

        System.out.println("----- Pretty Printer Ausgabe -----");
        System.out.println(visitor.result());
    }
}
