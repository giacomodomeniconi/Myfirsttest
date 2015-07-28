package enron.construct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class getEmails {
	private static String mailDir = "C:\\Users\\IBM_ADMIN\\Desktop\\DATASET\\maildir\\";
	private static String from = null;
	private static String[] to = null;
	private static String[] cc = null;
	private static String[] bcc = null;
	private static Set<String> emails = new HashSet<>();

	public static void main(String[] args) throws IOException {
		
		Files.walk(Paths.get(mailDir)).forEach(filePath -> {

		    if (Files.isRegularFile(filePath)) {
				String line = null;

				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString()), "UTF8"));
				 			 
					while ((line = br.readLine()) != null) {		
						if (from == null)
							from = Mail.getFrom(line);
							
						if (to == null)
							to = Mail.getTo(line);
						
						if (cc == null)
							cc = Mail.getCC(line);
						
						if (bcc == null)
							bcc = Mail.getBCC(line);
						
						if (line.contains("X-"))
							break;
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (from != null && to != null) {
					if (!emails.contains(from))
						emails.add(from);
					
					for (int i = 0; i < to.length; i++)
						if (!emails.contains(to[i]))
							emails.add(to[i]);
					
					if (cc != null)
						for (int i = 0; i < cc.length; i++)
							if (!emails.contains(cc[i]))
								emails.add(cc[i]);
					
					if (bcc != null)
						for (int i = 0; i < bcc.length; i++)
							if (!emails.contains(bcc[i]))
								emails.add(bcc[i]);
				}
				from = null; to = null; bcc = null; cc = null;
		    }
		});
		
		FileWriter writer = new FileWriter("emails");
		
		for (String email : emails)
			if (!email.isEmpty())
				writer.write(email.toLowerCase() + "\n");
		
		writer.close();
	}
}