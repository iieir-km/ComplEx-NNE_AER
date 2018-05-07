package struct;

public class Triple {
	private int iHeadEntity;
	private int iTailEntity;
	private int iRelation;
	
	public Triple() {
	}
	
	public Triple(int i, int j, int k) {
		iHeadEntity = i;
		iTailEntity = j;
		iRelation = k;
	}
	
	public int head() {
		return iHeadEntity;
	}
	
	public int tail() {
		return iTailEntity;
	}
	
	public int relation() {
		return iRelation;
	}
}
