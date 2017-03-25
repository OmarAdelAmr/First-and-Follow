package first.and.follow;

import java.util.ArrayList;

public class ffDataStructure
{

	private String leftside;
	private ArrayList<String> terminals;

	public ffDataStructure(String leftSide)
	{
		this.leftside = leftSide;
		this.terminals = new ArrayList<>();
	}

	public String getLeftside()
	{
		return leftside;
	}

	public void setLeftside(String leftside)
	{
		this.leftside = leftside;
	}

	public ArrayList<String> getTerminals()
	{
		return terminals;
	}

	public void setTerminals(ArrayList<String> terminals)
	{
		this.terminals = terminals;
	}

}
