package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads from an input file and writes to stdout.
 */
public class CmdProcessBuilderFiles
{
	public static void main(String args[]) throws InterruptedException, IOException
	{
		List<String> command = new ArrayList<String>();
		command.add("sort"); // DOS SORT cmd
		command.add("-r"); // reverse sort

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("work"));
		File wd = builder.directory();

		final Process process = builder.start();

		OutputStream os = process.getOutputStream();
		FileInputStream fis = new FileInputStream(new File(wd, "randomwords.txt"));
		int achar;
		while ((achar = fis.read()) != -1) {
			os.write(achar);
		}
		os.close();
		
		File outfile = new File(wd,"sortedwords.txt");
		FileOutputStream fos = new FileOutputStream(outfile);
		InputStream is = process.getInputStream();
		while ((achar = is.read()) != -1) {
			fos.write(achar);
			System.out.print((char) achar);
		}
		fos.close();
		
		System.out.println("Program terminated!");
	}
}
