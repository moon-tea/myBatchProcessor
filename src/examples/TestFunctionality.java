package examples;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.util.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

public class TestFunctionality 
{
    public static void main(String[] args)
    {
        // This was done to make everything work right.
        // It does not run properly if read() is translated
        // into the main method.

        // This will be replaced by command line arguments
        String file;
    	if( args.length == 0)
        {
        	System.out.println("Enter a file:");
        	Scanner input = new Scanner(System.in);
        	file = input.next();        	
        }
        else
        {
        	file = args[0];
        }
        read(file);
    }
    
    public static void read(String file)
    {
        try
        {
            //File xml = new File("batch1.xml");
            File xml = new File(file);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xml);
            
            Element pnode = doc.getDocumentElement();
            NodeList list = pnode.getChildNodes();
            
            List<String> fileList = new ArrayList<String>();
            List<String> cmdList = new ArrayList<String>();
            // Loop through the batch file
            for(int i = 0; i < list.getLength(); i++)
            {
                Node node = list.item(i);                
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element e = (Element) node;
                    String command = e.getNodeName();
                    
                    // Parse commands
                    parseCommand(e, fileList, cmdList);
                }
            }
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    // Parse the commands
    public static void parseCommand(Element e, List<String> fileList, List<String> cmdList) throws Exception
    {
        String command = e.getNodeName();
        
        if(command != null)
        {
            if("wd".equalsIgnoreCase(command))
            {
                System.out.println("Parsing wd");
                parseWd(e);
            }
            
            else if("file".equalsIgnoreCase(command))
            {
                System.out.println("Parsing file");
                String fileName = parseFile(e);
                fileList.add(fileName);
            }
            
            else if("cmd".equalsIgnoreCase(command))
            {
                System.out.println("Parsing cmd");
                List<String> commandList = parseCmd(e, fileList);
                
                boolean piping = false;
                System.out.println("PRINTING COMMANDS:");
                for(int i = 0; i < commandList.size(); i++)
                {
                	cmdList.add(commandList.get(i));
                	
                	//Check to see if we need to pipe
                	if(commandList.get(i).equalsIgnoreCase("pipe"))
                    {
                    	piping = true;
                    }
                	
                    System.out.print(commandList.get(i) + " ");
                }
                System.out.print("\n");
                
                //If we don't pipe, we will run the command
                if(!piping)
                {
                	runProcess(commandList, fileList);
                }                
            }
            
            else if("pipe".equalsIgnoreCase(command))
            {
                System.out.println("Parsing pipe");
                runPipe(cmdList, fileList);
            }
        }
        
        else
        {
            throw new Exception("Unknown command " + command + " from: " 
                    + e.getBaseURI());
        }
    }
    
    // Parse the wd command
    public static void parseWd(Element elem) throws Exception
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new Exception("Missing ID in WD Command");
		}
		System.out.println("ID: " + id);
		
		String path = ("../" + elem.getAttribute("path"));
		if (path == null || path.isEmpty()) {
			throw new Exception("Missing PATH in WD Command");
		}
		System.out.println("Path: " + path);
        }
    
    // Parse the file command
    public static String parseFile(Element elem) throws Exception
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new Exception("Missing ID in FILE Command");
		}
		System.out.println("ID: " + id);
		
		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new Exception("Missing PATH in FILE Command");
		}
		System.out.println("Path: " + path);
                
                // Return some kind of data structure related to the file
                return (id + " " + path);
        }
    
    // Parse the pipe command
    public static void parsePipe(Element elem) throws Exception
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new Exception("Missing ID in PIPE Command");
		}
		System.out.println("ID: " + id);
		
		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new Exception("Missing PATH in PIPE Command");
		}
		System.out.println("Path: " + path);
                
                String cmd1 = elem.getAttribute("cmd1");
		if (cmd1 == null || cmd1.isEmpty()) {
			throw new Exception("Missing CMD1 in FILE Command");
		}
		System.out.println("Command 1: " + cmd1);
                
                String cmd2 = elem.getAttribute("cmd2");
		if (cmd2 == null || cmd2.isEmpty()) {
			throw new Exception("Missing CMD2 in FILE Command");
		}
		System.out.println("Command 2: " + cmd2);
	}
    
    // Parse the cmd command
    public static List<String> parseCmd(Element elem, List<String> fileList) throws Exception
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new Exception("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		
		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new Exception("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);
                
                // Arguments must be passed to ProcessBuilder as a list of
		// individual strings. 
		List<String> cmdArgs = new ArrayList<String>();
                
                cmdArgs.add(path);

		String arg = elem.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}
		for(String argi: cmdArgs) {
			System.out.println("Arg: " + argi);
		}

                // Change file1, file2, etc to actual file names                
		String inID = elem.getAttribute("in");
		if (!(inID == null || inID.isEmpty())) {
			System.out.println("inID: " + inID);
                        
            for(int i = 0; i < fileList.size(); i++)
            {
                String file = fileList.get(i);
                StringTokenizer st = new StringTokenizer(file);
                String token = st.nextToken();
                
                if(inID.equals(token))
                {
                    System.out.println("Token match");
                    inID = st.nextToken();
                    System.out.println("New inID: " + inID);
                }
            }
		}

		String outID = elem.getAttribute("out");
		if (!(outID == null || outID.isEmpty())) {
			System.out.println("outID: " + outID);
            String temp = outID;
            
            for(int i = 0; i < fileList.size(); i++)
            {
                String file = fileList.get(i);
                StringTokenizer st = new StringTokenizer(file);
                String token = st.nextToken();
                
                if(outID.equals(token))
                {
                    System.out.println("Token match");
                    outID = st.nextToken();
                    System.out.println("New outID: " + outID);
                }
            }
            
            if(outID.equals("pipe"))
            {
                System.out.println("We will pipe this cmd eventually...");                
            }
            else if(outID.equals(temp))
            {
                System.out.println("Error: file is invalid");
                System.exit(0);
            }
	            
		}                   
                
        // Add input and output files to list of strings
        if(!(inID == null || inID.isEmpty()))
        {
            cmdArgs.add(inID);
        }
            
        if(!(outID == null || outID.isEmpty()))
        {
            cmdArgs.add(outID);
        }
        
        return cmdArgs;
	}
    
    // Run the pipe
    public static void runPipe(List<String> commandList, List<String> fileList) throws Exception
    {
    	List<String> cmd1 = new ArrayList<String>();
    	List<String> cmd2 = new ArrayList<String>();
    	for(int i = 0; i < commandList.size(); i++)
        {    		
    		if(!commandList.get(i).equalsIgnoreCase("pipe"))
    		{
	    		if (i < commandList.size()/2 )
	    		{
	    			System.out.println(i + " 1: " + commandList.get(i));
	    			cmd1.add(commandList.get(i));
	    		}
	    		else
	    		{
	    			System.out.println(i + " 2: " + commandList.get(i));
	    			cmd2.add(commandList.get(i));
	    		}
    		}
        }
    	for(int i = 0; i < fileList.size(); i++)
        {
    		System.out.println(i + ": " + fileList.get(i));
        }
    	
    	 Redirect bufferedReader = null;
         //bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	
    	ProcessBuilder builder = new ProcessBuilder();        
    	builder.command(cmd1);
    	builder.directory(new File("work"));
        builder.redirectError(new File("error.txt"));
              
        Process process = builder.start();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder sbuilder = new StringBuilder();
        String line = null;
        while ( (line = br.readLine()) != null) {
           sbuilder.append(line);
           sbuilder.append(System.getProperty("line.separator"));
        }
        String result = sbuilder.toString();
        //System.out.println("result");
        process.waitFor();
        
        builder = new ProcessBuilder();        
    	builder.command(cmd2);
    	builder.directory(new File("work"));
        builder.redirectError(new File("error.txt"));
        //builder.redirectInput(builder.)
        builder.redirectOutput(new File(commandList.get(commandList.size() - 1)));
        
        process = builder.start();
        
        process.waitFor();        
        System.out.println("Command has been processed.");
    }
    
    private static void inheritIO(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
            }
        }).start();
    }
   
    
    // Run the command
    public static void runProcess(List<String> commandList , List<String> fileList) throws Exception
    {
    	
    	ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandList);
        builder.directory(new File("work"));
        builder.redirectError(new File("error.txt"));
        builder.redirectOutput(new File(commandList.get(commandList.size() - 1)));
        
        Process process = builder.start();
        process.waitFor();
        
        System.out.println("Command has been processed.");
    }
}
