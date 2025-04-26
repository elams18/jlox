package lox;

public class AstPrinter implements Expr.ExprVisitor<String> {

    @Override
    public String visitBinaryExpr(Expr.Binary binary) {
        return "";
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping grouping) {
        return "";
    }

    @Override
    public String visitLiteralExpr(Expr.Literal literal) {
        return "";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary unary) {
        return "";
    }
}
