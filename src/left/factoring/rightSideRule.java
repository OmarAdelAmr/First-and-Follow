package left.factoring;

public class rightSideRule
{
	private String rule;
	private boolean isValid;

	public rightSideRule(String rule)
	{
		this.rule = rule;
		this.isValid = true;
	}

	public String getRule()
	{
		return rule;
	}

	public void setRule(String rule)
	{
		this.rule = rule;
	}

	public boolean isValid()
	{
		return isValid;
	}

	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}

}
