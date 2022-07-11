import java.util.Arrays;

public class Parser 
{
	String commandName;
	String[] args;
	
	public boolean parse(String input)
	{
		args=null;
		String[] arrOfStr = input.split(" ");
		
		if(Arrays.asList(arrOfStr).contains(">") || Arrays.asList(arrOfStr).contains(">>"))
		{	
			commandName="redirectAppend";
			args=Arrays.copyOf(arrOfStr,arrOfStr.length);
		}
		else
		{
			commandName=arrOfStr[0];
			args = Arrays.copyOfRange(arrOfStr,1,arrOfStr.length);
		}
		
		return true;
	}
	 	
	public String GetCommandName(){ return commandName; }
	 		
	public String[] GetArgs() { return args; }
}