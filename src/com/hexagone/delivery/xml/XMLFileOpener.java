/**
 * 
 */
package com.hexagone.delivery.xml;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import com.hexagone.delivery.xml.XMLException;

/**
 * This class provides the methods to open any xml file on the system.
 * 
 *
 */
public class XMLFileOpener extends FileFilter {

	private static XMLFileOpener instance = null;

	private XMLFileOpener() {
	}

	protected static XMLFileOpener getInstance() {
		if (instance == null)
			instance = new XMLFileOpener();
		return instance;
	}

	public File open() throws XMLException {
		int returnValue;
		JFileChooser jFileChooserXML = new JFileChooser();
		jFileChooserXML.setFileFilter(this);
		returnValue = jFileChooserXML.showOpenDialog(null);
		if (returnValue != JFileChooser.APPROVE_OPTION)
			throw new XMLException("Problem opening XML file");
		return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f == null)
			return false;
		if (f.isDirectory())
			return true;
		String extension = getExtension(f);
		if (extension == null)
			return false;
		return extension.contentEquals("xml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "XML File";
	}

	/**
	 * This method allows to get file's extension
	 * 
	 * @param f
	 *            the file whose extension we want to extract
	 * @return file extension as a String, null if the file has no extension
	 */
	private String getExtension(File f) {
		String filename = f.getName();
		int i = filename.lastIndexOf('.');
		if (i > 0 && i < filename.length() - 1)
			return filename.substring(i + 1).toLowerCase();
		return null;
	}

}
