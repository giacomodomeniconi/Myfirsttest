package processing.text.main;
//STEP 1. Import required packages
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import processing.text.enron.model.Email;
import processing.text.enron.model.ValueComparatorInt;
import processing.text.enron.textproc.EmailsComparator;
import processing.text.enron.textproc.TextPreprocessing;
import utils.Util;

public class EmailsDimensionComparator {

	static final String DATASET="bc3";
	static final String DB_URL = "jdbc:mysql://shadow.cs.uoi.gr:3306/"+DATASET;
	static final String USER = "giacomo";
	static final String PASS = "12345";
	
	private Table<Integer, Integer, Double> time_dim_map = HashBasedTable.create();	  
	private Table<Integer, Integer, Double> peole_dim_map = HashBasedTable.create();	  
	private Table<Integer, Integer, Double> text_dim_map = HashBasedTable.create();	  
	private Table<Integer, Integer, Double> score_dim_map = HashBasedTable.create();	  
   	
	public static void main(String[] args) {
		new EmailsDimensionComparator();	
	}
   
	public EmailsDimensionComparator() {
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);	 
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			//String sql = "SELECT * FROM email_messages WHERE subject LIKE '%Hey!%' limit 1000";
			String sql =  "SELECT *"
			+ " FROM email_messages e, communications c WHERE "//e.subject LIKE '%Hey!%' AND"
			+ " e.email_id = c.email_id  LIMIT 500000";
			  
			  
			ResultSet rs = stmt.executeQuery(sql);
			//ArrayList<Email> emails=new ArrayList<>();
			//ArrayList<Integer> emails_IDs=new ArrayList<>();
			HashMap<Integer, Email> emails_map=new HashMap<>();
			  
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			while(rs.next()){
				int id  = rs.getInt("email_id");
				Email e=emails_map.get(id);
				if(e==null){
					int senderid  = rs.getInt("sender_id");
				int thireadid  = rs.getInt("thread_id");
				String text = rs.getString("text");
				String clean_text= rs.getString("clean_text");
				String quotation_text= rs.getString("quotation_text");
				String alchemy_doc= rs.getString("alchemy");
				String subject=rs.getString("subject");
				subject=subject.toLowerCase();       
				subject=subject.replaceAll("re:", "");
				subject=subject.replaceAll("fw:", "");
				subject=subject.replaceAll("fwd:", "");		
								
				e=new Email(id,thireadid, senderid, subject, text);
				e.insertReceiver(rs.getInt("recipient_id"));
				e.setTimestamp(rs.getTimestamp("time"));
				e.setCleanContent(clean_text);
				e.setCleanQuotes(quotation_text);
				e.setAlchemy_document(Util.getDocumentFromXmlString(alchemy_doc));
			}
			else
				e.insertReceiver(rs.getInt("recipient_id"));		 
			emails_map.put(id,e);	         
		}
		rs.close();
		stmt.close();
		ArrayList<Email> emails=new ArrayList<>();
		for(Email e : emails_map.values())
			emails.add(e);	      			
		
		
	    System.out.println("BoWs Creation...");
		emails=TextPreprocessing.createBowAndWeight(emails, true, true,2, DATASET);

		
				
		HashMap<Integer, HashMap<Integer, Double>> score_map=new HashMap<>();
	  	
	  	for(int i=0;i<emails.size()-1;i++){	  		
			Email ei=emails.get(i);
			HashMap<Integer, Double> ei_map=new HashMap<>();	
			
			for(int j=0;j<emails.size();j++){	  		
				Email ej=emails.get(j);
			
			 	//System.out.println("text: \n"+ ei.getText());
				double simcos_text=EmailsComparator.compareContent_CosineSimilarity(ei, ej);
				double simcos_subj=EmailsComparator.compareContent_SubjectCosineSimilarity(ei, ej);
				double jacc_subj=EmailsComparator.compareContent_SubjectJaccardSimilarity(ei, ej);
				double simcos_concepts=EmailsComparator.compareContent_AlchemyConceptsCosineSimilairty(ei, ej);
				double simcos_keywords=EmailsComparator.compareContent_AlchemykeywordCosineSimilairty(ei, ej);
				double simcos_entities=EmailsComparator.compareContent_AlchemyEntitiesCosineSimilairty(ei, ej);		
				
				double jacc_users=EmailsComparator.comparePeople_UsersJaccardSimilarity(ei,ej);
				System.out.println(jacc_users);
				
				double time_dist=EmailsComparator.compareTime_logDistInDays(ei, ej);
				
				int id1=ei.getId();
				int id2=ej.getId();
				if(id1>id2){
					id2=ei.getId();
					id1=ej.getId();
				}

				this.time_dim_map.put(ei.getId(), ej.getId(), time_dist);
				this.peole_dim_map.put(ei.getId(), ej.getId(), jacc_users);
				this.text_dim_map.put(ei.getId(), ej.getId(), (simcos_text+simcos_subj+simcos_concepts+simcos_entities+simcos_keywords));
				this.score_dim_map.put(ei.getId(), ej.getId(), (1+simcos_text+simcos_subj+simcos_concepts+simcos_entities+simcos_keywords)*(1+time_dist)*(1+jacc_users));
				
				ei_map.put(ej.getId(), (1+simcos_text+simcos_subj+simcos_concepts+simcos_entities+simcos_keywords)*(1+time_dist)*(1+jacc_users));
			}
			score_map.put(ei.getId(), ei_map);
			
	  	}
	  	
	  	
	  	
	  	
		
		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
	      e.printStackTrace();
		}finally{
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }// nothing we can do
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
		}//end try
	}
   
	public Email readEmailConnections(Email email,  Connection conn){
		try{
		  Statement stmt = conn.createStatement();
	      String sql = "SELECT * FROM communications WHERE email_id="+email.getId();
	      ResultSet rs = stmt.executeQuery(sql);
	      while(rs.next()){
	    	  email.insertReceiver(rs.getInt("recipient_id"));
	    	  email.setSender(rs.getInt("sender_id"));
	    	  email.setTimestamp(rs.getTimestamp("time"));
	      }

	      rs.close();
	      stmt.close();
	  }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
	  return email;   	  
   }

	public Table<Integer, Integer, Double> getTime_dim_map() {
		return time_dim_map;
	}

	public void setTime_dim_map(Table<Integer, Integer, Double> time_dim_map) {
		this.time_dim_map = time_dim_map;
	}

	public Table<Integer, Integer, Double> getPeole_dim_map() {
		return peole_dim_map;
	}

	public void setPeole_dim_map(Table<Integer, Integer, Double> peole_dim_map) {
		this.peole_dim_map = peole_dim_map;
	}

	public Table<Integer, Integer, Double> getScore_dim_map() {
		return score_dim_map;
	}

	public void setScore_dim_map(Table<Integer, Integer, Double> score_dim_map) {
		this.score_dim_map = score_dim_map;
	}

	public Table<Integer, Integer, Double> getText_dim_map() {
		return text_dim_map;
	}

	public void setText_dim_map(Table<Integer, Integer, Double> text_dim_map) {
		this.text_dim_map = text_dim_map;
	}        
	
   
}