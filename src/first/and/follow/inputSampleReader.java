package first.and.follow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class inputSampleReader
{
	private ArrayList<String> inVariables;
	private ArrayList<String> inTerminals;
	private ArrayList<inputRuleFF> rules;

	public inputSampleReader(String input_data_file_name)
	{
		this.rules = new ArrayList<>();
		try
		{
			FileReader fr = new FileReader(input_data_file_name);
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			currentLine = br.readLine();
			this.inVariables = new ArrayList<String>(Arrays.asList(currentLine.split(",")));
			currentLine = br.readLine();
			this.inTerminals = new ArrayList<String>(Arrays.asList(currentLine.split(",")));
			while ((currentLine = br.readLine()) != null)
			{
				inputRuleFF tempRule = new inputRuleFF(currentLine);
				String[] tempRightSide = br.readLine().split("\\|");
				tempRule.setRightSide(new ArrayList<String>(Arrays.asList(tempRightSide)));
				this.rules.add(tempRule);
			}
			br.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<String> getInVariables()
	{
		return inVariables;
	}

	public void setInVariables(ArrayList<String> inVariables)
	{
		this.inVariables = inVariables;
	}

	public ArrayList<String> getInTerminals()
	{
		return inTerminals;
	}

	public void setInTerminals(ArrayList<String> inTerminals)
	{
		this.inTerminals = inTerminals;
	}

	public ArrayList<inputRuleFF> getRules()
	{
		return rules;
	}

	public void setRules(ArrayList<inputRuleFF> rules)
	{
		this.rules = rules;
	}

}
