package lab_04;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FastaSequence 
{
	private final String header;
	private final String sequence;
	private float gccount;
	
	public FastaSequence(String header, String sequence)
	{
		this.header = header;
		this.sequence = sequence;
	}
	
	
	//returns the header of this sequence without the ">"
	public String getHeader()
	{		
		String str = header.substring(1);
		return str;
	}
	
	//returns the DNA sequence of this FastaSequence
	public String getSequence()
	{
		return sequence;
	}
	
	//returns the number of G's and C's divided by the length of this sequence
	public float getGCRatio()
	{
		for (int i = 0; i < sequence.length(); i++)
		{
			char c = sequence.charAt(i);
			if (c == 'C' || c == 'G') 
			{
				gccount++;
			}
		}
		return gccount / sequence.length();
	}
	
	//counts and returns the base in the sequence, for part 2
	public int countBase(char base)
	{
		int count = 0;
		for (int i = 0; i<sequence.length(); i++)
		{
			if (sequence.charAt(i) == base) 
			{
				count++;
			}
		} return count;
	}
		
	//strips the header description to just leave the ID
	public String getSeqID()
	{
		String arr[] = this.getHeader().split(" ", 2);
		return arr[0];
	}
	
	@Override
	public String toString()
	{
		return "Header:\t" + header + "\nSequence:\t" + sequence + "\nGCRatio:\t";
	}
	
	//factory method that parses a Fasta file and returns a list of FastaSequence objects
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		try (BufferedReader br = new BufferedReader ( new FileReader(filepath)))
		{
			List<FastaSequence> list = new ArrayList<FastaSequence>();
			
			String line;
			String header = null;
			StringBuilder sequence = new StringBuilder();
			
			while ((line = br.readLine()) != null) //while there are still lines in the file
			{
				if ( line.startsWith(">"))
				{
					if ( header != null)
					{
						//checks !null because if there is a header than an object is ready to be made
						//constructs object of previous fasta entry
						list.add(new FastaSequence(header,sequence.toString()));
					}
					//resets the information for new fasta entry
					header = line.substring(0); //makes header everything that isnt the ">"
					sequence.setLength(0); //resets the sequence to rebuild it
				}
				else
				{
					sequence.append(line);
				}
			}
			//one more list added because the loop only checks when ">" encountered, meaning last one is never added
			list.add(new FastaSequence(header, sequence.toString()));
			return list;
		}
	}
	
	//part 2
	//implement a method that inputs a List<FastaSequence> and outputs a columnar spreadsheet represented as a tab-separated .txt file
	public static void writeTableSummary(List<FastaSequence> list, File outputFile) throws Exception
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)))
		{
			//sets up header row
			writer.write("seqID\tnumA\tnumC\tnumG\tnumT\tsequence");
			writer.newLine();
			for (FastaSequence fasta : list) 
			{
				//adds the information of each FastaSequence object to each row
				String line = String.join("\t", fasta.getSeqID(), Integer.toString(fasta.countBase('A')), Integer.toString(fasta.countBase('C')), Integer.toString(fasta.countBase('G')), Integer.toString(fasta.countBase('T')), fasta.sequence);
				writer.write(line);
				writer.newLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> fastaList = FastaSequence.readFastaFile("C:\\Users\\Bryce\\git\\lab04\\fastatest.txt");
		
		for (FastaSequence fs : fastaList)
		{
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(fs.getGCRatio());
		}
		
		File myFile = new File("C:\\Users\\Bryce\\git\\lab04\\output.txt");
		writeTableSummary( fastaList, myFile);
	}

}
