package highlighting.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/// MiniJava Pretty Printer (minimal, stateful)
public final class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void> {

    private final StringBuilder out = new StringBuilder();
    private final int indentWidth;
    private int currentIndent = 0;
    private boolean atLineStart = true;

    private Token lastToken = null;

    public PrettyPrinterVisitor(int indentWidth) {
        this.indentWidth = Math.max(0, indentWidth);
    }

    public String result() {
        return out.toString();
    }

    @Override
    public Void visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {

        if (ctx.packageDecl() != null) {
            visit(ctx.packageDecl());
            nl();
            nl();
        }

        for (MiniJavaParser.ImportDeclContext importDecl : ctx.importDecl()) {
            visit(importDecl);
            nl();
        }

        if (!ctx.importDecl().isEmpty()) {
            nl();
        }

        for (MiniJavaParser.TypeDeclContext typeDecl : ctx.typeDecl()) {
            visit(typeDecl);
            nl();
        }

        return null;
    }

    @Override
    public Void visitClassBody(MiniJavaParser.ClassBodyContext ctx) {

        writeln("{");

        currentIndent++;

        for (MiniJavaParser.ClassBodyDeclarationContext declaration : ctx.classBodyDeclaration()) {
            visit(declaration);
            nl();
        }

        currentIndent--;

        write("}");

        return null;
    }

    @Override
    public Void visitBlock(MiniJavaParser.BlockContext ctx) {

        writeln("{");

        currentIndent++;

        for (MiniJavaParser.BlockStatementContext statement : ctx.blockStatement()) {
            visit(statement);
            nl();
        }

        currentIndent--;

        write("}");

        return null;
    }

    @Override
    public Void visitStatement(MiniJavaParser.StatementContext ctx) {

        if (ctx.block() != null) {
            visit(ctx.block());
            return null;
        }

        visitChildren(ctx);

        if (ctx.getText().endsWith(";")) {
            nl();
        }

        return null;
    }

    private void indent() {
        if (atLineStart) {
            out.repeat(" ", Math.max(0, indentWidth * currentIndent));
            atLineStart = false;
        }
    }

    private void write(String s) {
        if (s == null || s.isEmpty()) return;
        indent();
        out.append(s);
    }

    private void nl() {
        out.append('\n');
        atLineStart = true;
        lastToken = null;
    }

    private void writeln(String s) {
        write(s);
        nl();
    }

    @Override
    public Void visitTerminal(TerminalNode node) {
        Token t = node.getSymbol();
        String text = t.getText();

        if (lastToken != null) {
            int prevType = lastToken.getType();
            int curType = t.getType();

            if (needsSpaceBetween(prevType, curType)) write(" ");
        }

        write(text);
        lastToken = t;
        return null;
    }

    private boolean needsSpaceBetween(int prevType, int curType) {
        return isWordLike(prevType) && isWordLike(curType);
    }

    private boolean isWordLike(int type) {
        return type == MiniJavaLexer.IDENTIFIER
            || type == MiniJavaLexer.STRING_LITERAL
            || type == MiniJavaLexer.CHAR_LITERAL
            || type == MiniJavaLexer.NULL
            || type == MiniJavaLexer.PACKAGE
            || type == MiniJavaLexer.IMPORT
            || type == MiniJavaLexer.CLASS
            || type == MiniJavaLexer.PUBLIC
            || type == MiniJavaLexer.PRIVATE
            || type == MiniJavaLexer.FINAL
            || type == MiniJavaLexer.RETURN
            || type == MiniJavaLexer.NEW
            || type == MiniJavaLexer.IF
            || type == MiniJavaLexer.ELSE
            || type == MiniJavaLexer.WHILE
            || type == MiniJavaLexer.EXTENDS
            || type == MiniJavaLexer.IMPLEMENTS;
    }
}
