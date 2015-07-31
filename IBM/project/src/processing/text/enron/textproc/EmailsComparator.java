package processing.text.enron.textproc;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Table;

import processing.text.enron.model.Email;
import processing.text.enron.model.Word;
import utils.Util;

public class EmailsComparator {
	
	public EmailsComparator() {
		// TODO Auto-generated constructor stub
	}

	public static double compareContent_CosineSimilarity(Email email1, Email email2) {
		double den1=0, den2=0, num=0;
		for(Word w1 : email1.getBow()){	
			den1+=w1.getWeight()*w1.getWeight();
		}
		for(Word w2 : email2.getBow()){
			den2+=w2.getWeight()*w2.getWeight();				
			for(Word w1 : email1.getBow()){
				if(w1.getWord().equals(w2.getWord())){
					num+=w1.getWeight()*w2.getWeight();					
				}
			}				
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}
	
	public static double compareContent_SubjectCosineSimilarity(Email email1, Email email2) {
		double den1=0, den2=0, num=0;
		for(Word w1 : email1.getBow()){	
			if(w1.isInSubject())
				den1+=w1.getWeight()*w1.getWeight();
		}
		for(Word w2 : email2.getBow()){
			if(w2.isInSubject()){
				den2+=w2.getWeight()*w2.getWeight();				
				for(Word w1 : email1.getBow()){
					if(w1.isInSubject()){
						if(w1.getWord().equals(w2.getWord())){
							num+=w1.getWeight()*w2.getWeight();					
						}
					}					
				}	
			}						
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}
		
	public static double compareContent_SubjectJaccardSimilarity(Email email1, Email email2) {
		int intersection=0;
		ArrayList<String> words_union=new ArrayList<>();
		for(Word w1 : email1.getBow()){	
			if(w1.isInSubject())
				words_union.add(w1.getWord());
		}
		for(Word w2 : email2.getBow()){	
			if(w2.isInSubject()){
				if(words_union.contains(w2.getWord()))
					intersection++;
				else
					words_union.add(w2.getWord());
				
			}
		}
		if(words_union.size()>0)
			return (intersection*1.0/words_union.size());
		else 
			return 0;
	}
	
	public static double compareContent_AlchemykeywordCosineSimilairty(Email email1, Email email2) {
		double den1=0, den2=0, num=0;
		ArrayList<Object[]> key1_list=email1.getAlchemy_keywords_List();
		ArrayList<Object[]> key2_list=email2.getAlchemy_keywords_List();
		
		for(Object[] key1 : key1_list){	
			den1+=Math.pow((double)key1[1],2);
		}
		for(Object[] key2 : key2_list){	
			den2+=Math.pow((double)key2[1],2);		
			for(Object[] key1 : key1_list){	
				if(((String)key1[0]).equals((String)key2[0])){
					num+=(double)key1[1]*(double)key2[1];			
				}					
			}					
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}
	

	
	public static double compareContent_AlchemyEntitiesCosineSimilairty(Email email1, Email email2) {
		double den1=0, den2=0, num=0;
		ArrayList<Object[]> key1_list=email1.getAlchemy_entities_List();
		ArrayList<Object[]> key2_list=email2.getAlchemy_entities_List();
		
		for(Object[] key1 : key1_list){	
			den1+=Math.pow((double)key1[1],2);
		}
		for(Object[] key2 : key2_list){	
			den2+=Math.pow((double)key2[1],2);		
			for(Object[] key1 : key1_list){	
				if(((String)key1[0]).equals((String)key2[0])){
					num+=(double)key1[1]*(double)key2[1];			
				}					
			}					
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}
	

	
	public static double compareContent_AlchemyConceptsCosineSimilairty(Email email1, Email email2) {
		double den1=0, den2=0, num=0;
		ArrayList<Object[]> key1_list=email1.getAlchemy_concepts_List();
		ArrayList<Object[]> key2_list=email2.getAlchemy_concepts_List();		
		for(Object[] key1 : key1_list){	
			den1+=Math.pow((double)key1[1],2);
		}
		for(Object[] key2 : key2_list){	
			den2+=Math.pow((double)key2[1],2);		
			for(Object[] key1 : key1_list){	
				if(((String)key1[0]).equals((String)key2[0])){
					num+=(double)key1[1]*(double)key2[1];			
				}					
			}					
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}

	public static double compareTime_logDistInHours(Email e1, Email e2){
		long timestamp_diff=e1.getTimestamp().getTime() - e2.getTimestamp().getTime();
		double diff_Hours = timestamp_diff*1.0 / (60 * 60 * 1000);
		return Util.log2(1+(1/(Math.abs(diff_Hours))));
	}
	
	public static double compareTime_logDistInDays(Email e1, Email e2){
		long timestamp_diff=e1.getTimestamp().getTime() - e2.getTimestamp().getTime();
		double diff_Days = timestamp_diff*1.0 / (24 * 60 * 60 * 1000);
		return Util.log2(1+(1/(Math.abs(diff_Days))));
	}
	
	public static double comparePeople_UsersJaccardSimilarity(Email e1, Email e2){
		int intersection=0;
		ArrayList<Integer> users_union=new ArrayList<>();
		for(int u1 : e1.getIvolvedUsers()){	
			users_union.add(u1);
		}
		for(int u2 : e2.getIvolvedUsers()){	
			if(users_union.contains(u2))
				intersection++;
			else
				users_union.add(u2);
				
			
		}
		if(users_union.size()>0)
			return (intersection*1.0/users_union.size());
		else 
			return 0;
	}
	
}
