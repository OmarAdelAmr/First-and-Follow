package left.factoring;

import java.util.ArrayList;

public class inputRule
{
	private String leftSide;
	private ArrayList<rightSideRule> rightSide;

	public inputRule(String leftSide)
	{
		this.leftSide = leftSide;
		rightSide = new ArrayList<rightSideRule>();
	}

	public void addToRightSide(rightSideRule input_rule)
	{
		this.rightSide.add(input_rule);
	}

	public String getLeftSide()
	{
		return leftSide;
	}

	public void setLeftSide(String leftSide)
	{
		this.leftSide = leftSide;
	}

	public ArrayList<rightSideRule> getRightSide()
	{
		return rightSide;
	}

	public void setRightSide(ArrayList<rightSideRule> rightSide)
	{
		this.rightSide = rightSide;
	}

}
