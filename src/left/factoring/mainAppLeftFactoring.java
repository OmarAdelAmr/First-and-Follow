package left.factoring;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class mainAppLeftFactoring
{
	private String inputFile = "Sample3.in";

	private ArrayList<inputRule> inputRules;
	private ArrayList<outputRule> outputRules;

	public mainAppLeftFactoring()
	{
		inputDataReader x = new inputDataReader(inputFile);
		this.inputRules = x.getInput_rules();
		this.outputRules = new ArrayList<outputRule>();
		left_factoring();
		removeDuplicates();
		writeOutputFile();
	}

	public void left_factoring()
	{
		for (int i = 0; i < inputRules.size(); i++)
		{
			ArrayList<ArrayList<String>> outer = new ArrayList<>();
			String left_side = inputRules.get(i).getLeftSide();
			for (int j = 0; j < inputRules.get(i).getRightSide().size(); j++)
			{
				String current_right_rule = inputRules.get(i).getRightSide().get(j).getRule();
				boolean current_rule_flag = inputRules.get(i).getRightSide().get(j).isValid();
				String longest_prefix_sofar = current_right_rule;
				ArrayList<String> inner = new ArrayList<>();
				if (current_rule_flag)
				{
					inner.add(current_right_rule);
					inputRules.get(i).getRightSide().get(j).setValid(false);
					for (int k = j + 1; k < inputRules.get(i).getRightSide().size(); k++)
					{
						String compare_right_rule = inputRules.get(i).getRightSide().get(k).getRule();
						boolean compare_rule_flag = inputRules.get(i).getRightSide().get(k).isValid();
						if (compare_rule_flag)
						{
							String longest_prefix = getGreatestCommonPrefix(current_right_rule, compare_right_rule);
							if (longest_prefix.length() > 0)
							{
								inputRules.get(i).getRightSide().get(k).setValid(false);
								longest_prefix_sofar = longest_prefix;
								inner.add(compare_right_rule);
							}
						}
					}
					for (int k = 0; k < inner.size(); k++)
					{
						String temp_new_rule = inner.get(k);
						temp_new_rule = temp_new_rule.substring(longest_prefix_sofar.length());
						if (temp_new_rule.equals(""))
						{
							temp_new_rule = "!";
						}
						inner.set(k, temp_new_rule);
					}
					inner.add(0, longest_prefix_sofar);
					outer.add(inner);
				}
			}
			generateOutput(left_side, outer);
		}

		if (checkForMoreIteration())
		{
			inputRules.clear();
			for (int i = 0; i < outputRules.size(); i++)
			{
				inputRules.add(new inputRule(outputRules.get(i).getLeftSide()));
				for (int j = 0; j < outputRules.get(i).getRightSide().size(); j++)
				{
					inputRules.get(i).getRightSide().add(new rightSideRule(outputRules.get(i).getRightSide().get(j)));
				}
			}
			outputRules.clear();
			left_factoring();
		}
	}

	public boolean checkForMoreIteration()
	{
		boolean result = false;
		A: for (int i = 0; i < outputRules.size(); i++)
		{
			for (int j = 0; j < outputRules.get(i).getRightSide().size(); j++)
			{
				String comp1 = outputRules.get(i).getRightSide().get(j);
				for (int j2 = j + 1; j2 < outputRules.get(i).getRightSide().size(); j2++)
				{
					String comp2 = outputRules.get(i).getRightSide().get(j2);
					if (getGreatestCommonPrefix(comp1, comp2).length() > 0)
					{
						result = true;
						break A;
					}
				}
			}
		}
		return result;
	}

	public void generateOutput(String left, ArrayList<ArrayList<String>> outer)
	{
		int counter = 1;
		for (int i = 0; i < outer.size(); i++)
		{
			if (outer.get(i).size() > 2)
			{
				String temp_new_rule = left;
				for (int j = 0; j < counter; j++)
				{
					temp_new_rule += "'";
				}

				if (getCorrectRule(left) == -1)
				{
					outputRules.add(new outputRule(left));
				}
				outputRules.get(getCorrectRule(left)).getRightSide().add(outer.get(i).get(0) + temp_new_rule);

				if (getCorrectRule(temp_new_rule) == -1)
				{
					outputRules.add(new outputRule(temp_new_rule));
				}

				for (int j = 1; j < outer.get(i).size(); j++)
				{
					outputRules.get(getCorrectRule(temp_new_rule)).getRightSide().add(outer.get(i).get(j));
				}
				counter++;
			} else
			{
				if (getCorrectRule(left) == -1)
				{
					outputRules.add(new outputRule(left));
				}
				outputRules.get(getCorrectRule(left)).getRightSide().add(outer.get(i).get(0));
			}
		}
	}

	public int getCorrectRule(String left)
	{
		for (int i = 0; i < outputRules.size(); i++)
		{
			if (outputRules.get(i).getLeftSide().equals(left))
			{
				return i;
			}
		}
		return -1;
	}

	public String getGreatestCommonPrefix(String x, String y)
	{
		String res = "";
		int minLength = Math.min(x.length(), y.length());
		for (int i = 0; i < minLength; i++)
		{
			if (x.charAt(i) == y.charAt(i))
			{
				res += x.charAt(i) + "";
			} else
			{
				return res;
			}
		}
		return res;
	}

	public void removeDuplicates()
	{
		for (int i = 0; i < outputRules.size(); i++)
		{
			for (int j = i + 1; j < outputRules.size(); j++)
			{
				if (outputRules.get(i).getRightSide().containsAll(outputRules.get(j).getRightSide())
						&& outputRules.get(j).getRightSide().containsAll(outputRules.get(i).getRightSide()))
				{
					String to_remove = outputRules.get(j).getLeftSide();
					String replacement = outputRules.get(i).getLeftSide();
					outputRules.remove(j);
					for (int k = 0; k < outputRules.size(); k++)
					{
						for (int k2 = 0; k2 < outputRules.get(k).getRightSide().size(); k2++)
						{
							if (outputRules.get(k).getRightSide().get(k2).contains(to_remove))
							{
								outputRules.get(k).getRightSide().set(k2,
										outputRules.get(k).getRightSide().get(k2).replace(to_remove, replacement));
							}
						}
					}
				}
			}
		}
	}

	public void writeOutputFile()
	{
		try
		{
			String output = "";
			String output_file = inputFile.replaceAll("\\D+", "");
			PrintWriter fw = new PrintWriter(new FileWriter("Sample" + output_file + ".out"));
			for (int i = 0; i < outputRules.size(); i++)
			{
				output += outputRules.get(i).getLeftSide() + "->[";
				for (int j = 0; j < outputRules.get(i).getRightSide().size(); j++)
				{
					output += outputRules.get(i).getRightSide().get(j);
					if (j < outputRules.get(i).getRightSide().size() - 1)
					{
						output += ", ";
					}
				}
				output += "]" + "\n";
			}
			System.out.println(output);
			fw.print(output);
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new mainAppLeftFactoring();
	}

}
