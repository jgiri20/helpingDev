/**
 * 
 */
package com.ca.devtest.sv.devtools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * @author gaspa03
 *
 */
public class PackMarFile {

	/**
	 * @param workingFolder
	 * @param config
	 * @return
	 * @throws IOException
	 */
	public static File packVirtualService(File workingFolder, @SuppressWarnings("rawtypes") Map config) throws IOException {

		File virtualServiceArchive = File.createTempFile("virtualServiceArchive", ".mar");
		ZipOutputStream zip = null;
		try {

			FileOutputStream fileWriter = null;
			/*
			 * create the output stream to zip file result
			 */
			fileWriter = new FileOutputStream(virtualServiceArchive);
			zip = new ZipOutputStream(fileWriter);

			FilenameFilter filter = new WildcardFileFilter(new String[] { "*.mari" });
			File[] lstFile = workingFolder.listFiles(filter);
			if (lstFile.length > 0) {

				File mariFile = lstFile[0];

				ZipEntry entry = new ZipEntry(".marinfo");
				zip.putNextEntry(entry);
				byte[] data = IOUtils.toByteArray(new FileInputStream(mariFile));
				data = VelocityRender.render(IOUtils.toString(data, Charset.defaultCharset().name()), config)
						.getBytes();
				zip.write(data, 0, data.length);
				zip.closeEntry();
				
				 entry = new ZipEntry(".maraudit");
				zip.putNextEntry(entry);
				 data = IOUtils.toByteArray(getMarAuditTpl());
				data = VelocityRender.render(IOUtils.toString(data, Charset.defaultCharset().name()), config)
						.getBytes();
				zip.write(data, 0, data.length);
				zip.closeEntry();

			}

			/*
			 * add the folder to the zip
			 */
			addFolderToZip("", workingFolder, zip, config);

		} finally {

			if (null != zip) {
				zip.flush();
				zip.close();
			}
		}
		return virtualServiceArchive;
	}

	private static InputStream getMarAuditTpl() {
		
		return Thread.currentThread().getContextClassLoader().getResourceAsStream("mar/maraudit.tpl");
	}

	/*
	 * recursively add files to the zip files
	 */
	private static void addFileToZip(String path, File folder, ZipOutputStream zip, boolean flag, @SuppressWarnings("rawtypes") Map config)
			throws IOException {

		/*
		 * if the folder is empty add empty folder to the Zip file
		 */
		if (flag == true) {
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		} else { /*
					 * if the current name is directory, recursively traverse it
					 * to get the files
					 */
			if (folder.isDirectory()) {
				/*
				 * if folder is not empty
				 */
				addFolderToZip(path, folder, zip, config);
			} else {
				/*
				 * write the file to the output
				 */
				if (shouldPack(folder)) {
					zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));

					byte[] data = IOUtils.toByteArray(new FileInputStream(folder));
					data = VelocityRender.render(IOUtils.toString(data, Charset.defaultCharset().name()), config)
							.getBytes();
					zip.write(data, 0, data.length);
				}

			}
		}
	}

	private static boolean shouldPack(File file) {

		return FilenameUtils.wildcardMatch(file.getName(), "*.project")
				|| FilenameUtils.wildcardMatch(file.getName(), "*.vsi")
				|| FilenameUtils.wildcardMatch(file.getName(), "*.vsm")
				|| FilenameUtils.wildcardMatch(file.getName(), "*.config");
	}

	/*
	 * add folder to the zip file
	 */
	private static void addFolderToZip(String path, File srcFolder, ZipOutputStream zip, @SuppressWarnings("rawtypes") Map config) throws IOException {

		/*
		 * check the empty folder
		 */
		if (srcFolder.list().length == 0) {
			System.out.println(srcFolder.getName());
			addFileToZip(path, srcFolder, zip, true, config);
		} else {
			/*
			 * list the files in the folder
			 */
			// FilenameFilter filter = new WildcardFileFilter(new String[] {
			// "*.vsm", "*.vsi", "*.config" });
			String[] files = srcFolder.list();

			for (String fileName : files) {
				if (path.equals("")) {
					addFileToZip(srcFolder.getName(), new File(srcFolder + "/" + fileName), zip, false, config);
				} else {
					addFileToZip(path + "/" + srcFolder.getName(), new File(srcFolder + "/" + fileName), zip, false,
							config);
				}
			}
		}
	}
}
