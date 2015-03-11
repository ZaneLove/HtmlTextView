package com.zanelove.ViewHtmlTextDemo.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import org.apache.http.util.EncodingUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTool {
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/ZaneLove";
	public void savaBitmap(Bitmap bitmap, String name) {

		String filename = SDPATH + name;

		File f = new File(filename);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void savaInputStreamImage(InputStream inputStream, String name) {

		Bitmap bm = null;
		String filename = SDPATH + "Image/" + name;

		bm = BitmapFactory.decodeStream(inputStream);

		// byte[] bt;
		// bt = getBytes(inputStream);
		// bm = BitmapFactory.decodeByteArray(bt, 0, bt.length);

		File f = new File(filename);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		pathString = SDPATH + "Image/" + pathString;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {

		}

		return bitmap;
	}

	public byte[] getBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(b, 0, 1024)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	
	public boolean checkFileExists(String filepath) {
		File file = new File(SDPATH + filepath);
		return file.exists();
	}

	public boolean checkFileExistsFrom(String filepath) {
		String SDPATH = Environment.getExternalStorageDirectory() + "/Offcn/";
		File file = new File(SDPATH + filepath);
		return file.exists();
	}

	
	public File createDIR(String dirpath) {
		File dir = new File(SDPATH + dirpath);
		dir.mkdir();
		return dir;
	}

	
	public File createDIRFrom(String dirpath) {
		String SDPATH = Environment.getExternalStorageDirectory() + "/Offcn/";
		File dir = new File(SDPATH + dirpath);
		dir.mkdir();
		return dir;
	}

	
	public File createFile(String filepath) {
		File file = new File(SDPATH + filepath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	
	public void writeFileSdcardFile(String fileName, String write_str) {
		fileName = SDPATH + fileName;
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String readFileSdcardFile(String fileName) {
		fileName = SDPATH + fileName;
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public void deleteFile_FroSDcard(boolean is_zixun, boolean is_lianxiti) {
		File file = new File(SDPATH);
		if (file.exists() && file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && !files[i].toString().equals(SDPATH + "MY_SUBSCRIPTION")) {
					if (is_zixun) {
						Pattern pattern = Pattern.compile("^" + SDPATH + "Exam_Info.*|^" + SDPATH + "Teacher_Coach.*");
						Matcher matcher = pattern.matcher(files[i].toString());
						if (matcher.matches()) {
							files[i].delete();
						}
					}
					if (is_lianxiti) {
						if (files[i].toString().equals(SDPATH + "My_Daily_Exams") || files[i].toString().equals(SDPATH + "Daily_Exams")) {
							files[i].delete();
						}
					}
				}
			}
		}

		// Daily_Exams
		File file_Daily_Exams = new File(SDPATH + "Daily_Exams/");
		if (file_Daily_Exams.exists() && file_Daily_Exams.isDirectory()) {
			File files_Daily_Exams_list[] = file_Daily_Exams.listFiles();
			for (int i = 0; i < files_Daily_Exams_list.length; i++) {
				files_Daily_Exams_list[i].delete();
			}
		}

		// Download
		File file_Download = new File(SDPATH + "Download/");
		if (file_Download.exists() && file_Download.isDirectory()) {
			File files_Download_list[] = file_Download.listFiles();
			for (int i = 0; i < files_Download_list.length; i++) {
				files_Download_list[i].delete();
			}
		}

		// Image
		File file_Image = new File(SDPATH + "Image/");
		if (file_Image.exists() && file_Image.isDirectory()) {
			File files_Image_list[] = file_Image.listFiles();
			for (int i = 0; i < files_Image_list.length; i++) {
				deleteDirAll(files_Image_list[i]);
			}
		}

		// Infos
		File file_Infos = new File(SDPATH + "Infos/");
		if (file_Infos.exists() && file_Infos.isDirectory()) {
			File files_Infos_list[] = file_Infos.listFiles();
			for (int i = 0; i < files_Infos_list.length; i++) {
				if (!files_Infos_list[i].toString().equals(SDPATH + "Infos/MY_SUBSCRIPTION")) {
					files_Infos_list[i].delete();
				}
			}
		}

		// Exam_Calendar
		File Exam_Calendar = new File(SDPATH + "Exam_Calendar/");
		if (Exam_Calendar.exists() && Exam_Calendar.isDirectory()) {
			File files_Infos_list[] = Exam_Calendar.listFiles();
			for (int i = 0; i < files_Infos_list.length; i++) {
				files_Infos_list[i].delete();
			}
		}
	}

	public void deleteDirAll(File fileName) {
		if (fileName.exists() && fileName.isDirectory()) {
			File files_Image_list[] = fileName.listFiles();
			for (int i = 0; i < files_Image_list.length; i++) {
				deleteDirAll(files_Image_list[i]);
			}
		} else {
			fileName.delete();
		}
		fileName.delete();
	}

	public void deleteFile(String fileName) {
		File filepath = new File(SDPATH);
		if (filepath.exists() && filepath.isDirectory()) {
			fileName = SDPATH + fileName;
			File file = new File(fileName);
			if (file.exists())
				file.delete();
		}
	}

	public boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public boolean createPath(String path) {
		File f = new File(path);
		if (!f.exists()) {
			Boolean o = f.mkdirs();
			return o;
		}
		return true;
	}

	public boolean exists(String file) {
		return new File(file).exists();
	}

	public File saveFile(String file, InputStream inputStream) {
		File f = null;
		OutputStream outSm = null;

		try {
			f = new File(file);
			String path = f.getParent();
			if (!createPath(path)) {
				return null;
			}

			if (!f.exists()) {
				f.createNewFile();
			}

			outSm = new FileOutputStream(f);
			byte[] buffer = new byte[4 * 1024];
			while ((inputStream.read(buffer)) != -1) {
				outSm.write(buffer);
			}
			outSm.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;

		} finally {
			try {
				if (outSm != null)
					outSm.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return f;
	}

	public Drawable getImageDrawable(String file) {
		if (!exists(file))
			return null;
		try {
			InputStream inp = new FileInputStream(new File(file));
			return BitmapDrawable.createFromStream(inp, "img");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getUrlImageName(String url) {
		String a[] = url.split("/");
		String img_name = a[((int) a.length) - 1];
		return img_name;
	}
}
