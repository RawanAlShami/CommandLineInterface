import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
		Scanner Scanner = new Scanner(System.in);
		Boolean Continue = true;
		
		Terminal T=new Terminal();
		Parser P = new Parser();
		
		while(Continue)
		{
			String CommandLine = Scanner.nextLine();
				 
			String CommandName;
			String[] Arguments;
			 
			P.parse(CommandLine);
			CommandName=P.GetCommandName();
			Arguments=P.GetArgs();
			
			T.ChooseCommandAction(CommandName,Arguments);
		}
		Scanner.close();
	}
}
