package ro.mapassistent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MapPoint implements Serializable {

	public float refx;

	public float refy;

	public String pointName;

	public MapPoint() {
	}

	public MapPoint(String name, float x, float y) {
		super();
		this.refx = x;
		this.refy = y;
		this.pointName = name;

	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		try {

			refx = (float) aInputStream.readFloat();
			refy = (float) aInputStream.readFloat();
			pointName = (String) aInputStream.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This is the default implementation of writeObject. Customise if
	 * necessary.
	 */
	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		ByteArrayOutputStream bos = null;
		try {

			aOutputStream.writeObject(refx);
			aOutputStream.writeObject(refy);
			aOutputStream.writeObject(pointName);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return pointName;
		
	}

}
