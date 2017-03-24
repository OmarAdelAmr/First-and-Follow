package left.factoring;

import java.util.ArrayList;

public class outputRule
{

	private String leftSide;
	private ArrayList<String> rightSide;

	public outputRule(String leftSide)
	{
		this.leftSide = leftSide;
		this.rightSide = new ArrayList<String>();
	}

	public void addToRightSide(String input_string)
	{
		this.rightSide.add(input_string);
	}

	public String getLeftSide()
	{
		return leftSide;
	}

	public void setLeftSide(String leftSide)
	{
		this.leftSide = leftSide;
	}

	public ArrayList<String> getRightSide()
	{
		return rightSide;
	}

	public void setRightSide(ArrayList<String> rightSide)
	{
		this.rightSide = rightSide;
	}

}
