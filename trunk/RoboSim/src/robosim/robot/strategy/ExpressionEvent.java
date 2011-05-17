package robosim.robot.strategy;

public class ExpressionEvent extends Event {
	
	private Expression expression;

	public ExpressionEvent(String name, Expression expression) {
		super(name);
		this.expression = expression;
	}

	@Override
	public boolean evaluate() {
		return expression.evaluate();
	}

}
