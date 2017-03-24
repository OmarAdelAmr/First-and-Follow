package left.factoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class inputDataReader
{

	private ArrayList<inputRule> input_rules;

	public inputDataReader(String input_data_file_name)
	{
		this.input_rules = new ArrayList<inputRule>();

		try
		{
			FileReader fr = new FileReader(input_data_file_name);
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			while ((currentLine = br.readLine()) != null)
			{
				inputRule temp = new inputRule(currentLine);
				currentLine = br.readLine();
				String[] rightSideArr = currentLine.split("\\|");
				ArrayList<rightSideRule> tempRightSideArr = new ArrayList<>();
				for (int i = 0; i < rightSideArr.length; i++)
				{
					tempRightSideArr.add(new rightSideRule(rightSideArr[i]));
				}
				temp.setRightSide(tempRightSideArr);
				this.input_rules.add(temp);
			}
			br.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<inputRule> getInput_rules()
	{
		return input_rules;
	}

	public void setInput_rules(ArrayList<inputRule> input_rules)
	{
		this.input_rules = input_rules;
	}

}
