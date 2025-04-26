package lox;

public abstract class Expr {
	 interface ExprVisitor<R> {
		 R visitBinaryExpr(Binary binary);
		 R visitGroupingExpr(Grouping grouping);
		 R visitLiteralExpr(Literal literal);
		 R visitUnaryExpr(Unary unary);
	}
	 abstract <R> R accept(ExprVisitor<R> visitor);

	public class Binary extends Expr {
		private final Expr left;
		private final Token operator;
		private final Expr right;
		Binary ( Expr left, Token operator, Expr right){
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitBinaryExpr(this);
		}
	}
	public class Grouping extends Expr {
		private final Expr expression;
		Grouping ( Expr expression){
			this.expression = expression;
		}

		@Override
		<R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitGroupingExpr(this);
		}
	}
	public class Literal extends Expr {
		private final Object value;
		Literal ( Object value){
			this.value = value;
		}

		@Override
		<R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitLiteralExpr(this);
		}
	}
	public class Unary extends Expr {
		private final Token operator;
		private final Expr right;
		Unary ( Token operator, Expr right){
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(ExprVisitor<R> visitor) {
			return visitor.visitUnaryExpr(this);
		}
	}
}
