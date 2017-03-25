package first.and.follow;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class mainAppFirstAndFollow
{
	private String file_name = "Sample4.in";

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
		inputRules = new inputSampleReader(file_name);
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

		// for (int i = 0; i < firsts.size(); i++)
		// {
		// //
		// System.out.println(firsts.get(i).getLeftside());
		// for (int j = 0; j < firsts.get(i).getTerminals().size(); j++)
		// {
		// System.out.println(firsts.get(i).getTerminals().get(j));
		// }
		// System.out.println(">>>>>>>>>><<<<<<<<<<<<");
		// }

		for (int i = 0; i < follows.size(); i++)
		{
			System.out.println(follows.get(i).getLeftside());
			for (int j = 0; j < follows.get(i).getTerminals().size(); j++)
			{
				System.out.println(follows.get(i).getTerminals().get(j));
			}
			System.out.println(">>>>>>>>>><<<<<<<<<<<<<");
		}
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
				if (!doneVars.contains(rules.get(i).getLeftSide()))
				{
					boolean allCalculated = true;

					for (int j = 0; j < rules.get(i).getRightSide().size(); j++)
					{
						if (!doneVars.contains(rules.get(i).getRightSide().get(j).charAt(0) + ""))
						{
							allCalculated = false;
						}
					}

					if (allCalculated)
					{
						ffDataStructure tempNew = new ffDataStructure(rules.get(i).getLeftSide());
						for (int j = 0; j < rules.get(i).getRightSide().size(); j++)
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
				String current_Var = variables.get(i);
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
					System.out.println(allCalculated);

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
												System.out.println("NEXT NEXT" + nextNext.matches("//s"));
												if (nextNext.matches(""))
												{
													System.out.println("HEREEEEEEEEEEEEEEEE");
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
													// TODO for sample 5
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

	public static void main(String[] args)
	{
		new mainAppFirstAndFollow();
	}
}
