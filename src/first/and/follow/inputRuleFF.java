package first.and.follow;

import java.util.ArrayList;

public class inputRuleFF
{

	private String leftSide;
	private ArrayList<String> rightSide;

	public inputRuleFF(String leftSide)
	{
		this.leftSide = leftSide;
		this.rightSide = new ArrayList<>();
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

	public void addRightTerm(String newTerm)
	{
		this.rightSide.add(newTerm);
	}

}
