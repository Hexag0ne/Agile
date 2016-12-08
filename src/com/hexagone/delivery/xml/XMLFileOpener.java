/**
 * Package containing all the classes that deal with loading and parsing XML files into object of our model
 */
package com.hexagone.delivery.xml;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * This class provides the methods to open any xml file on the system.
 */
public class XMLFileOpener extends FileFilter {
	/**
	 * This class is a singleton
	 */

	/** Unique instance of the File Opener */
	private static XMLFileOpener instance = null;

	/**
	 * Private Constructor
	 */
	private XMLFileOpener() {
	}

	/**
	 * This method allows the access to the unique instance of the FileOpener
	 * 
	 * @return the XMLFile opener that will allow the user to choose a file on
	 *         the file system and open it.
	 */
	protected static XMLFileOpener getInstance() {
		if (instance == null)
			instance = new XMLFileOpener();
		return instance;
	}

	/**
	 * This method opens a FileChosser to let the user choose a file on the
	 * system.
	 * 
	 * @return an XML file as the File java object
	 * @throws XMLException
	 *             if the user cancels the operation
	 */
	public File open() throws NoFileChosenException {
		int returnValue;
		JFileChooser jFileChooserXML = new JFileChooser();
		jFileChooserXML.setFileFilter(this);
		returnValue = jFileChooserXML.showOpenDialog(null);
		if (returnValue != JFileChooser.APPROVE_OPTION)
			throw new NoFileChosenException("No file was selected");
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
	private static String getExtension(File f) {
		String filename = f.getName();
		int i = filename.lastIndexOf('.');
		if (i > 0 && i < filename.length() - 1)
			return filename.substring(i + 1).toLowerCase();
		return null;
	}

}
