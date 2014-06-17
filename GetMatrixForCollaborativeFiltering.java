import java.io.*;

public class GetMatrixForCollaborativeFiltering {
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/data/userSim_truncated.3000"));
		FileWriter fstream1 = new FileWriter("src/data/3000train");
		BufferedWriter out1 = new BufferedWriter(fstream1);
		FileWriter fstream2 = new FileWriter("src/data/3000valid");
		BufferedWriter out2 = new BufferedWriter(fstream2);
		String line = br.readLine();
		int userID = 1;
		while(line!=null)
		{
			String parts[] = line.split("\t");
			int taskID =1;
			for(int i=1;i<parts.length;i++)
			{
				if(userID<161)
				{
					out1.write(userID+" "+taskID+" "+parts[i]);
					out1.write("\n");
				}
				else
				{
					out2.write(userID+" "+taskID+" "+parts[i]);
					out2.write("\n");
				}
				taskID++;
			}
			userID++;
			System.out.println(parts.length+"_"+taskID+"_"+userID);
			line = br.readLine();
		}
		out1.close();
		out2.close();
	}
}
