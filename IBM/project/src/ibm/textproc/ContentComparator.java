package ibm.textproc;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alchemyapi.api.AlchemyAPI;

import ibm.model.Email;
import ibm.model.Word;

public class ContentComparator {
	
	public ContentComparator() {
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
					num+=(double)key1[1]*(double)key1[2];			
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
					num+=(double)key1[1]*(double)key1[2];			
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
					num+=(double)key1[1]*(double)key1[2];			
				}					
			}					
		}
		if(den1>0 && den2>0)
			return num*1.0/(Math.sqrt(den1)*Math.sqrt(den2));
		else 
			return 0;
	}
	
}
