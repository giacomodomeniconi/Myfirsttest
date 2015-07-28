package ibm.model;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Email {
	
	private int id;
	private int senderid;
	private ArrayList<Integer> receiversid;
	private String subject;
	private String text;
	private Timestamp timestamp;
	private ArrayList<Word> bow;
	private Document alchemy_keywords;
	private Document alchemy_concepts;
	private Document alchemy_entities;
	private Document alchemy_sentiment;
	
	public Email() {
		// TODO Auto-generated constructor stub
	}

	public Email(int senderid,String subject, String text) {
		this.senderid=senderid;
		this.subject=subject;
		this.text=text;
		this.receiversid=new ArrayList<>();
	}
	
	public Email(int id, int senderid,String subject, String text) {
		this.id=id;
		this.senderid=senderid;
		this.subject=subject;
		this.text=text;
		this.receiversid=new ArrayList<>();
	}

	public int getSender() {
		return senderid;
	}

	public void setSender(int senderid) {
		this.senderid = senderid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Integer> getReceiversid() {
		return receiversid;
	}

	public void setReceiversid(ArrayList<Integer> receiversid) {
		this.receiversid = receiversid;
	}
	
	public void insertReceiver(int userid){
		if(!receiversid.contains(userid))
			receiversid.add(userid);
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<Word> getBow() {
		return bow;
	}

	public void setBow(ArrayList<Word> bow) {
		this.bow = bow;
	}
	
	public void insertWord(String word, boolean inSubject){
		for(Word w : bow){
			if(w.word.equals(word)){
				w.incOccurence();
				return;
			}
		}
		this.bow.add(new Word(word,inSubject));
	}

	public Document getAlchemy_keywords() {
		return alchemy_keywords;
	}

	public void setAlchemy_keywords(Document alchemy_keywords) {
		this.alchemy_keywords = alchemy_keywords;
	}

	public Document getAlchemy_entities() {
		return alchemy_entities;
	}

	public void setAlchemy_entities(Document alchemy_entities) {
		this.alchemy_entities = alchemy_entities;
	}

	public Document getAlchemy_sentiment() {
		return alchemy_sentiment;
	}

	public void setAlchemy_sentiment(Document alchemy_sentiment) {
		this.alchemy_sentiment = alchemy_sentiment;
	}

	public Document getAlchemy_concepts() {
		return alchemy_concepts;
	}

	public void setAlchemy_concepts(Document alchemy_concepts) {
		this.alchemy_concepts = alchemy_concepts;
	}
	
	/**
	 * 
	 * @return Arraylist of object, each one contains: 0->(String) text, 1->(double) relevance, 2->(int) count, 3->(String) type
	 */
	public ArrayList<Object[]> getAlchemy_entities_List(){
		ArrayList<Object[]> list=new ArrayList<>();
		NodeList keyList = this.alchemy_entities.getElementsByTagName("entity");		
		
		for (int t = 0; t < keyList.getLength(); t++) {
			Node node = keyList.item(t);							 				 
			if (node.getNodeType() == Node.ELEMENT_NODE) {					 
				Element element = (Element) node;
				Object [] key=new Object[4];
				key[0]=element.getElementsByTagName("text").item(0).getTextContent();
				key[1]=Double.valueOf(element.getElementsByTagName("relevance").item(0).getTextContent());
				key[2]=Integer.valueOf(element.getElementsByTagName("count").item(0).getTextContent());
				key[3]=element.getElementsByTagName("type").item(0).getTextContent();
				list.add(key);
			}
		}
		return list;
	}

	/**
	 * 
	 * @return Arraylist of object, each one contains: 0->(String) keyword, 1->(double) relevance
	 */
	public ArrayList<Object[]> getAlchemy_keywords_List(){
		ArrayList<Object[]> list=new ArrayList<>();
		NodeList keyList = this.alchemy_keywords.getElementsByTagName("keyword");			
		for (int t = 0; t < keyList.getLength(); t++) {
			Node node = keyList.item(t);							 				 
			if (node.getNodeType() == Node.ELEMENT_NODE) {					 
				Element element = (Element) node;
				Object [] key=new Object[2];
				key[0]=element.getElementsByTagName("text").item(0).getTextContent();
				key[1]=Double.valueOf(element.getElementsByTagName("relevance").item(0).getTextContent());
				list.add(key);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @return Arraylist of object, each one contains: 0->(String) keyword, 1->(double) relevance
	 */
	public ArrayList<Object[]> getAlchemy_concepts_List(){
		ArrayList<Object[]> list=new ArrayList<>();
		NodeList keyList = this.alchemy_concepts.getElementsByTagName("concepts");			
		for (int t = 0; t < keyList.getLength(); t++) {
			Node node = keyList.item(t);							 				 
			if (node.getNodeType() == Node.ELEMENT_NODE) {					 
				Element element = (Element) node;
				Object [] key=new Object[2];
				key[0]=element.getElementsByTagName("text").item(0).getTextContent();
				key[1]=Double.valueOf(element.getElementsByTagName("relevance").item(0).getTextContent());
				list.add(key);
			}
		}
		return list;
	}
}
