package first.and.follow;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class mainAppFirstAndFollow
{
	private String file_name = "Sample5.in";

	private inputSampleReader inputRules;
	private ArrayList<String> terminals;
	private ArrayList<String> variables;
	private ArrayList<String> doneVars;
	private ArrayList<String> doneFollows;
	private ArrayList<ffDataStructure> firsts;
	private ArrayList<ffDataStructure> follows;
	private ArrayList<inputRuleFF> rules;
	private String startVar;

	public mainAppFirstAndFollow()
	{
		this.inputRules = new inputSampleReader(file_name);
		this.firsts = new ArrayList<>();
		this.follows = new ArrayList<>();
		this.terminals = inputRules.getInTerminals();
		this.variables = inputRules.getInVariables();
		this.doneVars = new ArrayList<>();
		this.doneFollows = new ArrayList<>();
		this.rules = inputRules.getRules();
		this.startVar = variables.get(0);

		calculateFirsts();
		calculateFollows();
		createOutputFile();
	}

	public void calculateFirsts()
	{
		for (int i = 0; i < inputRules.getRules().size(); i++)
		{
			boolean allTerminals = true;
			A: for (int j = 0; j < inputRules.getRules().get(i).getRightSide().size(); j++)
			{
				if (!(terminals.contains(inputRules.getRules().get(i).getRightSide().get(j).charAt(0) + "")
						|| inputRules.getRules().get(i).getRightSide().get(j).charAt(0) == '!'
						|| terminals.contains(inputRules.getRules().get(i).getRightSide().get(j))))
				{
					allTerminals = false;
					break A;
				}
			}
			if (allTerminals)
			{
				ffDataStructure tempFirst = new ffDataStructure(inputRules.getRules().get(i).getLeftSide());
				doneVars.add(inputRules.getRules().get(i).getLeftSide());
				for (int j = 0; j < inputRules.getRules().get(i).getRightSide().size(); j++)
				{
					if (terminals.contains(inputRules.getRules().get(i).getRightSide().get(j)))
					{
						tempFirst.getTerminals().add(inputRules.getRules().get(i).getRightSide().get(j));
					} else
					{
						tempFirst.getTerminals().add(inputRules.getRules().get(i).getRightSide().get(j).charAt(0) + "");
					}
				}
				firsts.add(tempFirst);
			}
		}

		while (firsts.size() < variables.size())
		{
			for (int i = 0; i < rules.size(); i++)
			{
				String currentVar = rules.get(i).getLeftSide();
				if (!doneVars.contains(rules.get(i).getLeftSide()))
				{
					boolean allCalculated = true;
					for (int j = 0; j < rules.get(i).getRightSide().size(); j++)
					{
						if (!doneVars.contains(rules.get(i).getRightSide().get(j).charAt(0) + "")
								&& !rules.get(i).getRightSide().get(j).startsWith(rules.get(i).getLeftSide())
								&& !rules.get(i).getRightSide().get(j).equals("!"))
						{
							allCalculated = false;
						} else if (!doneVars.contains(rules.get(i).getRightSide().get(j).charAt(0) + "")
								&& rules.get(i).getRightSide().get(j).startsWith(rules.get(i).getLeftSide()))
						{
							if (hasEpsilon(rules.get(i).getLeftSide()) && !doneVars.contains(rules.get(i).getRightSide()
									.get(j).substring(rules.get(i).getLeftSide().length()).charAt(0) + ""))
							{
								allCalculated = false;
							}
						}
					}

					if (allCalculated)
					{
						ffDataStructure tempNew = new ffDataStructure(rules.get(i).getLeftSide());
						for (int j = 0; j < rules.get(i).getRightSide().size(); j++)
						{
							if (currentVar.equals(rules.get(i).getRightSide().get(j).charAt(0) + "")
									&& hasEpsilon(currentVar))
							{
								A: for (int j2 = 1; j2 < rules.get(i).getRightSide().get(j).length(); j2++)
								{
									for (int k = 0; k < firsts.size(); k++)
									{
										if (firsts.get(k).getLeftside()
												.equals(rules.get(i).getRightSide().get(j).charAt(j2) + ""))
										{
											tempNew.getTerminals().addAll(firsts.get(k).getTerminals());
											if (!hasEpsilon(firsts.get(k).getLeftside()))
											{
												break A;
											}
										}
									}
								}
							} else
							{
								for (int j2 = 0; j2 < firsts.size(); j2++)
								{
									if (firsts.get(j2).getLeftside()
											.equals(rules.get(i).getRightSide().get(j).charAt(0) + ""))
									{
										for (int k = 0; k < firsts.get(j2).getTerminals().size(); k++)
										{
											tempNew.getTerminals().add(firsts.get(j2).getTerminals().get(k));
										}
									}
								}
								tempNew.getTerminals().add("");
							}
						}
						Set<String> hs = new HashSet<>();
						hs.addAll(tempNew.getTerminals());
						tempNew.getTerminals().clear();
						tempNew.getTerminals().addAll(hs);
						tempNew.getTerminals().remove("");
						firsts.add(tempNew);
						doneVars.add(rules.get(i).getLeftSide());
					}
				}
			}
		}
	}

	public void calculateFollows()
	{
		while (follows.size() < variables.size())
		{
			for (int i = 0; i < variables.size(); i++)
			{
				if (!doneFollows.contains(variables.get(i)))
				{
					boolean allCalculated = true;
					for (int j = 0; j < rules.size(); j++)
					{
						for (int j2 = 0; j2 < rules.get(j).getRightSide().size(); j2++)
						{
							if (rules.get(j).getRightSide().get(j2).contains(variables.get(i))
									&& !rules.get(j).getRightSide().get(j2).contains(variables.get(i) + "'"))
							{
								if (rules.get(j).getRightSide().get(j2).endsWith(variables.get(i))
										&& !rules.get(j).getLeftSide().equals(variables.get(i)))
								{
									if (!doneFollows.contains(rules.get(j).getLeftSide()))
									{
										allCalculated = false;
									}
								}
							}
						}
					}

					if (allCalculated)
					{
						boolean check = true;
						ffDataStructure tempNew = new ffDataStructure(variables.get(i));
						if (variables.get(i).equals(startVar))
						{
							tempNew.getTerminals().add("$");
						}

						A: for (int j = 0; j < rules.size(); j++)
						{
							for (int j2 = 0; j2 < rules.get(j).getRightSide().size(); j2++)
							{
								if (rules.get(j).getRightSide().get(j2).contains(variables.get(i))
										&& !rules.get(j).getRightSide().get(j2).contains(variables.get(i) + "'"))
								{
									if (rules.get(j).getRightSide().get(j2).endsWith(variables.get(i))
											&& !rules.get(j).getLeftSide().equals(variables.get(i)))
									{
										if (doneFollows.contains(rules.get(j).getLeftSide()))
										{
											for (int k = 0; k < follows.size(); k++)
											{
												if (follows.get(k).getLeftside().equals(rules.get(j).getLeftSide()))
												{
													tempNew.getTerminals().addAll(follows.get(k).getTerminals());
												}
											}
										}
									} else if (!rules.get(j).getRightSide().get(j2).endsWith(variables.get(i)))
									{
										String nextElem = getNext(variables.get(i),
												rules.get(j).getRightSide().get(j2));
										if (terminals.contains(nextElem))
										{
											tempNew.getTerminals().add(nextElem);
										} else if (variables.contains(nextElem))
										{
											for (int k = 0; k < firsts.size(); k++)
											{
												if (firsts.get(k).getLeftside().equals(nextElem))
												{
													tempNew.getTerminals().addAll(firsts.get(k).getTerminals());
												}
											}
											if (hasEpsilon(nextElem))
											{
												String nextNext = getNext(nextElem,
														rules.get(j).getRightSide().get(j2));
												if (nextNext.matches(""))
												{
													check = false;
													for (int l = 0; l < follows.size(); l++)
													{
														if (follows.get(l).getLeftside()
																.equals(rules.get(j).getLeftSide()))
														{
															tempNew.getTerminals()
																	.addAll(follows.get(l).getTerminals());
															check = true;
														}
													}
													if (!check)
													{
														break A;
													}

												} else
												{
													B: for (int k = rules.get(j).getRightSide().get(j2)
															.indexOf(nextElem); k < rules.get(j).getRightSide().get(j2)
																	.length(); k++)
													{
														for (int k2 = 0; k2 < firsts.size(); k2++)
														{
															if (firsts.get(k2).getLeftside().equals(
																	rules.get(j).getRightSide().get(j2).charAt(k) + ""))
															{
																tempNew.getTerminals()
																		.addAll(firsts.get(k2).getTerminals());
																if (!hasEpsilon(firsts.get(k2).getLeftside()))
																{
																	break B;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
						if (check)
						{
							Set<String> hs = new HashSet<>();
							hs.addAll(tempNew.getTerminals());
							tempNew.getTerminals().clear();
							tempNew.getTerminals().addAll(hs);
							tempNew.getTerminals().remove("!");
							tempNew.getTerminals().remove("");
							follows.add(tempNew);
							doneFollows.add(variables.get(i));
						}
					}
				}
			}
		}
	}

	public boolean hasEpsilon(String varIn)
	{
		boolean res = false;
		for (int i = 0; i < rules.size(); i++)
		{
			for (int j = 0; j < rules.get(i).getRightSide().size(); j++)
			{
				if (rules.get(i).getRightSide().get(j).equals("!"))
				{
					return true;
				}
			}
		}
		return res;
	}

	public String getNext(String x, String y)
	{
		String res = "";
		int index = y.indexOf(x);
		y = y.substring(index + x.length());

		A: for (int i = 0; i < y.length(); i++)
		{
			if (variables.contains(y.substring(0, y.length() - i))
					|| terminals.contains(y.substring(0, y.length() - i)))
			{
				res = y.substring(0, y.length() - i);
				break A;
			}
		}
		return res;
	}

	public void createOutputFile()
	{
		try
		{
			String output = "";
			String output_file = file_name.replaceAll("\\D+", "");
			PrintWriter fw = new PrintWriter(new FileWriter("Sample" + output_file + ".out"));
			for (int i = 0; i < terminals.size(); i++)
			{
				output += "First(" + terminals.get(i) + "): [" + terminals.get(i) + "]\n";
			}
			for (int i = 0; i < variables.size(); i++)
			{
				for (int j = 0; j < firsts.size(); j++)
				{
					if (variables.get(i).equals(firsts.get(j).getLeftside()))
					{
						output += "First(" + variables.get(i) + "): [" + convertToString(firsts.get(j).getTerminals())
								+ "]";
						output += "\n";
					}
				}

			}

			for (int i = 0; i < variables.size(); i++)
			{
				for (int j = 0; j < follows.size(); j++)
				{
					if (variables.get(i).equals(follows.get(j).getLeftside()))
					{
						output += "Follow(" + variables.get(i) + "): [" + convertToString(follows.get(j).getTerminals())
								+ "]";
						output += "\n";
					}
				}

			}
			System.out.println(output);
			fw.print(output);
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String convertToString(ArrayList<String> x)
	{
		String res = "";
		for (int i = 0; i < x.size(); i++)
		{
			res += x.get(i);
			if (i < x.size() - 1)
			{
				res += ", ";
			}
		}
		return res;
	}

	public static void main(String[] args)
	{
		new mainAppFirstAndFollow();
	}
}
