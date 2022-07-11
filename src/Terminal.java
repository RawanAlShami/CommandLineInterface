import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Terminal 
{	
	public void echo (String[] args) 
	{
		if(args.length==1)
			System.out.println(args[0]); 
		else 
			throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 1 )");
	}
	
	public String pwd()
	{
		String CurrentlyOn = System.getProperty("user.dir");
		return CurrentlyOn;
	}
	
	public void cd(String[] args)
	{
		if(args.length== 0)
		{
		    String UserHomeDir = System.getProperty("user.home");
			System.setProperty("user.dir",UserHomeDir);
		}
		else if(args.length==1)
		{
			if(args[0].equals("..")) 
			{
				String PreviousDir=(Paths.get(System.getProperty("user.dir")).getParent()).toString();
				System.setProperty("user.dir",PreviousDir);
			}
			else
			{
				File DirectoryIP= new File(HandleShortPath(args[0]));
				if(DirectoryIP.isDirectory())
					System.setProperty("user.dir", DirectoryIP.getAbsolutePath());
				else 
					throw new IllegalArgumentException("Error: Directory Entered Does Not Exist");
			}
		}
		else throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( None | 1 )");	
	}

	public void ls(String[] args)
	{
		File MainFile = new File(System.getProperty("user.dir"));
		File[] SubFiles = MainFile.listFiles();
		  
		if(args.length==0)
			Arrays.sort(SubFiles);
		else if(args[0].equals("-r"))
			Arrays.sort(SubFiles, Collections.reverseOrder());
		else throw new IllegalArgumentException("Error: Invalid Argument. Valid Arguments ( None | -r )");	
        
        System.out.println("File Contents: ");
        
        for (int i = 0; i < SubFiles.length; i++)
        	System.out.println(SubFiles[i].getName());
	}
	
    public void rm(String[] args)
    {
    	if(args.length==1)
    	{
    		File ToBeDeleted = new File(HandleShortPath(args[0]));
            
            if(ToBeDeleted.exists())
            {
            	boolean Deleted=ToBeDeleted.delete();
            	if(Deleted)
            		System.out.println("File "+args[0]+" Deleted Successfully");
            }
            else if(!(ToBeDeleted.exists()))
            	throw new IllegalArgumentException("Error: File Name Entered Does Not Exist In Current Directory");
            else if(ToBeDeleted.listFiles().length==0)
                throw new IllegalArgumentException("Error: Directory Is Empty");
    	}
    	else throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 1 )");			
    }
    
    public void mkdir(String[] args)
	{
		if(args.length!=0)
		{
			int Failed=0;
			
			String Destination=args[args.length-1];
			File DestinationFile = new File(HandleShortPath(Destination));
			
			if(DestinationFile.exists())
			{
				String[] DestinationArray= {Destination};
				cd(DestinationArray);
			}
			else
			{
				boolean DirectoryCreated= DestinationFile.mkdir();
				if(!DirectoryCreated)
					Failed++;
			}
			
			for(int i=0; i<args.length-1; i++)
			{
				String Argument=args[i];
				
				File NewDirectory=new File(HandleShortPath(Argument));
				boolean DirectoryCreated=NewDirectory.mkdir();
				
				if(!DirectoryCreated)
					Failed++;
			}
			
			if(Failed==0)
				System.out.println("All Directories Created Successfully");
			else
				System.out.println("Failed To Created "+ Failed + " Directory/Directories");
		}
		else
			throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( >=1 )");
	}
	
    public void rmdir(String[] args)
	{
		if(args.length==1)
		{
			String Argument=args[0];
			if(Argument.equals("*"))
			{
				String CurrentlyOn = System.getProperty("user.dir");	
				File CurrentDirectory = new File(CurrentlyOn);
				File[] SubFiles = CurrentDirectory.listFiles();
				
				for(int i=0;i<SubFiles.length;i++)
				{
					if(SubFiles[i].isDirectory() && SubFiles[i].length()==0)
						SubFiles[i].delete();
				}
				System.out.println("Any Empty Directories Found Were Deleted Successfully");
			}
			else
			{
				String argument= args[0];
				
				boolean Deleted=true;
				File ToBeDeleted = new File(HandleShortPath(argument));
				
				if(ToBeDeleted.isDirectory())
				{
					if(ToBeDeleted.listFiles().length==0)
						Deleted = ToBeDeleted.delete(); 
					else
						throw new IllegalArgumentException("Error: Failed To Delete Directory, Make Sure It Is Empty");	

					if(Deleted)
						System.out.println("Directory Deleted Successfully");
					else
						throw new IllegalArgumentException("Error: Failed To Delete Directory");	
				}
				else
					throw new IllegalArgumentException("Error: Directory Provided Does Not Exist");	
			}
		}
		else
			throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 1 )");
	}
    
	public void touch(String[] args)
	{
	   	try
	   	{
	   		if(args.length==1)
	   		{
	   			File File = new File(HandleShortPath(args[0]));
		   		boolean status=File.createNewFile();
		   		
			    if(status)
				   System.out.println("File Created Successfully");
			    else
			    {
			    	File.setLastModified(System.currentTimeMillis());
			    	System.out.println("Date Modified Updated");
			    }
			}
	   		else
				throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 1 )");
	   	}
	   	catch (IOException e) {}
	}
	
	public void cp(String[] args) 
    {
		try 
		{
			if(args[0].equals("-r"))
			{cpr(args);}
			else
			{
				if(args.length!=2)
					throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 2 )");
				else
				{
			        String DestinationPath = args[1];
			        File DestinationFile = new File(HandleShortPath(DestinationPath));
			        
			        String SourcePath = args[0];
			        File SourceFile = new File(HandleShortPath(SourcePath));
			        
			        if (!(SourceFile.exists()) || !(SourceFile.isFile())) 
			            throw new IllegalArgumentException ("Error: Source Entered Is Either Not A File Or Does Not Exist");
			        else
			        {
			        	String ResultReturned = "";
						if(SourceFile.canRead())
						{
							try 
							{
								Scanner ReadLines = new Scanner(SourceFile);
								while (ReadLines.hasNextLine()) 
									ResultReturned += ReadLines.nextLine() + "\n";
								ReadLines.close();
			                }
							catch (FileNotFoundException e)
							{System.out.println("Error: Failed To Read File");}
						}
						else
							throw new IllegalArgumentException("Error: File Can Not Be Accessed");
						
						if (DestinationFile.exists())
						{
							if(DestinationFile.isFile())
							{
								try
								{
									FileWriter WriteLines = new FileWriter(DestinationFile,true);
									WriteLines.write("\n");
									WriteLines.write(ResultReturned);
									WriteLines.flush();
									WriteLines.close();
								}
								catch(IOException E)
								{System.out.println("Error: Unexpected Error Occurred");}
							}
							else
								throw new IllegalArgumentException ("Error: Destination Entered Is Not A File");
						}
			            else
			            	throw new IllegalArgumentException ("Error: Destination Does Not Exist");
			        }
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException E) {System.out.println("Error: Acceptable Number Of Arguments ( 2 )");}
    }
	
	public static void cpr(String[] args)
	{
		if(args.length==3)
		{
			String DestinationPath = args[2];
	        File DestinationFile = new File(HandleShortPath(DestinationPath));
	        
	        String SourcePath = args[1];
	        File SourceFile = new File(HandleShortPath(SourcePath));
		    
		    directoryCopy(SourceFile, DestinationFile);
		}
		else
			throw new IllegalArgumentException("Error: Acceptable Number Of Arguments ( 2 )");
	}
	
	public static void directoryCopy(File SourceFile, File DestinationFile)
	{
	    if(SourceFile.isDirectory())
	    {
	    	if(!DestinationFile.exists())
	    		DestinationFile.mkdir();
	    	
			File[] SubFile = SourceFile.listFiles();
			for(File file : SubFile)
			{
				File NewSourceFile =  new File(SourceFile, file.getName());
				File NewDestinationFile = new File(DestinationFile, file.getName());
				directoryCopy(NewSourceFile,NewDestinationFile);
		    }
		}
	    else
	    {
	    	try 
	    	{Files.copy(SourceFile.toPath(), DestinationFile.toPath());} 
	    	catch (IOException e) 
	    	{e.printStackTrace();}
	    }
	}
	
    public String cat(String[] args)
    {
    	String ResultReturned = "";
    	
    	if(args.length==1 ||args.length==2)
    	{
        	for(int i=0;i<args.length;i++)
        	{
        		File ArgumentFile=new File(HandleShortPath(args[i]));
        		
        		if(ArgumentFile.exists() && !(ArgumentFile.isDirectory()))
        		{
        			if(ArgumentFile.canRead())
        			{
        				try 
        				{
        					Scanner ReadLines = new Scanner(ArgumentFile);
        					while (ReadLines.hasNextLine()) 
        						ResultReturned += ReadLines.nextLine() + "\n";
        					ReadLines.close();
                        }
        				catch (FileNotFoundException e)
        				{System.out.println("Error: File Could Not Be Read");}
        			}
        			else
        				throw new IllegalArgumentException("Error: File Can Not Be Accessed");
        		}
        		else
        			throw new IllegalArgumentException("Error: File Name Entered Does Exist Not Or Is A Directory");	
        	}
    	}
    	else 
    		throw new IllegalArgumentException("Error: Invalid Number Of Arguments ( 1 | 2 ) ");	
    	return ResultReturned;
    }
    
	public void redirectAppend(String[] args)
	{
		String[] NewArgs;
		
		boolean Append=Arrays.asList(args).contains(">>");
		
		if(!Append)
			NewArgs= Arrays.copyOfRange(args,1,Arrays.asList(args).indexOf(">"));
		else
			NewArgs= Arrays.copyOfRange(args,1,Arrays.asList(args).indexOf(">>"));
		
		String NewCommand=args[0];
		PrintStream o;
		
		try 
		{
			File DestinationFile;
			
			if(!Append)
				DestinationFile = new File(HandleShortPath(args[Arrays.asList(args).indexOf(">")+1]));
			else
				DestinationFile = new File(HandleShortPath(args[Arrays.asList(args).indexOf(">>")+1]));
				
	   		if(!DestinationFile.exists())
	   		{
	   			try 
	   			{DestinationFile.createNewFile();} 
	   			catch (IOException e) 
	   			{e.printStackTrace();}
	   		}
		
	   		o = new PrintStream(new FileOutputStream(DestinationFile,Append));
			PrintStream console = System.out;	  
		    System.setOut(o);
		    
		    ChooseCommandAction(NewCommand,NewArgs);

		    o.close();
		    System.setOut(console);
		} 
		catch (FileNotFoundException e) 
		{e.printStackTrace();}
	}
	
	static String HandleShortPath(String Arg)
    {
        if(Arg.charAt(0)!=File.separatorChar)
        	Arg=System.getProperty("user.dir")+File.separatorChar+Arg;
        return Arg;
    }
    
    public void ChooseCommandAction(String CommandName,String [] Arguments)
    {  	
		switch(CommandName)
		{
			case "echo":
			{
				try
				{echo(Arguments);}	
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "pwd":
			{
				String Return;
				Return=pwd();
				System.out.println("Current Path: " + Return);
				break;
			}
			case "cd":
			{
				try{cd(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "ls":
			{
				try{ls(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "mkdir":
			{
				try{mkdir(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "rmdir":
			{
				try{rmdir(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "touch":
			{
				try{touch(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "cp":
			{
				try{cp(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}	
			case "rm":
			{
				try{rm(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "cat":
			{
				try{System.out.println(cat(Arguments));}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case "redirectAppend":
			{
				try{redirectAppend(Arguments);}
				catch(IllegalArgumentException E)
				{System.out.println(E.getMessage());}
				break;
			}
			case"exit":
			{
				System.out.println("Terminal Exited");
				System.exit(0);
				break;
			}
			default: 
			{
				System.out.println("Error: Invalid Command Name Entered");
				break;
			}
		}		
	}
}