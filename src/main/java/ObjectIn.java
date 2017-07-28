
public interface ObjectIn {
	static final int PLANT =1;
	static final int ZOMBIE =2;
	static final int BULLET =3;
	static final int MAGIC =4;
	int getRowID();
	int getLayer();
	int getObjectType();
	
}
